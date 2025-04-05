package com.calvinnordstrom.cnboard.view;

import com.calvinnordstrom.cnboard.controller.BoardController;
import com.calvinnordstrom.cnboard.model.BoardModel;
import com.calvinnordstrom.cnboard.model.Sound;
import com.calvinnordstrom.cnboard.util.Resources;
import com.calvinnordstrom.cnboard.view.control.FileControl;
import com.calvinnordstrom.cnboard.view.node.Divider;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;

public class SoundCreator {
    private static final String TITLE = "Upload Sound";
    private final BoardModel model;
    private final BoardController controller;
    private final Window owner;
    private final Stage stage = new Stage();
    private final Label errorLabel = new Label();
    private Scene scene;

    public SoundCreator(BoardModel model, BoardController controller, Window owner) {
        this.model = model;
        this.controller = controller;
        this.owner = owner;

        init();
    }

    private void init() {
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle(TITLE);
        stage.setResizable(false);

        ObjectProperty<File> fileProperty = new SimpleObjectProperty<>();
        FileChooser.ExtensionFilter soundFilter = new FileChooser.ExtensionFilter("WAV Files", "*.wav");
        FileControl fileControl = new FileControl("Sound file", fileProperty, soundFilter);
        fileProperty.addListener((_, _, _) -> {
            errorLabel.setVisible(false);
        });

        errorLabel.setVisible(false);

        VBox vBox = new VBox(fileControl.asNode(), errorLabel);

        BorderPane borderPane = new BorderPane();
        Button okButton = getOkButton(fileProperty);
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnMouseClicked(_ -> hide());
        HBox right = new HBox(okButton, cancelButton);
        borderPane.setRight(right);

        VBox content = new VBox(vBox, Divider.horizontal(), borderPane);
        Pane root = new Pane(content);

        scene = new Scene(root);
        scene.getStylesheets().add(Resources.STYLES_PATH);

        errorLabel.getStyleClass().add("sound-creator_error-label");
        vBox.getStyleClass().add("sound-creator_vBox");
        content.getStyleClass().add("sound-creator_content");
        right.getStyleClass().add("sound-creator_right");
        okButton.getStyleClass().add("button-width-100");
        cancelButton.getStyleClass().add("button-width-100");
    }

    private Button getOkButton(ObjectProperty<File> fileProperty) {
        Button button = new Button("OK");
        button.setOnMouseClicked(_ -> {
            Sound.Builder builder = new Sound.Builder();
            builder.soundFile(fileProperty.get());
            try {
                Sound sound = builder.build();
                controller.addSound(sound);
                hide();
            } catch (IllegalArgumentException e) {
                errorLabel.setText(e.getMessage());
                errorLabel.setVisible(true);
            }
        });
        return button;
    }

    public void show() {
        if (scene != null) {
            stage.setScene(scene);
            stage.sizeToScene();
        }
        stage.show();
    }

    public void hide() {
        stage.hide();
        stage.setScene(null);
    }
}
