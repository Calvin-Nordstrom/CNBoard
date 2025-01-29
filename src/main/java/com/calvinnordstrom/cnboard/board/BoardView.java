package com.calvinnordstrom.cnboard.board;

import com.calvinnordstrom.cnboard.util.LocalAudioPlayer;
import com.calvinnordstrom.cnboard.view.*;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.calvinnordstrom.cnboard.util.Utils.*;

public class BoardView {
    private final BoardModel model;
    private final BoardController controller;
    private final BorderPane view = new BorderPane();
    private final FlowPane soundsPane = new FlowPane();
    private final Button newButton = new Button("New Sound");
    private SoundNode selectedSound;

    public BoardView(BoardModel model, BoardController controller) {
        this.model = model;
        this.controller = controller;

        init();
        initTop();
        initLeft();
        initCenter();
        initRight();
        initBottom();
    }

    private void init() {
        view.setPrefWidth(1280);
    }

    private void initTop() {
        BorderPane boardTop = new BorderPane();

        boardTop.setRight(newButton);

        view.setTop(boardTop);

        newButton.getStyleClass().add("board-top_new-button");
        boardTop.getStyleClass().add("board-top");
    }

    private void initLeft() {

    }

    private void initCenter() {
        renderSounds();
        model.getSounds().addListener((ListChangeListener<Sound>) _ -> {
            renderSounds();
        });

        ScrollPane scrollPane = new ScrollPane(soundsPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        view.setCenter(scrollPane);

        soundsPane.prefHeightProperty().bind(scrollPane.heightProperty().subtract(2));

        soundsPane.getStyleClass().add("sounds-pane");
        scrollPane.getStyleClass().add("sounds-scrollpane");
    }

    private void initRight() {

    }

    private void initBottom() {
        ToggleControl hearMyselfControl = new ToggleControl("Hear myself", new SimpleBooleanProperty());
        ToggleControl hearSoundsControl = new ToggleControl("Hear sounds", new SimpleBooleanProperty());
        KeybindControl stopSoundsControl = new KeybindControl("Stop all sounds", new SimpleIntegerProperty());

        VBox pane1 = new VBox(hearMyselfControl);
        VBox pane2 = new VBox(hearSoundsControl);
        VBox pane3 = new VBox(stopSoundsControl);
        HBox boardControlPane = new HBox(pane1, createVerticalDivider(), pane2, createVerticalDivider(), pane3, createVerticalDivider());
        view.setBottom(boardControlPane);

        pane1.getStyleClass().add("board-control");
        pane2.getStyleClass().add("board-control");
        pane3.getStyleClass().add("board-control");
        boardControlPane.getStyleClass().add("board-control-pane");
    }

    private void renderSounds() {
        soundsPane.getChildren().clear();
        ObservableList<Sound> sounds = model.getSounds();
        if (sounds.isEmpty()) view.setLeft(null);
        for (Sound sound : sounds) {
            SoundNode soundNode = new SoundNode(sound);
            if (sounds.getFirst().equals(sound)) {
                setSelectedSound(soundNode);
            }
            soundNode.asParent().addEventFilter(MouseEvent.MOUSE_CLICKED, _ -> {
                setSelectedSound(soundNode);
            });
            soundsPane.getChildren().add(soundNode.asParent());
        }
    }

    private void setSelectedSound(SoundNode soundNode) {
        LocalAudioPlayer.getInstance().stop();
        SoundControl soundControl = new SoundControl(soundNode.getSound());
        view.setLeft(soundControl.asParent());

        if (selectedSound != null) {
            selectedSound.asParent().getStyleClass().remove("selected-sound");
        }
        selectedSound = soundNode;
        soundNode.asParent().getStyleClass().add("selected-sound");
    }

    public Parent asParent() {
        return view;
    }

    private static class SoundNode {
        private final Sound sound;
        private final VBox view = new VBox();

        public SoundNode(Sound sound) {
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
            titleLabel.getStyleClass().addAll("text", "title", "sound-node_title");
            keyCodeLabel.getStyleClass().add("text");
        }

        public Sound getSound() {
            return sound;
        }

        public Parent asParent() {
            return view;
        }
    }

    private class SoundControl {
        private final Sound sound;
        private final VBox view = new VBox();

        public SoundControl(Sound sound) {
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
            Logger.getLogger(GlobalScreen.class.getPackageName()).setLevel(Level.OFF);
            GlobalScreen.addNativeKeyListener(keybindControl);

            ToggleControl enabledControl = new ToggleControl("Enabled", sound.enabledProperty());

            FileChooser.ExtensionFilter soundFilter = new FileChooser.ExtensionFilter("WAV Files", "*.wav");
            FileControl soundFileControl = new FileControl("Sound file", sound.soundFileProperty(), soundFilter);

            FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.bmp");
            FileControl iconFileControl = new FileControl("Icon file", sound.iconFileProperty(), imageFilter);

            SliderControl volumeControl = new SliderControl("Volume", 0, 100, sound.volumeProperty(), "%");

            LocalAudioPlayer player = LocalAudioPlayer.getInstance();
            Button startButton = new Button("Start");
            startButton.setOnMouseClicked(_ -> player.start(sound.getSoundFile()));
            Button stopButton = new Button("Stop");
            stopButton.setOnMouseClicked(_ -> player.stop());
            HBox playbackControl = new HBox(startButton, stopButton);

            Button deleteButton = new Button("Delete");
            deleteButton.setOnMouseClicked(_ -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete \"" + sound.getTitle() + "\"?");
                alert.showAndWait()
                        .filter(response -> response == ButtonType.OK)
                        .ifPresent(_ -> controller.onRemoveSound(sound));
            });
            HBox deleteControl = new HBox(deleteButton);

            view.getChildren().addAll(title, iconView, titleControl, keybindControl, enabledControl, soundFileControl, iconFileControl, createHorizontalDivider(), volumeControl, playbackControl, createHorizontalDivider(), deleteControl);

            view.getStyleClass().add("sound-control");
            title.getStyleClass().addAll("text", "sound-control_title");
            iconView.getStyleClass().add("sound-control_icon-view");
            playbackControl.getStyleClass().add("sound-control_playback-control");
            deleteButton.getStyleClass().add("sound-control_delete-button");
            deleteControl.getStyleClass().add("sound-control_delete-control");
        }

        public Sound getSound() {
            return sound;
        }

        public Parent asParent() {
            return view;
        }
    }
}
