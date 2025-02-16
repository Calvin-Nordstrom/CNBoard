package com.calvinnordstrom.cnboard.board;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/**
 * Handles the serialization and deserialization of the model, such as
 * {@link Sound} objects and {@link Settings} objects. This class provides
 * methods to save and load {@link Sound} objects and {@link Settings} objects
 * directly, and attach listeners to the properties of these them.
 *
 * <p>When listeners are attached to {@link Sound} objects, any time the list
 * or its elements change, it will be serialized.</p>
 *
 * <p>When listeners are attached to {@link Settings} objects, any time its
 * properties change, it will be serialized.</p>
 */
public class ModelSerializer {
    private final FileManager fileManager = new FileManager();

    /**
     * Attaches listeners to the specified list of {@link Sound} objects to
     * detect changes and invoke serialization. Whenever the list or its
     * elements change, serialization occurs.
     *
     * @param sounds the list of {@link Sound} objects
     */
    public void attachListeners(ObservableList<Sound> sounds) {
        for (Sound sound : sounds) {
            attachPropertyListeners(sound, sounds);
        }

        sounds.addListener((ListChangeListener<Sound>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Sound sound : change.getAddedSubList()) {
                        attachPropertyListeners(sound, sounds);
                    }
                }
                if (change.wasRemoved()) {
                    for (Sound sound : change.getRemoved()) {
                        detachPropertyListeners(sound, sounds);
                    }
                }
            }
            saveSounds(sounds);
        });
    }

    private void attachPropertyListeners(Sound sound, ObservableList<Sound> sounds) {
        sound.titleProperty().addListener((_, _, _) -> saveSounds(sounds));
        sound.iconFileProperty().addListener((_, _, _) -> saveSounds(sounds));
        sound.soundFileProperty().addListener((_, _, _) -> saveSounds(sounds));
        sound.keyCodeProperty().addListener((_, _, _) -> saveSounds(sounds));
        sound.volumeProperty().addListener((_, _, _) -> saveSounds(sounds));
        sound.enabledProperty().addListener((_, _, _) -> saveSounds(sounds));
    }

    private void detachPropertyListeners(Sound sound, ObservableList<Sound> sounds) {
        sound.titleProperty().removeListener((_, _, _) -> saveSounds(sounds));
        sound.iconFileProperty().removeListener((_, _, _) -> saveSounds(sounds));
        sound.soundFileProperty().removeListener((_, _, _) -> saveSounds(sounds));
        sound.keyCodeProperty().removeListener((_, _, _) -> saveSounds(sounds));
        sound.volumeProperty().removeListener((_, _, _) -> saveSounds(sounds));
        sound.enabledProperty().removeListener((_, _, _) -> saveSounds(sounds));
    }

    /**
     * Saves the list of {@link Sound} objects to storage.
     *
     * @param sounds the list of {@link Sound} objects to serialize
     */
    public void saveSounds(ObservableList<Sound> sounds) {
        fileManager.saveSounds(sounds);
    }

    /**
     * Loads the list of {@link Sound} objects from storage. If the file does
     * not exist, a new list containing default sounds is returned.
     *
     * @return the list of {@link Sound} objects from storage
     */
    public ObservableList<Sound> loadSounds() {
        boolean soundsFileExists = fileManager.soundsFileExists();
        ObservableList<Sound> sounds = fileManager.loadSounds();

        if (!soundsFileExists) {
            sounds.add(Sounds.createBruhSound());
            sounds.add(Sounds.createTacoBellSound());
        }

        return sounds;
    }

    /**
     * Attaches listeners to the specified {@link Settings} object to
     * detect changes and invoke serialization. Whenever the properties of the
     * {@link Settings} object change, serialization occurs.
     *
     * @param settings the {@link Settings} object
     */
    public void attachListeners(Settings settings) {
        settings.hearSoundsProperty().addListener((_, _, _) -> saveSettings(settings));
        settings.stopSoundsKeyCodeProperty().addListener((_, _, _) -> saveSettings(settings));
    }

    /**
     * Saves the {@link Settings} object to storage.
     *
     * @param settings the {@link Settings} object to serialize
     */
    public void saveSettings(Settings settings) {
        fileManager.saveSettings(settings);
    }

    /**
     * Loads the {@link Settings} object from storage.
     *
     * @return the {@link Settings} object from storage
     */
    public Settings loadSettings() {
        return fileManager.loadSettings();
    }
}
