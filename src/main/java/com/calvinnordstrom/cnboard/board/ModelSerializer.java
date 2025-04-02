package com.calvinnordstrom.cnboard.board;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
    private final File directory = new File(System.getenv("APPDATA"), "CNBoard");
    private final File soundsFile = new File(directory, "sounds.ser");
    private final File settingsFile = new File(directory, "settings.ser");

    /**
     * Constructs a new {@link ModelSerializer} instance. When constructed, the
     * directory used to store all files is created if it doesn't already
     * exist.
     */
    public ModelSerializer() {
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    /**
     * Attaches listeners to the specified list of {@link Sound} objects to
     * detect changes and invoke serialization. Whenever the list or its
     * elements change, serialization occurs.
     *
     * @param sounds the list of {@link Sound} objects
     */
    public void attachSoundListeners(ObservableList<Sound> sounds) {
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
        try {
            serialize(new ArrayList<>(sounds), soundsFile);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Loads the list of {@link Sound} objects from storage. If the file does
     * not exist, a new list containing default sounds is returned.
     *
     * @return the list of {@link Sound} objects from storage
     */
    public ObservableList<Sound> loadSounds() {
        try {
            List<Sound> loadedList = deserialize(soundsFile, ArrayList.class);
            if (loadedList != null) {
                return FXCollections.observableArrayList(loadedList);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
        return Sounds.createDefaultSounds();
    }

    /**
     * Attaches listeners to the specified {@link Settings} object to
     * detect changes and invoke serialization. Whenever the properties of the
     * {@link Settings} object change, serialization occurs.
     *
     * @param settings the {@link Settings} object
     */
    public void attachSettingsListeners(Settings settings) {
        settings.hearSoundsProperty().addListener((_, _, _) -> saveSettings(settings));
        settings.stopSoundsKeyCodeProperty().addListener((_, _, _) -> saveSettings(settings));
    }

    /**
     * Saves the {@link Settings} object to storage.
     *
     * @param settings the {@link Settings} object to serialize
     */
    public void saveSettings(Settings settings) {
        try {
            serialize(settings, settingsFile);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Loads the {@link Settings} object from storage.
     *
     * @return the {@link Settings} object from storage
     */
    public Settings loadSettings() {
        try {
            Settings settings = deserialize(settingsFile, Settings.class);
            if (settings != null) {
                return settings;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
        return new Settings();
    }

    /**
     * Checks if the sounds file exists.
     *
     * @return {@code true} if the sounds file exists, {@code false} otherwise
     */
    public boolean soundsFileExists() {
        return soundsFile.exists();
    }

    /**
     * Checks if the settings file exists.
     *
     * @return {@code true} if the settings file exists, {@code false} otherwise
     */
    public boolean settingsFileExists() {
        return settingsFile.exists();
    }

    private <T extends Serializable> void serialize(T obj, File file) throws IOException {
        File parentDir = file.getParentFile();
        if (parentDir != null) {
            parentDir.mkdirs();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(obj);
        }
    }

    private <T extends Serializable> T deserialize(File file, Class<T> type) throws IOException, ClassNotFoundException {
        if (!file.exists()) {
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            return type.cast(obj);
        }
    }
}
