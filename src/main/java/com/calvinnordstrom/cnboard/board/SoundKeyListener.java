package com.calvinnordstrom.cnboard.board;

import javafx.collections.ObservableList;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class GlobalKeyListener implements NativeKeyListener {
    private final ObservableList<Sound> sounds;
    private final Set<Integer> activeKeys = new HashSet<>();

    public GlobalKeyListener(ObservableList<Sound> sounds) {
        this.sounds = sounds;
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {}

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        int keyCode = e.getKeyCode();
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
                                 | LineUnavailableException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }
        }
        activeKeys.add(keyCode);
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        activeKeys.remove(e.getKeyCode());
    }
}
