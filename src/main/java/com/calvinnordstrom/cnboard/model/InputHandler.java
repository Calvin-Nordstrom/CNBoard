package com.calvinnordstrom.cnboard.model;

import com.calvinnordstrom.cnboard.service.AudioRouter;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Handles user input for triggering sound playback based on key events.
 * <p>
 * When a key is activated with th {@link InputHandler#onKeyPressed(int)}
 * method, it is added to a set. When {@link InputHandler#onKeyReleased(int)}
 * is invoked, it is removed from the set. This process ensures that a key must
 * be released before it can be activated again. This was done because key
 * press events can fire too rapidly to be useful for sound injection.
 */
public class InputHandler {
    private final List<Sound> sounds;
    private final Settings settings;
    private final AudioRouter router;
    private final Set<Integer> activeKeys = new HashSet<>();

    /**
     * Constructs an {@code InputHandler} with the specified sounds, settings,
     * and router to inject audio to.
     *
     * @param sounds the sounds to check input against
     * @param settings the settings to control playback
     * @param router the router to route audio to
     */
    public InputHandler(List<Sound> sounds, Settings settings, AudioRouter router) {
        this.sounds = sounds;
        this.settings = settings;
        this.router = router;
    }

    /**
     * Handles key press events, triggering sound injection if the key is
     * associated with a sound. If the key matches the stop-sound key, all
     * ongoing sound injections are stopped.
     *
     * @param keyCode the key code of the pressed key.
     */
    public void onKeyPressed(int keyCode) {
        if (keyCode == settings.getStopSoundsKeyCode()) {
            router.stopInjection();
        }

        if (!activeKeys.contains(keyCode)) {
            for (Sound sound : sounds) {
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

    /**
     * Handles key release events, removing the key from the set of active
     * keys.
     *
     * @param keyCode the key code of the released key.
     */
    public void onKeyReleased(int keyCode) {
        activeKeys.remove(keyCode);
    }
}
