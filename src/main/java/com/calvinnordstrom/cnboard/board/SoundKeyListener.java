package com.calvinnordstrom.cnboard.board;

import javafx.collections.ObservableList;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class SoundKeyListener implements NativeKeyListener {
    private final ObservableList<Sound> sounds;
    private final Set<Integer> activeKeys = new HashSet<>();

    public SoundKeyListener(ObservableList<Sound> sounds) {
        this.sounds = sounds;
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent event) {}

    @Override
    public void nativeKeyPressed(NativeKeyEvent event) {
        int keyCode = event.getKeyCode();
        if (!activeKeys.contains(keyCode)) {
            for (Sound sound : sounds) {
                if (sound.getKeyCode() == keyCode) {

                    // Consider moving all of this to a SoundPlayer class:
                    // Instead of this code, use a consumer to accept and run
                    // the callback so that BoardManager can handle it.
                    File soundFile = sound.getSoundFile();
                    if (soundFile.exists()) {
                        try {
                            AudioInputStream ais = AudioSystem.getAudioInputStream(soundFile);
                            Clip clip = AudioSystem.getClip();
                            clip.open(ais);
                            clip.start();
                        } catch (UnsupportedAudioFileException
                                 | IOException
                                 | LineUnavailableException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
        activeKeys.add(keyCode);
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent event) {
        activeKeys.remove(event.getKeyCode());
    }
}
