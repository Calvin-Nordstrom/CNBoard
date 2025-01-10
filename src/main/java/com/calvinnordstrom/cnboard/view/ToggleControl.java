package com.calvinnordstrom.cnboard.view;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;

public class ToggleControl extends HBox {
    private final BooleanProperty value;
    private final CheckBox checkBox;

    public ToggleControl(String text, BooleanProperty boundValue) {
        boolean val = boundValue.get();
        value = new SimpleBooleanProperty(val);
        checkBox = new CheckBox(text);
        checkBox.setSelected(val);

        boundValue.bindBidirectional(value);

        init();
        initEventHandlers();
    }

    private void init() {
        getChildren().add(checkBox);

        checkBox.getStyleClass().addAll("text", "title");
        getStyleClass().add("control_label-pane");
    }

    private void initEventHandlers() {
        checkBox.selectedProperty().bindBidirectional(value);
    }
}
