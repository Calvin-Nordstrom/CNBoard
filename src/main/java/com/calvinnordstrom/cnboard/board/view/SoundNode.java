package com.calvinnordstrom.cnboard.board.view;

import com.calvinnordstrom.cnboard.board.Sound;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.jnativehook.keyboard.NativeKeyEvent;

public class SoundPane extends VBox {
    private final Sound sound;

    public SoundPane(Sound sound) {
        this.sound = sound;

        init();
    }

    private void init() {
        setAlignment(Pos.CENTER);

        Label titleLabel = new Label(sound.getTitle());
        getChildren().add(titleLabel);

        String keyCode = NativeKeyEvent.getKeyText(sound.getKeyCode());
        Label keyCodeLabel = new Label(keyCode);
        getChildren().add(keyCodeLabel);
    }
}
