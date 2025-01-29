package com.calvinnordstrom.cnboard.util;

import com.calvinnordstrom.cnboard.board.Sound;
import org.jnativehook.keyboard.NativeKeyEvent;

import java.io.File;

public final class Resources {
    private static final String DEFAULT_PATH = "src/main/resources/com/calvinnordstrom/cnboard/static/";
    public static final File DEFAULT_ICON_FILE = new File(DEFAULT_PATH + "icons/default.png");

    public static final File BRUH_ICON_FILE = new File(DEFAULT_PATH + "icons/bruh.png");
    public static final File BRUH_SOUND_FILE = new File(DEFAULT_PATH + "sounds/bruh.wav");

    public static final File TACO_BELL_ICON_FILE = new File(DEFAULT_PATH + "icons/taco_bell.png");
    public static final File TACO_BELL_SOUND_FILE = new File(DEFAULT_PATH + "sounds/taco_bell.wav");

    private Resources() {}

    public static Sound createBruhSound() {
        return new Sound.Builder()
                .title("Bruh")
                .iconFile(BRUH_ICON_FILE)
                .soundFile(BRUH_SOUND_FILE)
                .keyCode(NativeKeyEvent.VC_1)
                .volume(100)
                .enabled(true)
                .build();
    }

    public static Sound createTacoBellSound() {
        return new Sound.Builder()
                .title("Taco Bell")
                .iconFile(TACO_BELL_ICON_FILE)
                .soundFile(TACO_BELL_SOUND_FILE)
                .keyCode(NativeKeyEvent.VC_2)
                .volume(100)
                .enabled(true)
                .build();
    }
}
