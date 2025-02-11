package com.calvinnordstrom.cnboard.view;

import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class StringControl {
    private final StringProperty value;
    private final VBox view = new VBox();
    private final Label label;
    private final TextField textField;

    public StringControl(String text, StringProperty value) {
        this.value = value;
        label = new Label(text);
        textField = new TextField(value.get());

        init();
        initEventHandlers();
    }

    private void init() {
        HBox labelPane = new HBox(label);
        HBox textFieldPane = new HBox(textField);
        view.getChildren().addAll(labelPane, textFieldPane);

        HBox.setHgrow(textField, Priority.ALWAYS);
        label.getStyleClass().add("title");
        labelPane.getStyleClass().add("control_label-pane");
        textField.getStyleClass().add("text-field");
    }

    private void initEventHandlers() {
        textField.textProperty().bindBidirectional(value);
    }

    public Node asNode() {
        return view;
    }
}
