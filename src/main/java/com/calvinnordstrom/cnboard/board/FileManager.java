package com.calvinnordstrom.cnboard.board;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the saving and loading of the model data, including sounds and
 * settings. This class provides methods to serialize and deserialize data
 * to and from files stored in a directory in the system's application data
 * folder.
 *
 * <p>The class handles the following operations:</p>
 * <ul>
 *     <li>Saving and loading sound data</li>
 *     <li>Saving and loading application settings</li>
 *     <li>Checking the existence of data files</li>
 * </ul>
 *
 * <p>Files are saved in the directory {@code "CNBoard"} within the system's
 * {@code APPDATA} folder.</p>
 */
public class FileManager {
    private final File directory = new File(System.getenv("APPDATA"), "CNBoard");
    private final File soundsFile = new File(directory, "sounds.ser");
    private final File settingsFile = new File(directory, "settings.ser");

    /**
     * Constructs a new {@link FileManager} instance. When constructed, the
     * directory used to store all files is created if it doesn't already
     * exist.
     */
    public FileManager() {
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    /**
     * Saves the specified list of {@link Sound} objects to a file. The sounds
     * are serialized and written to the {@code sounds.ser} file.
     *
     * @param sounds the list of {@link Sound} objects to be saved
     */
    public void saveSounds(ObservableList<Sound> sounds) {
        try {
            serialize(new ArrayList<>(sounds), soundsFile);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Loads the list of {@link Sound} objects from the file. The list is
     * deserialized from the {@code sounds.ser} file. If the file contents
     * are invalid, a new list of {@link Sound} objects is returned.
     *
     * @return an {@link ObservableList} of {@link Sound} objects, or an empty
     * {@link ObservableList} if deserialization fails.
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
        return FXCollections.observableArrayList();
    }

    /**
     * Saves the specified {@link Settings} object to a file. The settings
     * are serialized and written to the {@code settings.ser} file.
     *
     * @param settings the {@link Settings} object to be saved
     */
    public void saveSettings(Settings settings) {
        try {
            serialize(settings, settingsFile);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Loads the {@link Settings} object from the file. The settings are
     * deserialized from the {@code settings.ser} file. If the file contents
     * are invalid, a new {@link Settings} object is returned.
     *
     * @return the {@link Settings} object, or a default {@link Settings}
     * object if deserialization fails.
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
}
