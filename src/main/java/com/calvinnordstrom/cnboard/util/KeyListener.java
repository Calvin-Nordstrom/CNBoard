package com.calvinnordstrom.cnboard.util;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.util.HashSet;
import java.util.Set;

public class KeyListener implements NativeKeyListener {
    private final Set<Integer> activeKeys = new HashSet<>();

    public KeyListener() {
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {}

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        activeKeys.add(e.getKeyCode());
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        activeKeys.remove(e.getKeyCode());
    }
}
