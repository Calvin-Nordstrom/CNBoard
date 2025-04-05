package com.calvinnordstrom.cnboard.view;

import com.calvinnordstrom.cnboard.controller.BoardController;
import com.calvinnordstrom.cnboard.model.BoardModel;
import com.calvinnordstrom.cnboard.model.Sound;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.jnativehook.keyboard.NativeKeyEvent;

import static com.calvinnordstrom.cnboard.util.Resources.getImage;

public class SoundNode {
    private final BoardModel model;
    private final BoardController controller;
    private final Sound sound;
    private final VBox view = new VBox();

    public SoundNode(BoardModel model, BoardController controller, Sound sound) {
        this.model = model;
        this.controller = controller;
        this.sound = sound;

        init();
    }

    private void init() {
        Image icon = getImage(sound.getIconFile());
        ImageView iconView = new ImageView(icon);
        iconView.setFitWidth(80);
        iconView.setFitHeight(80);
        iconView.setSmooth(true);
        sound.iconFileProperty().addListener((_, _, newValue) -> {
            Image image = getImage(newValue);
            iconView.setImage(image);
        });

        Label titleLabel = new Label(sound.getTitle());
        titleLabel.textProperty().bind(sound.titleProperty());

        String keyCode = NativeKeyEvent.getKeyText(sound.getKeyCode());
        Label keyCodeLabel = new Label(keyCode);
        sound.keyCodeProperty().addListener((_, _, newValue) -> {
            keyCodeLabel.setText(NativeKeyEvent.getKeyText((int) newValue));
        });

        view.getChildren().addAll(iconView, titleLabel, keyCodeLabel);

        view.getStyleClass().add("sound-node");
        iconView.getStyleClass().add("sound-node_icon-view");
        titleLabel.getStyleClass().addAll("title", "sound-node_title");
    }

    public Node asNode() {
        return view;
    }
}
