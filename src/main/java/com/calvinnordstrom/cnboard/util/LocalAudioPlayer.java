package com.calvinnordstrom.cnboard.util;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public final class LocalAudioPlayer {
    private Clip clip;

    public void start(File file, float volume) {
        if (!file.exists()) return;
        float clampedVolume = Math.clamp(volume, 0.0f, 1.0f);

        stop();

        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(ais);
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float value = 20f * (float) Math.log10(clampedVolume) + 10f;
            float min = gainControl.getMinimum();
            float max = gainControl.getMaximum();
            gainControl.setValue(Math.clamp(value, min, max));
            clip.start();
        } catch (UnsupportedAudioFileException
                 | IOException
                 | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        if (clip != null && clip.isOpen()) {
            clip.stop();
        }
    }
}
