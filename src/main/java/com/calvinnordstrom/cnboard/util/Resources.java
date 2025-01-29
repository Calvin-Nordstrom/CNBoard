package com.calvinnordstrom.cnboard.util;

import java.io.File;

public final class Resources {
    private static final String DEFAULT_PATH = "src/main/resources/com/calvinnordstrom/cnboard/static/";
    public static final File DEFAULT_ICON_FILE = new File(DEFAULT_PATH + "icons/default.png");

    public static final File BRUH_ICON_FILE = new File(DEFAULT_PATH + "icons/bruh.png");
    public static final File BRUH_SOUND_FILE = new File(DEFAULT_PATH + "sounds/bruh.wav");

    public static final File TACO_BELL_ICON_FILE = new File(DEFAULT_PATH + "icons/taco_bell.png");
    public static final File TACO_BELL_SOUND_FILE = new File(DEFAULT_PATH + "sounds/taco_bell.wav");

    private Resources() {}
}
