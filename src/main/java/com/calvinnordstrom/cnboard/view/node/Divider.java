package com.calvinnordstrom.cnboard.view.node;

import javafx.scene.layout.Pane;

/**
 * A simple custom UI divider that supports two orientations: vertical and
 * horizontal. This class extends {@link Pane} so it may be directly added to
 * the node graph.
 */
public class Divider extends Pane {
    private Divider(Orientation type) {
        switch (type) {
            case VERTICAL -> getStyleClass().add("vertical-divider");
            case HORIZONTAL -> getStyleClass().add("horizontal-divider");
        }
    }

    /**
     * Creates a new vertical divider.
     *
     * @return a {@code Divider} of type {@link Orientation#VERTICAL}
     */
    public static Divider vertical() {
        return new Divider(Orientation.VERTICAL);
    }

    /**
     * Creates a new horizontal divider.
     *
     * @return a {@code Divider} of type {@link Orientation#HORIZONTAL}
     */
    public static Divider horizontal() {
        return new Divider(Orientation.HORIZONTAL);
    }

    /**
     * Enum representing the orientation of the divider.
     */
    public enum Orientation {
        /**
         * Represents a vertical divider.
         */
        VERTICAL,

        /**
         * Represents a horizontal divider.
         */
        HORIZONTAL,
    }
}
