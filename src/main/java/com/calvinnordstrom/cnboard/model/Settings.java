package com.calvinnordstrom.cnboard.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.*;

/**
 * Represents the settings for global sound-related controls. This class allows
 * for controlling whether sounds can be heard locally and what keycode should
 * be used to stop the sounds while playing. These settings are serialized to
 * support persistence.
 *
 * <p>The class contains properties for:</p>
 * <ul>
 *     <li>Whether the sounds can be heard locally (hearSounds)</li>
 *     <li>The keycode that stops the sounds (stopSoundsKeyCode)</li>
 * </ul>
 *
 * <p>These properties can also be bound to UI components and observed for
 * changes.</p>
 * <p>This class implements {@code Serializable} to allow for persistence with
 * files.</p>
 */
public class Settings implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private transient BooleanProperty hearSounds = new SimpleBooleanProperty(true);
    private transient IntegerProperty stopSoundsKeyCode = new SimpleIntegerProperty(0);

    /**
     * Returns whether sounds can be heard locally.
     *
     * @return {@code true} if sounds can be heard locally, {@code false}
     * otherwise
     */
    public boolean canHearSounds() {
        return hearSounds.get();
    }

    /**
     * Sets whether sounds can be heard locally.
     *
     * @param hearSounds {@code true} to allow hearing sounds, {@code false}
     * otherwise
     */
    public void setHearSounds(boolean hearSounds) {
        this.hearSounds.set(hearSounds);
    }

    /**
     * Returns the {@code BooleanProperty} for whether sounds can be heard
     * locally. This can be used for binding UI components to the property
     * and observed for changes.
     *
     * @return the {@code BooleanProperty} for hearing sounds locally
     */
    public BooleanProperty hearSoundsProperty() {
        return hearSounds;
    }

    /**
     * Returns the keycode that stops the sounds while playing.
     *
     * @return the keycode to stop the sounds
     */
    public int getStopSoundsKeyCode() {
        return stopSoundsKeyCode.get();
    }

    /**
     * Sets the keycode that stops the sounds while playing.
     *
     * @param stopSoundsKeyCode the keycode to stop the sounds
     */
    public void setStopSoundsKeyCode(int stopSoundsKeyCode) {
        this.stopSoundsKeyCode.set(stopSoundsKeyCode);
    }

    /**
     * Returns the {@code IntegerProperty} for the keycode that stops the
     * sounds while playing. This can be used for binding UI components to the
     * property and observed for changes.
     *
     * @return the {@code IntegerProperty} for the keycode that stops the
     * sounds while playing
     */
    public IntegerProperty stopSoundsKeyCodeProperty() {
        return stopSoundsKeyCode;
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeBoolean(hearSounds.get());
        out.writeInt(stopSoundsKeyCode.get());
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        hearSounds = new SimpleBooleanProperty(in.readBoolean());
        stopSoundsKeyCode = new SimpleIntegerProperty(in.readInt());
    }
}
