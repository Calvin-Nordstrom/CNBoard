package com.calvinnordstrom.cnboard.board;

import javafx.collections.ObservableList;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class KeyListener implements NativeKeyListener {
    private static final long COOLDOWN_MS = 100;
    private final ObservableList<Sound> sounds;
    private final AudioRouter router;
    private final Set<Integer> activeKeys = new HashSet<>();
    private long lastPressedTime = 0;

    public KeyListener(ObservableList<Sound> sounds, AudioRouter router) {
        this.sounds = sounds;
        this.router = router;
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent event) {}

    @Override
    public void nativeKeyPressed(NativeKeyEvent event) {
        int keyCode = event.getKeyCode();

        // REPLACE VC_SPACE WITH REAL "STOP ALL" KEY
        if (keyCode == NativeKeyEvent.VC_SPACE) {
            router.stopInjection();
        }

        if (lastPressedTime + COOLDOWN_MS > System.currentTimeMillis()) return;

        if (!activeKeys.contains(keyCode)) {
            for (Sound sound : sounds) {
                if (sound.getKeyCode() == keyCode) {
                    if (sound.isEnabled()) {
                        File soundFile = sound.getSoundFile();
                        if (soundFile.exists()) {
                            router.injectAudio(soundFile);
                            lastPressedTime = System.currentTimeMillis();
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
