package com.calvinnordstrom.cnboard.view;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;

public class FileControl extends VBox {
    private final ObjectProperty<File> value;
    private final FileChooser.ExtensionFilter filter;
    private final Label label;
    private final Button fileButton = new Button("Choose file");
    private final Label fileName;

    public FileControl(String text, ObjectProperty<File> boundValue,
                       FileChooser.ExtensionFilter filter) {
        File val = boundValue.get();
        value = new SimpleObjectProperty<>(val);
        this.filter = filter;
        label = new Label(text);
        fileName = new Label(val.getName());

        boundValue.bindBidirectional(value);

        init();
        initEventHandlers();
    }

    private void init() {
        HBox labelPane = new HBox(label);
        HBox filePane = new HBox(fileButton, fileName);
        getChildren().addAll(labelPane, filePane);

        labelPane.getStyleClass().add("control_label-pane");
        fileButton.getStyleClass().add("button-width-80");
        filePane.getStyleClass().add("file-control_file-pane");
    }

    private void initEventHandlers() {
        fileButton.setOnMouseClicked(_ -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open");
            fileChooser.getExtensionFilters().add(filter);
            Window owner = getScene().getWindow();
            File selectedFile = fileChooser.showOpenDialog(owner);

            if (selectedFile != null) {
                value.set(selectedFile);
                fileName.setText(selectedFile.getName());
            }
        });
    }
}
