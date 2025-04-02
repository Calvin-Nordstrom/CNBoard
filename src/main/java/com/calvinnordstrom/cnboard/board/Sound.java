package com.calvinnordstrom.cnboard.board;

import com.calvinnordstrom.cnboard.util.Resources;
import javafx.beans.property.*;

import java.io.*;

/**
 * Represents a sound with associated properties, such as title, icon, sound
 * file, keycode for activation, volume, and whether it is enabled. This class
 * supports serialization for persistence and provides methods to manage each
 * property.
 *
 * <p>The class contains properties for:</p>
 * <ul>
 *     <li>Title of the sound (title)</li>
 *     <li>Icon file for the sound (iconFile)</li>
 *     <li>Sound file for playback (soundFile)</li>
 *     <li>Key code to trigger the sound (keyCode)</li>
 *     <li>Volume level (volume)</li>
 *     <li>Whether the sound is enabled (enabled)</li>
 * </ul>
 *
 * <p>These properties can be bound to UI components and observed for changes.
 * </p>
 * <p>This class implements {@code Serializable} to allow for persistence with
 * files.</p>
 */
public class Sound implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private transient StringProperty title;
    private transient ObjectProperty<File> iconFile;
    private transient ObjectProperty<File> soundFile;
    private transient IntegerProperty keyCode;
    private transient DoubleProperty volume;
    private transient BooleanProperty enabled;

    private Sound(Builder builder) {
        title = new SimpleStringProperty(builder.title);
        iconFile = new SimpleObjectProperty<>(builder.iconFile);
        soundFile = new SimpleObjectProperty<>(builder.soundFile);
        keyCode = new SimpleIntegerProperty(builder.keyCode);
        volume = new SimpleDoubleProperty(builder.volume);
        enabled = new SimpleBooleanProperty(builder.enabled);
    }

    /**
     * Returns the title of the sound.
     *
     * @return the title of the sound
     */
    public String getTitle() {
        return title.get();
    }

    /**
     * Sets the title of the sound.
     *
     * @param title the title of the sound
     */
    public void setTitle(String title) {
        this.title.set(title);
    }

    /**
     * Returns the {@code StringProperty} for the title of the sound. This can
     * be used for binding UI components to the property and observed for
     * changes.
     *
     * @return the {@code StringProperty} for the title
     */
    public StringProperty titleProperty() {
        return title;
    }

    /**
     * Returns the icon file associated with the sound.
     *
     * @return the icon file
     */
    public File getIconFile() {
        return iconFile.get();
    }

    /**
     * Sets the icon file associated with the sound.
     *
     * @param iconFile the icon file
     */
    public void setIconFile(File iconFile) {
        this.iconFile.set(iconFile);
    }

    /**
     * Returns the {@code ObjectProperty} for the icon file. This can be used
     * for binding UI components to the property and observed for changes.
     *
     * @return the {@code ObjectProperty} for the icon file
     */
    public ObjectProperty<File> iconFileProperty() {
        return iconFile;
    }

    /**
     * Returns the sound file associated with the sound.
     *
     * @return the sound file
     */
    public File getSoundFile() {
        return soundFile.get();
    }

    /**
     * Sets the sound file associated with the sound.
     *
     * @param soundFile the sound file
     */
    public void setSoundFile(File soundFile) {
        this.soundFile.set(soundFile);
    }

    /**
     * Returns the {@code ObjectProperty} for the sound file. This can be used
     * for binding UI components to the property and observed for changes.
     *
     * @return the {@code ObjectProperty} for the sound file
     */
    public ObjectProperty<File> soundFileProperty() {
        return soundFile;
    }

    /**
     * Returns the key code that triggers the sound.
     *
     * @return the key code to trigger the sound
     */
    public int getKeyCode() {
        return keyCode.get();
    }

    /**
     * Sets the key code that triggers the sound.
     *
     * @param keyCode the key code to trigger the sound
     */
    public void setKeyCode(int keyCode) {
        this.keyCode.set(keyCode);
    }

    /**
     * Returns the {@code IntegerProperty} for the key code. This can be used
     * for binding UI components to the property and observed for changes.
     *
     * @return the {@code IntegerProperty} for the key code
     */
    public IntegerProperty keyCodeProperty() {
        return keyCode;
    }

    /**
     * Returns the volume level of the sound.
     *
     * @return the volume level of the sound
     */
    public double getVolume() {
        return volume.get();
    }

    /**
     * Sets the volume level of the sound.
     *
     * @param volume the volume level of the sound
     */
    public void setVolume(double volume) {
        this.volume.set(volume);
    }

    /**
     * Returns the {@code DoubleProperty} for the volume. This can be used for
     * binding UI components to the property and observed for changes.
     *
     * @return the {@code DoubleProperty} for the volume
     */
    public DoubleProperty volumeProperty() {
        return volume;
    }

    /**
     * Returns whether the sound is enabled.
     *
     * @return {@code true} if the sound is enabled, {@code false} otherwise
     */
    public boolean isEnabled() {
        return enabled.get();
    }

    /**
     * Sets whether the sound is enabled.
     *
     * @param enabled {@code true} to enable the sound, {@code false} to
     * disable it
     */
    public void setEnabled(boolean enabled) {
        this.enabled.set(enabled);
    }

    /**
     * Returns the {@code BooleanProperty} for whether the sound is enabled.
     * This can be used for binding UI components to the property and observed
     * for changes.
     *
     * @return the {@code BooleanProperty} for whether the sound is enabled
     */
    public BooleanProperty enabledProperty() {
        return enabled;
    }

    /**
     * Builder class to construct a {@code Sound} object with customizable
     * properties.
     */
    public static class Builder {
        private String title = "New Sound";
        private File iconFile = Resources.DEFAULT_ICON_FILE;
        private File soundFile;
        private int keyCode = 0;
        private double volume = 100.0d;
        private boolean enabled = true;

        /**
         * Sets the title of the sound.
         *
         * @param title the title of the sound
         * @return the builder instance
         */
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        /**
         * Sets the icon file of the sound.
         *
         * @param iconFile the icon file
         * @return the builder instance
         */
        public Builder iconFile(File iconFile) {
            this.iconFile = iconFile;
            return this;
        }

        /**
         * Sets the sound file for playback.
         *
         * @param soundFile the sound file
         * @return the builder instance
         */
        public Builder soundFile(File soundFile) {
            this.soundFile = soundFile;
            return this;
        }

        /**
         * Sets the key code that triggers the sound.
         *
         * @param keyCode the key code to trigger the sound
         * @return the builder instance
         */
        public Builder keyCode(int keyCode) {
            this.keyCode = keyCode;
            return this;
        }

        /**
         * Sets the volume level of the sound.
         *
         * @param volume the volume level of the sound
         * @return the builder instance
         */
        public Builder volume(double volume) {
            this.volume = volume;
            return this;
        }

        /**
         * Sets whether the sound is enabled.
         *
         * @param enabled {@code true} to enable the sound, {@code false} to
         * disable it
         * @return the builder instance
         */
        public Builder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        /**
         * Builds a {@code Sound} object using the specified properties.
         *
         * @return a new {@code Sound} instance
         * @throws IllegalArgumentException if any required property is invalid
         */
        public Sound build() {
            if (title == null) {
                throw new IllegalArgumentException("Title cannot be null");
            }
            if (iconFile == null || !iconFile.exists()) {
                throw new IllegalArgumentException("Icon file must exist");
            }
            if (soundFile == null || !soundFile.exists()) {
                throw new IllegalArgumentException("Sound file must exist");
            }
            if (keyCode < 0) {
                throw new IllegalArgumentException("Key code cannot be negative");
            }
            if (volume < 0 || volume > 100) {
                throw new IllegalArgumentException("Volume must be between 0 and 100");
            }
            return new Sound(this);
        }
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(title.get());
        out.writeObject(iconFile.get());
        out.writeObject(soundFile.get());
        out.writeInt(keyCode.get());
        out.writeDouble(volume.get());
        out.writeBoolean(enabled.get());
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        title = new SimpleStringProperty((String) in.readObject());
        iconFile = new SimpleObjectProperty<>((File) in.readObject());
        soundFile = new SimpleObjectProperty<>((File) in.readObject());
        keyCode = new SimpleIntegerProperty(in.readInt());
        volume = new SimpleDoubleProperty(in.readDouble());
        enabled = new SimpleBooleanProperty(in.readBoolean());
    }
}
