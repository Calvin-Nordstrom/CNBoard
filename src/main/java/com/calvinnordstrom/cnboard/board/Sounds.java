package com.calvinnordstrom.cnboard.board;

import com.calvinnordstrom.cnboard.util.Resources;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jnativehook.keyboard.NativeKeyEvent;

/**
 * The {@code Sounds} class provides utility methods to create default
 * {@code Sound} objects. It includes methods to generate predefined objects
 * with default values.
 */
public class Sounds {
    /**
     * Creates a list of default sounds.
     *
     * @return an {@code ObservableList} containing predefined sounds.
     */
    public static ObservableList<Sound> createDefaultSounds() {
        ObservableList<Sound> sounds = FXCollections.observableArrayList();
        sounds.add(createBruhSound());
        sounds.add(createTacoBellSound());
        return sounds;
    }

    /**
     * Creates the "Bruh" sound with predefined settings.
     *
     * @return a {@code Sound} object representing the "Bruh" sound.
     */
    public static Sound createBruhSound() {
        return new Sound.Builder()
                .title("Bruh")
                .iconFile(Resources.BRUH_ICON_FILE)
                .soundFile(Resources.BRUH_SOUND_FILE)
                .keyCode(NativeKeyEvent.VC_1)
                .volume(100)
                .enabled(true)
                .build();
    }

    /**
     * Creates the "Taco Bell" sound with predefined settings.
     *
     * @return a {@code Sound} object representing the "Taco Bell" sound.
     */
    public static Sound createTacoBellSound() {
        return new Sound.Builder()
                .title("Taco Bell")
                .iconFile(Resources.TACO_BELL_ICON_FILE)
                .soundFile(Resources.TACO_BELL_SOUND_FILE)
                .keyCode(NativeKeyEvent.VC_2)
                .volume(100)
                .enabled(true)
                .build();
    }
}
