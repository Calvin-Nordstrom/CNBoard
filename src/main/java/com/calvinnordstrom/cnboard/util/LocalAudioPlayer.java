package com.calvinnordstrom.cnboard.util;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public final class LocalAudioPlayer {
    private static LocalAudioPlayer instance;
    private Clip clip;

    private LocalAudioPlayer() {}

    public void start(File file) {
        if (file.exists()) {
            stop();
            try {
                AudioInputStream ais = AudioSystem.getAudioInputStream(file);
                clip = AudioSystem.getClip();
                clip.open(ais);
                clip.start();
            } catch (UnsupportedAudioFileException
                     | IOException
                     | LineUnavailableException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void stop() {
        if (clip != null) clip.stop();
    }

    public static LocalAudioPlayer getInstance() {
        if (instance == null) {
            instance = new LocalAudioPlayer();
        }
        return instance;
    }
}
