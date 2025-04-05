package com.calvinnordstrom.cnboard.view;

import com.calvinnordstrom.cnboard.controller.BoardController;
import com.calvinnordstrom.cnboard.model.BoardModel;
import com.calvinnordstrom.cnboard.model.Sound;
import com.calvinnordstrom.cnboard.view.control.*;
import com.calvinnordstrom.cnboard.view.node.Divider;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.jnativehook.GlobalScreen;

import static com.calvinnordstrom.cnboard.util.Resources.getImage;

public class SoundControl {
    private final BoardModel model;
    private final BoardController controller;
    private final Sound sound;
    private final VBox view = new VBox();

    public SoundControl(BoardModel model, BoardController controller, Sound sound) {
        this.model = model;
        this.controller = controller;
        this.sound = sound;

        init();
    }

    private void init() {
        Label title = new Label("Sound Editor");

        Image icon = getImage(sound.getIconFile());
        ImageView iconView = new ImageView(icon);
        iconView.setFitWidth(160);
        iconView.setFitHeight(160);
        iconView.setSmooth(true);
        sound.iconFileProperty().addListener((_, _, newValue) -> {
            Image image = getImage(newValue);
            iconView.setImage(image);
        });

        StringControl titleControl = new StringControl("Title", sound.titleProperty());

        KeybindControl keybindControl = new KeybindControl("Keybind", sound.keyCodeProperty());
        GlobalScreen.addNativeKeyListener(keybindControl);

        ToggleControl enabledControl = new ToggleControl("Enabled", sound.enabledProperty());

        FileChooser.ExtensionFilter soundFilter = new FileChooser.ExtensionFilter("WAV Files", "*.wav");
        FileControl soundFileControl = new FileControl("Sound file", sound.soundFileProperty(), soundFilter);

        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.bmp");
        FileControl iconFileControl = new FileControl("Icon file", sound.iconFileProperty(), imageFilter);

        SliderControl volumeControl = new SliderControl("Volume", 0, 100, sound.volumeProperty(), "%");

        Button startButton = new Button("Start");
        startButton.setOnMouseClicked(_ -> {
            controller.startLocalAudio(sound);
        });
        Button stopButton = new Button("Stop");
        stopButton.setOnMouseClicked(_ -> controller.stopLocalAudio());
        HBox playbackControl = new HBox(startButton, stopButton);

        Button deleteButton = new Button("Delete");
        deleteButton.setOnMouseClicked(_ -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete \"" + sound.getTitle() + "\"?");
            alert.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(_ -> controller.removeSound(sound));
        });
        HBox deleteControl = new HBox(deleteButton);

        view.getChildren().addAll(title, iconView, titleControl.asNode(), keybindControl.asNode(), enabledControl.asNode(), soundFileControl.asNode(), iconFileControl.asNode(), volumeControl.asNode(), playbackControl, Divider.horizontal(), deleteControl);

        view.getStyleClass().add("sound-control");
        title.getStyleClass().add("sound-control_title");
        iconView.getStyleClass().add("sound-control_icon-view");
        playbackControl.getStyleClass().add("sound-control_playback-control");
        deleteButton.getStyleClass().add("sound-control_delete-button");
        deleteControl.getStyleClass().add("sound-control_delete-control");
    }

    public Node asNode() {
        return view;
    }
}
