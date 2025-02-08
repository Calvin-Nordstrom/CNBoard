package com.calvinnordstrom.cnboard.board;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class InputHandler {
    private final BoardModel model;
    private final Set<Integer> activeKeys = new HashSet<>();

    public InputHandler(BoardModel model) {
        this.model = model;
    }

    public void onKeyPressed(int keyCode) {
        if (keyCode == model.getSettings().getStopSoundsKeyCode()) {
            model.getRouter().stopInjection();
        }

        if (!activeKeys.contains(keyCode)) {
            for (Sound sound : model.getSounds()) {
                if (sound.getKeyCode() == keyCode) {
                    if (sound.isEnabled()) {
                        File soundFile = sound.getSoundFile();
                        if (soundFile.exists()) {
                            float volume = (float) sound.getVolume() / 100;
                            boolean playback = model.getSettings().canHearSounds();
                            model.getRouter().injectAudio(soundFile, volume, playback);
                        }
                    }
                }
            }
        }
        activeKeys.add(keyCode);
    }

    public void onKeyReleased(int keyCode) {
        activeKeys.remove(keyCode);
    }
}
