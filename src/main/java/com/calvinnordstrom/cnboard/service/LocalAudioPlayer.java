package com.calvinnordstrom.cnboard.service;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * A class that provides simple audio playback functionality for local audio
 * files.
 * <p>
 * This class uses the Java Sound API to load and play an audio file. Volume
 * may be specified as a float expected to be within 0.0 and 1.0. This value is
 * to be clamped between this range, if the specified value is outside of these
 * bounds.
 */
public final class LocalAudioPlayer {
    private Clip clip;

    /**
     * Starts playing the specified audio file at the given volume.
     * <p>
     * If an audio file is already being played, it will stop that sound to
     * start playing the specified audio.
     * <p>
     * If the audio file does not exist, this method returns immediately.
     * <p>
     * The provided volume is clamped
     * between 0.0 and 1.0. The minimum value 0.0 means the sound will be
     * inaudible. The maximum value 1.0 is the loudest sound that can be played
     * using the MASTER_GAIN control.
     *
     * @param file the audio file to play
     * @param volume the playback volume, where 0.0 is inaudible and 1.0 is
     *               maximum volume
     * @throws RuntimeException if an error occurs while loading or playing
     * the audio file.
     */
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

    /**
     * Stops the currently playing audio clip.
     * <p>
     * If no audio is playing or the clip is not open, this method does
     * nothing.
     */
    public void stop() {
        if (clip != null && clip.isOpen()) {
            clip.stop();
        }
    }
}
