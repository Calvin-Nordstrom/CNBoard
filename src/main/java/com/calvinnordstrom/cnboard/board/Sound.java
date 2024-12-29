package com.calvinnordstrom.cnboard.board;

import javafx.beans.property.*;

import java.io.File;

public class Sound {
    private final StringProperty title;
    private final ObjectProperty<File> iconFile;
    private final ObjectProperty<File> soundFile;
    private final IntegerProperty keyCode;
    private final DoubleProperty volume;
    private final BooleanProperty enabled;

    private Sound(Builder builder) {
        title = new SimpleStringProperty(builder.title);
        iconFile = new SimpleObjectProperty<>(builder.iconFile);
        soundFile = new SimpleObjectProperty<>(builder.soundFile);
        keyCode = new SimpleIntegerProperty(builder.keyCode);
        volume = new SimpleDoubleProperty(builder.volume);
        enabled = new SimpleBooleanProperty(builder.enabled);
    }

    // Title methods

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public StringProperty titleProperty() {
        return title;
    }

    // Icon file methods

    public File getIconFile() {
        return iconFile.get();
    }

    public void setIconFile(File iconFile) {
        this.iconFile.set(iconFile);
    }

    public ObjectProperty<File> iconFileProperty() {
        return iconFile;
    }

    // Sound file methods

    public File getSoundFile() {
        return soundFile.get();
    }

    public void setSoundFile(File soundFile) {
        this.soundFile.set(soundFile);
    }

    public ObjectProperty<File> soundFileProperty() {
        return soundFile;
    }

    // Key code methods

    public int getKeyCode() {
        return keyCode.get();
    }

    public void setKeyCode(int keyCode) {
        this.keyCode.set(keyCode);
    }

    public IntegerProperty keyCodeProperty() {
        return keyCode;
    }

    // Volume methods

    public double getVolume() {
        return volume.get();
    }

    public void setVolume(double volume) {
        this.volume.set(volume);
    }

    public DoubleProperty volumeProperty() {
        return volume;
    }

    // Enabled methods

    public boolean isEnabled() {
        return enabled.get();
    }

    public void setEnabled(boolean enabled) {
        this.enabled.set(enabled);
    }

    public BooleanProperty enabledProperty() {
        return enabled;
    }

    public static class Builder {
        private String title = "New Sound";
        private File iconFile;
        private File soundFile;
        private int keyCode;
        private double volume = 50.0d;
        private boolean enabled = true;

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder iconFile(File iconFile) {
            this.iconFile = iconFile;
            return this;
        }

        public Builder soundFile(File soundFile) {
            this.soundFile = soundFile;
            return this;
        }

        public Builder keyCode(int keyCode) {
            this.keyCode = keyCode;
            return this;
        }

        public Builder volume(double volume) {
            this.volume = volume;
            return this;
        }

        public Builder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Sound build() {
            return new Sound(this);
        }
    }
}
