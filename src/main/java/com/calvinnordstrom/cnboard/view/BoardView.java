package com.calvinnordstrom.cnboard.view;

import com.calvinnordstrom.cnboard.controller.BoardController;
import com.calvinnordstrom.cnboard.model.BoardModel;
import com.calvinnordstrom.cnboard.model.Settings;
import com.calvinnordstrom.cnboard.model.Sound;
import com.calvinnordstrom.cnboard.view.control.KeybindControl;
import com.calvinnordstrom.cnboard.view.control.ToggleControl;
import com.calvinnordstrom.cnboard.view.node.Divider;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import org.jnativehook.GlobalScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardView {
    private final BoardModel model;
    private final BoardController controller;
    private final BorderPane view = new BorderPane();
    private final FlowPane soundsPane = new FlowPane();
    private final Map<Sound, SoundNode> soundNodes = new HashMap<>();
    private final Map<Sound, SoundControl> soundControls = new HashMap<>();
    private String searchText;
    private SoundNode selectedSound;

    public BoardView(BoardModel model, BoardController controller) {
        this.model = model;
        this.controller = controller;

        init();
        initTop();
        initCenter();
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
        newButton.setOnMouseClicked(_ -> {
            SoundCreator soundCreator = new SoundCreator(model, controller, newButton.getScene().getWindow());
            soundCreator.show();
        });

        HBox right = new HBox(searchBar, newButton);
        boardTop.setRight(right);

        view.setTop(boardTop);

        boardTop.getStyleClass().add("board-top");
        searchBar.getStyleClass().add("board-top_search-bar");
        newButton.getStyleClass().add("board-top_new-button");
        right.getStyleClass().add("board-top_right");
    }

    private void initCenter() {
        populateSoundNodes();
        populateSoundControls();
        renderSounds(true);
        model.getSounds().addListener((ListChangeListener<Sound>) c -> {
            populateSoundNodes();
            populateSoundControls();

            while (c.next()) {
                if (c.wasRemoved()) {
                    renderSounds(true);
                } else if (c.wasAdded()) {
                    renderSounds(false);
                    setSelectedSound(model.getSounds().getLast());
                }
            }
        });

        ScrollPane scrollPane = createScrollPane(soundsPane);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        view.setCenter(scrollPane);

        soundsPane.getStyleClass().add("sounds-pane");
        scrollPane.getStyleClass().add("sounds-pane_scroll-pane");
    }

    private void initBottom() {
        Settings settings = model.getSettings();
        ToggleControl hearSoundsControl = new ToggleControl("Hear sounds", settings.hearSoundsProperty());
        KeybindControl stopSoundsControl = new KeybindControl("Stop all sounds", settings.stopSoundsKeyCodeProperty());
        GlobalScreen.addNativeKeyListener(stopSoundsControl);

        Node node1 = new HBox(hearSoundsControl.asNode());
        Node node2 = new HBox(stopSoundsControl.asNode());
        HBox boardControlPane = new HBox(node1, Divider.vertical(), node2);
        view.setBottom(boardControlPane);

        node1.getStyleClass().add("board-control");
        node2.getStyleClass().add("board-control");
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

            controller.stopLocalAudio();

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
            soundNodes.put(sound, new SoundNode(model, controller, sound));
        }
    }

    private void populateSoundControls() {
        if (!soundControls.isEmpty()) {
            soundControls.clear();
        }
        for (Sound sound : model.getSounds()) {
            soundControls.put(sound, new SoundControl(model, controller, sound));
        }
    }

    public Node asNode() {
        return view;
    }

    private static ScrollPane createScrollPane(Node content) {
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        return scrollPane;
    }
}
