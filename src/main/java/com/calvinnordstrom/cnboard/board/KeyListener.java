package com.calvinnordstrom.cnboard.board;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/**
 * The {@code KeyListener} class implements {@link NativeKeyListener} to listen
 * for native key events.
 * <p>
 * Native key events are those that originate from outside of this application.
 * Listening for these events is required to allowing the application inject
 * sounds when the native key code matches a sound's activation key code.
 */
public class KeyListener implements NativeKeyListener {
    private final BoardController controller;

    public KeyListener(BoardController controller) {
        this.controller = controller;
    }

    /**
     * Invoked when a key has been typed.
     *
     * @param event the native key event.
     */
    @Override
    public void nativeKeyTyped(NativeKeyEvent event) {}

    /**
     * Invoked when a key has been pressed.
     *
     * @param event the native key event.
     */
    @Override
    public void nativeKeyPressed(NativeKeyEvent event) {
        controller.keyPress(event.getKeyCode());
    }

    /**
     * Invoked when a key has been released.
     *
     * @param event the native key event.
     */
    @Override
    public void nativeKeyReleased(NativeKeyEvent event) {
        controller.keyRelease(event.getKeyCode());
    }
}
