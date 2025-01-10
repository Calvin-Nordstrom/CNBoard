package com.calvinnordstrom.cnboard.board.view;

import com.calvinnordstrom.cnboard.board.Sound;
import com.calvinnordstrom.cnboard.util.LocalAudioPlayer;
import com.calvinnordstrom.cnboard.view.KeybindControl;
import com.calvinnordstrom.cnboard.view.ToggleControl;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

public class BoardView extends BorderPane {
    private final ObservableList<Sound> sounds;
    private SoundNode selectedSound;

    public BoardView(ObservableList<Sound> sounds) {
        this.sounds = sounds;

        init();
        initTop();
        initLeft();
        initCenter();
        initRight();
        initBottom();
    }

    private void init() {
        setPrefWidth(1280);
    }

    private void initTop() {

    }

    private void initLeft() {

    }

    private void initCenter() {
        FlowPane soundsPane = new FlowPane();

        for (Sound sound : sounds) {
            SoundNode soundNode = new SoundNode(sound);
            if (sounds.getFirst().equals(sound)) {
                handleSoundSelection(soundNode);
            }
            soundNode.addEventFilter(MouseEvent.MOUSE_CLICKED, _ -> {
                handleSoundSelection(soundNode);
            });
            soundsPane.getChildren().add(soundNode);
        }

        ScrollPane scrollPane = new ScrollPane(soundsPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        setCenter(scrollPane);

        soundsPane.prefHeightProperty().bind(scrollPane.heightProperty().subtract(2));

        soundsPane.getStyleClass().add("sounds-pane");
        scrollPane.getStyleClass().add("sounds-scrollpane");
    }

    private void initRight() {

    }

    private void initBottom() {
        ToggleControl hearSoundsControl = new ToggleControl("Hear sounds", new SimpleBooleanProperty());
        KeybindControl stopSoundsControl = new KeybindControl("Stop all sounds", new SimpleIntegerProperty());

        VBox pane1 = new VBox(hearSoundsControl);
        Pane divider1 = new Pane();
        VBox pane2 = new VBox(stopSoundsControl);
        HBox boardControlPane = new HBox(pane1, divider1, pane2);
        setBottom(boardControlPane);

        pane1.getStyleClass().add("board-control");
        divider1.getStyleClass().add("vertical-divider");
        pane2.getStyleClass().add("board-control");
        boardControlPane.getStyleClass().add("board-control-pane");
    }

    private void handleSoundSelection(SoundNode soundNode) {
        LocalAudioPlayer.getInstance().stop();
        SoundControl soundControl = new SoundControl(soundNode.getSound());
        setLeft(soundControl);

        if (selectedSound != null) {
            selectedSound.getStyleClass().remove("selected");
        }
        selectedSound = soundNode;
        soundNode.getStyleClass().add("selected");
    }
}
