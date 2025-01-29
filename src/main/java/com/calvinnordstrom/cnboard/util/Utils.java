package com.calvinnordstrom.cnboard.util;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

import java.io.File;

public class Utils {
    private Utils() {}

    public static Image getImage(File file) {
        if (file != null && file.exists()) {
            return new Image(file.getAbsolutePath());
        } else {
            return new Image(Resources.DEFAULT_ICON_FILE.getAbsolutePath());
        }
    }

    public static Pane createHorizontalDivider() {
        Pane divider = new Pane();
        divider.getStyleClass().add("horizontal-divider");
        return divider;
    }

    public static Pane createVerticalDivider() {
        Pane divider = new Pane();
        divider.getStyleClass().add("vertical-divider");
        return divider;
    }
}
