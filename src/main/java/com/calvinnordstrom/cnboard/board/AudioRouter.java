package com.calvinnordstrom.cnboard.board;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioRouter {
    private static final int INPUT_BUFFER_SIZE = 512;
    private static final int OUTPUT_BUFFER_SIZE = 512;
    private final TargetDataLine inputLine;
    private final SourceDataLine outputLine;
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

    public synchronized void injectAudio(File file) {
        stopInjection();

        injectionThread = new Thread(() -> {
            try {
                AudioInputStream ais = AudioSystem.getAudioInputStream(file);
                AudioFormat format = ais.getFormat();
                AudioFormat outputFormat = outputLine.getFormat();

                if (!format.matches(outputFormat)) {
                    ais = AudioSystem.getAudioInputStream(outputFormat, ais);
                }

                isPlaying = true;
                byte[] buffer = new byte[OUTPUT_BUFFER_SIZE];
                int bytesRead;
                while (isPlaying && (bytesRead = ais.read(buffer)) != -1) {
                    if (Thread.currentThread().isInterrupted()) break;
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
        });

        injectionThread.start();
    }

    public synchronized void stopInjection() {
        isPlaying = false;
        if (injectionThread != null && injectionThread.isAlive()) {
            injectionThread.interrupt();
        }
    }
}
