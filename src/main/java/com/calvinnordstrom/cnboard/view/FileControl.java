package com.calvinnordstrom.cnboard.view;

import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;

public class FileControl {
    private final ObjectProperty<File> value;
    private final FileChooser.ExtensionFilter filter;
    private final VBox view = new VBox();
    private final Label label;
    private final Button fileButton = new Button("Choose file");
    private final Label fileName;
    private final String initialName;

    public FileControl(String text, ObjectProperty<File> value,
                       FileChooser.ExtensionFilter filter) {
        this.value = value;
        this.filter = filter;
        label = new Label(text);
        initialName = value.get() == null ? "No file selected" : value.get().getName();
        fileName = new Label(initialName);

        init();
        initEventHandlers();
    }

    private void init() {
        HBox labelPane = new HBox(label);
        fileName.setTooltip(new Tooltip(initialName));
        HBox filePane = new HBox(fileButton, fileName);
        view.getChildren().addAll(labelPane, filePane);

        label.getStyleClass().add("title");
        labelPane.getStyleClass().add("control_label-pane");
        fileButton.getStyleClass().add("button-width-100");
        filePane.getStyleClass().add("file-control_file-pane");
    }

    private void initEventHandlers() {
        fileButton.setOnMouseClicked(_ -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open");
            fileChooser.getExtensionFilters().add(filter);
            Window owner = view.getScene().getWindow();
            File selectedFile = fileChooser.showOpenDialog(owner);

            if (selectedFile != null) {
                value.set(selectedFile);
                String selectedFileName = selectedFile.getName();
                fileName.setText(selectedFileName);
                fileName.setTooltip(new Tooltip(selectedFileName));
            }
        });
    }

    public Node asNode() {
        return view;
    }
}
