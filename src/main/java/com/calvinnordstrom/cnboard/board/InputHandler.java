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
        Settings settings = model.getSettings();
        AudioRouter router = model.getRouter();

        if (keyCode == settings.getStopSoundsKeyCode()) {
            router.stopInjection();
        }

        if (!activeKeys.contains(keyCode)) {
            for (Sound sound : model.getSounds()) {
                if (sound.getKeyCode() == keyCode) {
                    if (sound.isEnabled()) {
                        File soundFile = sound.getSoundFile();
                        if (soundFile.exists()) {
                            float volume = (float) sound.getVolume() / 100;
                            boolean playback = settings.canHearSounds();
                            router.injectAudio(soundFile, volume, playback);
                            break;
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
