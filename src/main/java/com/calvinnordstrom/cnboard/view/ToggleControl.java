package com.calvinnordstrom.cnboard.view;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;

public class ToggleControl {
    private final BooleanProperty value;
    private final HBox view = new HBox();
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
        view.getChildren().add(checkBox);

        view.getStyleClass().add("control_label-pane");
        checkBox.getStyleClass().addAll("text", "title");
    }

    private void initEventHandlers() {
        checkBox.selectedProperty().bindBidirectional(value);
    }

    public Node asNode() {
        return view;
    }
}
