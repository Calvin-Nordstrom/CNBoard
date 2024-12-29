package com.calvinnordstrom.cnboard.board.view;

import com.calvinnordstrom.cnboard.board.Sound;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.jnativehook.keyboard.NativeKeyEvent;

import static com.calvinnordstrom.cnboard.util.Utils.getImage;

public class SoundNode extends VBox {
    private final Sound sound;

    public SoundNode(Sound sound) {
        this.sound = sound;

        init();
    }

    private void init() {
        Image icon = getImage(sound.getIconFile());
        ImageView iconView = new ImageView(icon);
        iconView.setFitWidth(80);
        iconView.setFitHeight(80);
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

        getChildren().addAll(iconView, titleLabel, keyCodeLabel);

        getStyleClass().add("sound-node");
    }
}
