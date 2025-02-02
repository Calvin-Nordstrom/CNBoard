package com.calvinnordstrom.cnboard.board;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioRouter {
    private static final int INPUT_BUFFER_SIZE = 512;
    private static final int OUTPUT_BUFFER_SIZE = 512;
    private final TargetDataLine inputLine;
    private final SourceDataLine outputLine;
    private Clip clip;
    private volatile boolean running = false;
    private volatile boolean isPlaying = false;
    private Thread injectionThread;

    public AudioRouter(TargetDataLine inputLine, SourceDataLine outputLine) {
        this.inputLine = inputLine;
        this.outputLine = outputLine;
    }

    public synchronized void start() {
        if (running) return;

        running = true;
        try {
            inputLine.open();
            inputLine.start();
            outputLine.open();
            outputLine.start();
        } catch (LineUnavailableException e) {
            System.err.println(e.getMessage());
        }

        Thread microphoneThread = new Thread(this::routeInput);
        microphoneThread.start();
    }

    public synchronized void stop() {
        if (!running) return;

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
                }
            }
        } finally {
            stop();
        }
    }

    public synchronized void injectAudio(File file, float volume, boolean playback) {
        stopInjection();

        injectionThread = new Thread(() -> {
            try {
                AudioInputStream ais = AudioSystem.getAudioInputStream(file);
                AudioInputStream aisCopy = AudioSystem.getAudioInputStream(file);
                AudioFormat format = ais.getFormat();
                AudioFormat outputFormat = outputLine.getFormat();

                if (!format.matches(outputFormat)) {
                    ais = AudioSystem.getAudioInputStream(outputFormat, ais);
                    aisCopy = AudioSystem.getAudioInputStream(outputFormat, aisCopy);
                }

                if (playback) {
                    try {
                        clip = AudioSystem.getClip();
                        clip.open(ais);
                        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                        float value = clamp(volume, -20, gainControl.getMaximum());
                        gainControl.setValue(value);
                        clip.start();
                    } catch (LineUnavailableException e) {
                        System.err.println(e.getMessage());
                    }
                }

                isPlaying = true;
                byte[] buffer = new byte[OUTPUT_BUFFER_SIZE];
                int bytesRead;
                while (isPlaying && (bytesRead = aisCopy.read(buffer)) != -1) {
                    if (Thread.currentThread().isInterrupted()) break;
                    scaleVolume(buffer, bytesRead, format, volume);
                    outputLine.write(buffer, 0, bytesRead);
                }

                ais.close();
                aisCopy.close();
            } catch (UnsupportedAudioFileException | IOException e) {
                System.err.println(e.getMessage());
            } finally {
                outputLine.drain();
                outputLine.flush();
                isPlaying = false;
            }
        });
        injectionThread.start();
    }

    public synchronized void stopInjection() {
        isPlaying = false;

        if (injectionThread != null && injectionThread.isAlive()) {
            injectionThread.interrupt();
        }

        if (clip != null && clip.isOpen()) {
            clip.stop();
        }

        outputLine.flush();
    }

    private static void scaleVolume(byte[] buffer, int bytesRead, AudioFormat format, float scale) {
        if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) return;

        int sampleSizeInBytes = format.getSampleSizeInBits() / 8;
        boolean isBigEndian = format.isBigEndian();

        for (int i = 0; i < bytesRead; i += sampleSizeInBytes) {
            int sample = 0;

            if (sampleSizeInBytes == 2) {
                if (isBigEndian) {
                    sample = (buffer[i] << 8) | (buffer[i + 1] & 0xFF);
                } else {
                    sample = (buffer[i + 1] << 8) | (buffer[i] & 0xFF);
                }
            } else if (sampleSizeInBytes == 1) {
                sample = buffer[i] - 128;
            }

            sample = (int) (sample * scale);

            if (sampleSizeInBytes == 2) {
                if (isBigEndian) {
                    buffer[i] = (byte) ((sample >> 8) & 0xFF);
                    buffer[i + 1] = (byte) (sample & 0xFF);
                } else {
                    buffer[i] = (byte) (sample & 0xFF);
                    buffer[i + 1] = (byte) ((sample >> 8) & 0xFF);
                }
            } else if (sampleSizeInBytes == 1) {
                buffer[i] = (byte) (sample + 128);
            }
        }
    }

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, min + value * (max - min)));
    }
}
