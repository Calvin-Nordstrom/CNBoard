package com.calvinnordstrom.cnboard.board;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class KeyListener implements NativeKeyListener {
    private final BoardController controller;

    public KeyListener(BoardController controller) {
        this.controller = controller;
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent event) {}

    @Override
    public void nativeKeyPressed(NativeKeyEvent event) {
        controller.onKeyPressed(event.getKeyCode());
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent event) {
        controller.onKeyReleased(event.getKeyCode());
    }
}
