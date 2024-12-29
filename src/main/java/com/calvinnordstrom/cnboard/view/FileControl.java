package com.calvinnordstrom.cnboard.view;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;

public class FileChooserControl extends VBox {
    private final ObjectProperty<File> value;
    private final Label label;
    private final Label fileName;

    public FileChooserControl(String text, ObjectProperty<File> boundValue) {
        File val = boundValue.get();
        value = new SimpleObjectProperty<>(val);
        label = new Label(text);
        fileName = new Label(val.getName());

        boundValue.bindBidirectional(value);

        getStyleClass().add("file-chooser-control");

        init();
        initEventHandlers();
    }

    private void init() {
        HBox labelPane = new HBox(label);
        HBox textFieldPane = new HBox(fileName);
        getChildren().addAll(labelPane, textFieldPane);

        labelPane.getStyleClass().add("control_label-pane");
    }

    private void initEventHandlers() {

    }
}
