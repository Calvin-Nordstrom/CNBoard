package com.calvinnordstrom.cnboard.board.view;

import com.calvinnordstrom.cnboard.board.Sound;
import javafx.collections.ObservableList;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

public class BoardView extends BorderPane {
    private final ObservableList<Sound> sounds;

    public BoardView(ObservableList<Sound> sounds) {
        this.sounds = sounds;

        initTop();
        initLeft();
        initCenter();
        initRight();
        initBottom();
    }

    private void initTop() {

    }

    private void initLeft() {

    }

    private void initCenter() {
        FlowPane soundsPane = new FlowPane();

        for (Sound sound : sounds) {
            SoundNode soundNode = new SoundNode(sound);
            soundNode.addEventFilter(MouseEvent.MOUSE_CLICKED, _ -> {
                SoundControl soundControl = new SoundControl(sound);
                setRight(soundControl);
            });
            soundsPane.getChildren().add(soundNode);
        }

        ScrollPane scrollPane = new ScrollPane(soundsPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        setCenter(scrollPane);

        soundsPane.getStyleClass().add("sounds-pane");
    }

    private void initRight() {

    }

    private void initBottom() {

    }
}
