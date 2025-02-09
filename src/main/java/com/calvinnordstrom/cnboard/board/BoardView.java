package com.calvinnordstrom.cnboard.board;

import com.calvinnordstrom.cnboard.util.LocalAudioPlayer;
import com.calvinnordstrom.cnboard.util.Resources;
import com.calvinnordstrom.cnboard.view.*;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardView {
    private final BoardModel model;
    private final BoardController controller;
    private final BorderPane view = new BorderPane();
    private final FlowPane soundsPane = new FlowPane();
    private final LocalAudioPlayer localAudioPlayer = new LocalAudioPlayer();
    private String searchText;
    private final Map<Sound, SoundNode> soundNodes = new HashMap<>();
    private final Map<Sound, SoundControl> soundControls = new HashMap<>();
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

        TextField searchBar = new TextField();
        searchBar.setPromptText("Search sounds");
        searchBar.textProperty().addListener((_, _, newValue) -> {
            searchText = newValue;
            renderSounds(false);
        });

        Button newButton = new Button("New Sound");

        HBox right = new HBox(searchBar, newButton);
        boardTop.setRight(right);

        view.setTop(boardTop);

        boardTop.getStyleClass().add("board-top");
        searchBar.getStyleClass().add("board-top_search-bar");
        newButton.getStyleClass().add("board-top_new-button");
        right.getStyleClass().add("board-top_right");
    }

    private void initLeft() {

    }

    private void initCenter() {
        populateSoundNodes();
        populateSoundControls();
        renderSounds(true);
        model.getSounds().addListener((ListChangeListener<Sound>) _ -> {
            populateSoundNodes();
            populateSoundControls();
            renderSounds(true);
        });

        ScrollPane scrollPane = createScrollPane(soundsPane);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        view.setCenter(scrollPane);

        soundsPane.getStyleClass().add("sounds-pane");
        scrollPane.getStyleClass().add("sounds-pane_scroll-pane");
    }

    private void initRight() {

    }

    private void initBottom() {
        Settings settings = model.getSettings();
        ToggleControl hearSoundsControl = new ToggleControl("Hear sounds", settings.hearSoundsProperty());
        KeybindControl stopSoundsControl = new KeybindControl("Stop all sounds", settings.stopSoundsKeyCodeProperty());
        GlobalScreen.addNativeKeyListener(stopSoundsControl);

        VBox pane1 = new VBox(hearSoundsControl.asNode());
        VBox pane2 = new VBox(stopSoundsControl.asNode());
        HBox boardControlPane = new HBox(pane1, createVerticalDivider(), pane2);
        view.setBottom(boardControlPane);

        pane1.getStyleClass().add("board-control");
        pane2.getStyleClass().add("board-control");
        boardControlPane.getStyleClass().add("board-control-pane");
    }

    private void renderSounds(boolean selectFirst) {
        soundsPane.getChildren().clear();

        if (model.getSounds().isEmpty()) {
            view.setLeft(null);
            return;
        }

        List<Sound> sounds = getSoundsByTitle(searchText);

        if (sounds.isEmpty()) {
            setSelectedSound(model.getSounds().getFirst());
            return;
        }

        for (Sound sound : sounds) {
            SoundNode soundNode = soundNodes.get(sound);
            if (soundNode != null) {
                if (selectFirst && sounds.getFirst().equals(sound)) {
                    setSelectedSound(sound);
                }
                soundNode.asNode().setOnMouseClicked(_ -> {
                    setSelectedSound(sound);
                });
                soundsPane.getChildren().add(soundNode.asNode());
            }
        }
    }

    private void setSelectedSound(Sound sound) {
        SoundNode soundNode = soundNodes.get(sound);

        if (soundNode != null) {
            if (soundNode.equals(selectedSound)) {
                return;
            }

            localAudioPlayer.stop();

            SoundControl soundControl = soundControls.get(sound);
            Region content = (Region) soundControl.asNode();
            ScrollPane scrollPane = createScrollPane(content);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
            view.setLeft(scrollPane);

            if (selectedSound != null) {
                selectedSound.asNode().getStyleClass().remove("selected-sound");
            }
            selectedSound = soundNode;
            soundNode.asNode().getStyleClass().add("selected-sound");

            scrollPane.getStyleClass().add("sound-control_scroll-pane");
        }
    }

    private List<Sound> getSoundsByTitle(String title) {
        List<Sound> sounds = new ArrayList<>();
        if (title == null || title.isEmpty()) {
            return model.getSounds();
        }
        for (Sound sound : model.getSounds()) {
            String soundTitle = sound.getTitle().toLowerCase();
            String argTitle = title.toLowerCase();
            if (soundTitle.contains(argTitle)) {
                sounds.add(sound);
            }
        }
        return sounds;
    }

    private void populateSoundNodes() {
        if (!soundNodes.isEmpty()) {
            soundNodes.clear();
        }
        for (Sound sound : model.getSounds()) {
            soundNodes.put(sound, new SoundNode(sound));
        }
    }

    private void populateSoundControls() {
        if (!soundControls.isEmpty()) {
            soundControls.clear();
        }
        for (Sound sound : model.getSounds()) {
            soundControls.put(sound, new SoundControl(sound));
        }
    }

    public Node asNode() {
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

        public Node asNode() {
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
            GlobalScreen.addNativeKeyListener(keybindControl);

            ToggleControl enabledControl = new ToggleControl("Enabled", sound.enabledProperty());

            FileChooser.ExtensionFilter soundFilter = new FileChooser.ExtensionFilter("WAV Files", "*.wav");
            FileControl soundFileControl = new FileControl("Sound file", sound.soundFileProperty(), soundFilter);

            FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.bmp");
            FileControl iconFileControl = new FileControl("Icon file", sound.iconFileProperty(), imageFilter);

            SliderControl volumeControl = new SliderControl("Volume", 0, 100, sound.volumeProperty(), "%");

            Button startButton = new Button("Start");
            startButton.setOnMouseClicked(_ -> {
                float volume = (float) sound.getVolume() / 100;
                localAudioPlayer.start(sound.getSoundFile(), volume);
            });
            Button stopButton = new Button("Stop");
            stopButton.setOnMouseClicked(_ -> localAudioPlayer.stop());
            HBox playbackControl = new HBox(startButton, stopButton);

            Button deleteButton = new Button("Delete");
            deleteButton.setOnMouseClicked(_ -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete \"" + sound.getTitle() + "\"?");
                alert.showAndWait()
                        .filter(response -> response == ButtonType.OK)
                        .ifPresent(_ -> controller.onRemoveSound(sound));
            });
            HBox deleteControl = new HBox(deleteButton);

            view.getChildren().addAll(title, iconView, titleControl.asNode(), keybindControl.asNode(), enabledControl.asNode(), soundFileControl.asNode(), iconFileControl.asNode(), volumeControl.asNode(), playbackControl, createHorizontalDivider(), deleteControl);

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

        public Node asNode() {
            return view;
        }
    }

    private static Image getImage(File file) {
        if (file != null && file.exists()) {
            return new Image(file.getAbsolutePath());
        } else {
            return new Image(Resources.DEFAULT_ICON_FILE.getAbsolutePath());
        }
    }

    private static Pane createHorizontalDivider() {
        Pane divider = new Pane();
        divider.getStyleClass().add("horizontal-divider");
        return divider;
    }

    private static Pane createVerticalDivider() {
        Pane divider = new Pane();
        divider.getStyleClass().add("vertical-divider");
        return divider;
    }

    private static ScrollPane createScrollPane(Node content) {
        ScrollPane sp = new ScrollPane(content);
        sp.setFitToWidth(true);
        sp.setFitToHeight(true);
        return sp;
    }
}
