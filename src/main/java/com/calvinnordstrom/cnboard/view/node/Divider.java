package com.calvinnordstrom.cnboard.view.node;

import javafx.scene.layout.Pane;

public class Divider extends Pane {
    public Divider(Type type) {
        switch (type) {
            case VERTICAL -> getStyleClass().add("vertical-divider");
            case HORIZONTAL -> getStyleClass().add("horizontal-divider");
        }
    }

    public static Divider vertical() {
        return new Divider(Type.VERTICAL);
    }

    public static Divider horizontal() {
        return new Divider(Type.HORIZONTAL);
    }

    public enum Type {
        VERTICAL,
        HORIZONTAL
    }
}
