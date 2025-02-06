package com.calvinnordstrom.cnboard.board;

import com.calvinnordstrom.cnboard.util.LocalAudioPlayer;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * The {@code AudioRouter} class is responsible for routing audio data between
 * an input audio line (such as a microphone) and an output audio line (such as
 * speakers). In addition, it supports injecting audio from a file into the
 * output stream with configurable volume and optional local playback.
 * <p>
 * Audio routing is performed in a dedicated thread that continuously reads
 * from the input line and writes to the output line unless an audio injection
 * is in progress.
 * </p>
 */
public class AudioRouter {
    private static final String ROUTER_THREAD_NAME = "AudioRouter Thread";
    private static final String INJECTION_THREAD_NAME = "AudioRouter Injection Thread";
    private static final int INPUT_BUFFER_SIZE = 512;
    private static final int OUTPUT_BUFFER_SIZE = 512;
    private final TargetDataLine inputLine;
    private final SourceDataLine outputLine;
    private final LocalAudioPlayer localAudioPlayer = new LocalAudioPlayer();
    private volatile boolean running = false;
    private volatile boolean isPlaying = false;
    private Thread injectionThread;

    /**
     * Constructs an {@code AudioRouter} with the specified input and output
     * audio lines.
     *
     * @param inputLine the target data line to read audio data from
     * @param outputLine the source data line to write audio data to
     */
    public AudioRouter(TargetDataLine inputLine, SourceDataLine outputLine) {
        this.inputLine = inputLine;
        this.outputLine = outputLine;
    }

    /**
     * Starts the audio routing process.
     * <p>
     * This method opens and starts both the input and output audio lines, then
     * creates and starts a new thread to route audio data from the input line
     * to the output line.
     * </p>
     */
    public synchronized void start() {
        if (running) {
            return;
        }

        running = true;
        try {
            inputLine.open();
            inputLine.start();
            outputLine.open();
            outputLine.start();
        } catch (LineUnavailableException e) {
            System.err.println(e.getMessage());
        }

        Thread microphoneThread = new Thread(this::routeInput, ROUTER_THREAD_NAME);
        microphoneThread.start();
    }

    /**
     * Stops the audio routing process.
     * <p>
     * This method stops any ongoing audio injection and closes both the input
     * and output audio lines.
     * </p>
     */
    public synchronized void stop() {
        if (!running) {
            return;
        }

        running = false;
        stopInjection();
        if (inputLine.isOpen()) {
            inputLine.close();
        }
        if (outputLine.isOpen()) {
            outputLine.close();
        }
    }

    private void routeInput() {
        byte[] buffer = new byte[INPUT_BUFFER_SIZE];
        try {
            while (running) {
                if (!isPlaying) {
                    int bytesRead = inputLine.read(buffer, 0, buffer.length);
                    if (bytesRead > 0) {
                        outputLine.write(buffer, 0, bytesRead);
                    }
                } else {
                    int available = inputLine.available();
                    if (available > 0) {
                        int bytesToRead = Math.min(available, buffer.length);
                        inputLine.read(buffer, 0, bytesToRead);
                    } else {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                }
            }
        } finally {
            stop();
        }
    }

    /**
     * Injects audio from the specified file into the output line with volume
     * scaling.
     * <p>
     * The method reads the audio data from the given file and, if necessary,
     * converts it to match the output line's audio format. If an audio file is
     * already being injected, this method will cancel it and inject the new
     * audio file. If the specified file does not exist, the method returns
     * without action. The provided volume is clamped between 0.0 and 1.0
     * before being applied. If the {@code playback} flag is set to
     * {@code true}, the audio is also played locally using the local audio
     * player. Audio injection is handled in a separate thread.
     * </p>
     * While audio is being injected, the data routed from the input line to
     * the output line is paused.
     * </p>
     *
     * @param file the audio file to inject
     * @param volume the volume scaling factor between [0.0, 1.0]
     * @param playback if {@code true}, the audio is played locally as well
     */
    public synchronized void injectAudio(File file, float volume, boolean playback) {
        if (!file.exists()) {
            return;
        }
        float clampedVolume = Math.clamp(volume, 0.0f, 1.0f);

        stopInjection();

        injectionThread = new Thread(() -> {
            try {
                AudioInputStream ais = AudioSystem.getAudioInputStream(file);
                AudioFormat format = ais.getFormat();
                AudioFormat outputFormat = outputLine.getFormat();

                if (!format.matches(outputFormat)) {
                    ais = AudioSystem.getAudioInputStream(outputFormat, ais);
                }

                if (playback) {
                    localAudioPlayer.start(file, clampedVolume);
                }

                isPlaying = true;
                byte[] buffer = new byte[OUTPUT_BUFFER_SIZE];
                int bytesRead;
                while (isPlaying && (bytesRead = ais.read(buffer)) != -1) {
                    if (Thread.currentThread().isInterrupted()) {
                        break;
                    }
                    scaleVolume(buffer, bytesRead, format, clampedVolume);
                    outputLine.write(buffer, 0, bytesRead);
                }

                ais.close();
            } catch (UnsupportedAudioFileException | IOException e) {
                System.err.println(e.getMessage());
            } finally {
                outputLine.drain();
                outputLine.flush();
                isPlaying = false;
            }
        }, INJECTION_THREAD_NAME);
        injectionThread.start();
    }

    /**
     * Stops any active audio injection.
     * <p>
     * This method stops the injected audio by interrupting the injection
     * thread if it is active, stopping the local audio player, and flushing
     * the output audio line.
     * </p>
     */
    public synchronized void stopInjection() {
        isPlaying = false;

        if (injectionThread != null && injectionThread.isAlive()) {
            injectionThread.interrupt();
        }

        localAudioPlayer.stop();

        outputLine.flush();
    }

    private static void scaleVolume(byte[] buffer, int bytesRead, AudioFormat format, float scale) {
        int sampleSizeInBytes = format.getSampleSizeInBits() / 8;
        boolean isBigEndian = format.isBigEndian();
        boolean isFloatingPoint = format.getEncoding() == AudioFormat.Encoding.PCM_FLOAT;

        for (int i = 0; i < bytesRead; i += sampleSizeInBytes) {
            if (isFloatingPoint) {
                float sample = readFloatingPointSample(buffer, i, sampleSizeInBytes, isBigEndian);
                sample = scaleAndClampFloat(sample, scale);
                writeFloatingPointSample(buffer, i, sample, sampleSizeInBytes, isBigEndian);
            } else {
                int sample = readSample(buffer, i, sampleSizeInBytes, isBigEndian);
                sample = scaleAndClamp(sample, scale, format.getSampleSizeInBits());
                writeSample(buffer, i, sample, sampleSizeInBytes, isBigEndian);
            }
        }
    }

    private static int readSample(byte[] buffer, int index, int sampleSizeInBytes, boolean isBigEndian) {
        int sample = 0;

        switch (sampleSizeInBytes) {
            case 1: // 8-bit PCM (Unsigned)
                sample = (buffer[index] & 0xFF) - 128;
                break;
            case 2: // 16-bit PCM
                sample = isBigEndian
                        ? (buffer[index] << 8) | (buffer[index + 1] & 0xFF)
                        : (buffer[index + 1] << 8) | (buffer[index] & 0xFF);
                break;
            case 3: // 24-bit PCM
                sample = isBigEndian
                        ? (buffer[index] << 16) | ((buffer[index + 1] & 0xFF) << 8) | (buffer[index + 2] & 0xFF)
                        : (buffer[index + 2] << 16) | ((buffer[index + 1] & 0xFF) << 8) | (buffer[index] & 0xFF);
                if ((sample & 0x800000) != 0) sample |= 0xFF000000;
                break;
            case 4: // 32-bit PCM (Integer)
                sample = isBigEndian
                        ? (buffer[index] << 24) | ((buffer[index + 1] & 0xFF) << 16) | ((buffer[index + 2] & 0xFF) << 8) | (buffer[index + 3] & 0xFF)
                        : (buffer[index + 3] << 24) | ((buffer[index + 2] & 0xFF) << 16) | ((buffer[index + 1] & 0xFF) << 8) | (buffer[index] & 0xFF);
                break;
        }

        return sample;
    }

    private static void writeSample(byte[] buffer, int index, int sample, int sampleSizeInBytes, boolean isBigEndian) {
        switch (sampleSizeInBytes) {
            case 1: // 8-bit PCM (Unsigned)
                buffer[index] = (byte) (sample + 128);
                break;
            case 2: // 16-bit PCM
                if (isBigEndian) {
                    buffer[index] = (byte) ((sample >> 8) & 0xFF);
                    buffer[index + 1] = (byte) (sample & 0xFF);
                } else {
                    buffer[index] = (byte) (sample & 0xFF);
                    buffer[index + 1] = (byte) ((sample >> 8) & 0xFF);
                }
                break;
            case 3: // 24-bit PCM
                if (isBigEndian) {
                    buffer[index] = (byte) ((sample >> 16) & 0xFF);
                    buffer[index + 1] = (byte) ((sample >> 8) & 0xFF);
                    buffer[index + 2] = (byte) (sample & 0xFF);
                } else {
                    buffer[index] = (byte) (sample & 0xFF);
                    buffer[index + 1] = (byte) ((sample >> 8) & 0xFF);
                    buffer[index + 2] = (byte) ((sample >> 16) & 0xFF);
                }
                break;
            case 4: // 32-bit PCM (Integer)
                if (isBigEndian) {
                    buffer[index] = (byte) ((sample >> 24) & 0xFF);
                    buffer[index + 1] = (byte) ((sample >> 16) & 0xFF);
                    buffer[index + 2] = (byte) ((sample >> 8) & 0xFF);
                    buffer[index + 3] = (byte) (sample & 0xFF);
                } else {
                    buffer[index] = (byte) (sample & 0xFF);
                    buffer[index + 1] = (byte) ((sample >> 8) & 0xFF);
                    buffer[index + 2] = (byte) ((sample >> 16) & 0xFF);
                    buffer[index + 3] = (byte) ((sample >> 24) & 0xFF);
                }
                break;
        }
    }

    private static float readFloatingPointSample(byte[] buffer, int index, int sampleSizeInBytes, boolean isBigEndian) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(buffer, index, sampleSizeInBytes);
        byteBuffer.order(isBigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);

        return sampleSizeInBytes == 4 ? byteBuffer.getFloat() : (float) byteBuffer.getDouble();
    }

    private static void writeFloatingPointSample(byte[] buffer, int index, float sample, int sampleSizeInBytes, boolean isBigEndian) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(buffer, index, sampleSizeInBytes);
        byteBuffer.order(isBigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);

        if (sampleSizeInBytes == 4) {
            byteBuffer.putFloat(sample);
        } else {
            byteBuffer.putDouble(sample);
        }
    }

    private static int scaleAndClamp(int sample, float scale, int sampleSizeInBits) {
        int maxSample = (1 << (sampleSizeInBits - 1)) - 1;
        int minSample = -maxSample - 1;
        sample = (int) (sample * scale);
        return Math.max(minSample, Math.min(maxSample, sample));
    }

    private static float scaleAndClampFloat(float sample, float scale) {
        sample *= scale;
        return Math.max(-1.0f, Math.min(1.0f, sample));
    }
}
