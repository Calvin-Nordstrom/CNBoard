package com.calvinnordstrom.cnboard.board;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.jnativehook.keyboard.NativeKeyEvent;

public class Settings {
    private final BooleanProperty hearSounds = new SimpleBooleanProperty(true);
    private final IntegerProperty stopSoundsKeyCode = new SimpleIntegerProperty(NativeKeyEvent.VC_P);

    public Settings() {

    }

    // Hear sounds methods

    public boolean canHearSounds() {
        return hearSounds.get();
    }

    public void setHearSounds(boolean hearSounds) {
        this.hearSounds.set(hearSounds);
    }

    public BooleanProperty hearSoundsProperty() {
        return hearSounds;
    }

    // Stop sounds key code methods

    public int getStopSoundsKeyCode() {
        return stopSoundsKeyCode.get();
    }

    public void setStopSoundsKeyCode(int stopSoundsKeyCode) {
        this.stopSoundsKeyCode.set(stopSoundsKeyCode);
    }

    public IntegerProperty stopSoundsKeyCodeProperty() {
        return stopSoundsKeyCode;
    }
}
