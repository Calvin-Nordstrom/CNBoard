package com.calvinnordstrom.cnboard.util;

import javafx.scene.image.Image;

import java.io.File;

public class Utils {
    private Utils() {}

    public static Image getImage(File file) {
        if (file.exists()) {
            return new Image(file.getAbsolutePath());
        } else {
            return new Image("src/main/resources/com/calvinnordstrom/cnboard/icons/bruh1.png");
        }
    }
}
