package com.calvinnordstrom.cnboard.util;

import com.calvinnordstrom.cnboard.Main;
import javafx.scene.image.Image;

import java.io.File;
import java.net.URL;
import java.util.Objects;

public final class Resources {
    private static final String DEFAULT_PATH = "src/main/resources/com/calvinnordstrom/cnboard/";

    public static final String STYLES_PATH = getPath("css/styles.css");

    public static final File DEFAULT_ICON_FILE = new File(DEFAULT_PATH + "icons/default.png");

    public static final File BRUH_ICON_FILE = new File(DEFAULT_PATH + "icons/bruh.png");
    public static final File BRUH_SOUND_FILE = new File(DEFAULT_PATH + "sounds/bruh.wav");

    public static final File TACO_BELL_ICON_FILE = new File(DEFAULT_PATH + "icons/taco_bell.png");
    public static final File TACO_BELL_SOUND_FILE = new File(DEFAULT_PATH + "sounds/taco_bell.wav");

    private Resources() {}

    public static Image getImage(File file) {
        if (file != null && file.exists()) {
            return new Image(file.getAbsolutePath());
        } else {
            return new Image(DEFAULT_ICON_FILE.getAbsolutePath());
        }
    }

    private static URL getUrl(String localPath) {
        return Objects.requireNonNull(Main.class.getResource(localPath));
    }

    private static String getPath(String localPath) {
        return getUrl(localPath).toExternalForm();
    }
}
