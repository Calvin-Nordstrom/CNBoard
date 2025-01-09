package com.calvinnordstrom.cnboard.board.view;

import com.calvinnordstrom.cnboard.board.Sound;
import com.calvinnordstrom.cnboard.view.FileControl;
import com.calvinnordstrom.cnboard.view.SliderControl;
import com.calvinnordstrom.cnboard.view.StringControl;
import com.calvinnordstrom.cnboard.view.ToggleControl;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.jnativehook.GlobalScreen;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.calvinnordstrom.cnboard.util.Utils.getImage;

public class SoundControl extends VBox {
    private final Sound sound;
    private Clip clip;

    public SoundControl(Sound sound) {
        this.sound = sound;

        getStyleClass().add("sound-control");

        init();
    }

    private void init() {
        Image icon = getImage(sound.getIconFile());
        ImageView iconView = new ImageView(icon);
        iconView.setFitWidth(128);
        iconView.setFitHeight(128);
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

        Button startButton = new Button("Start");
        startButton.setOnMouseClicked(_ -> startSound());
        Button stopButton = new Button("Stop");
        stopButton.setOnMouseClicked(_ -> stopSound());
        HBox playbackControl = new HBox(startButton, stopButton);

        Button deleteButton = new Button("Delete");
        HBox deleteControl = new HBox(deleteButton);

        getChildren().addAll(iconView, titleControl, keybindControl, enabledControl, soundFileControl, iconFileControl, volumeControl, playbackControl, deleteControl);

        playbackControl.getStyleClass().add("sound-control_playback-control");
        deleteControl.getStyleClass().add("sound-control_delete-control");
    }

    private void startSound() {
        File soundFile = sound.getSoundFile();
        if (soundFile.exists()) {
            try {
                AudioInputStream ais = AudioSystem.getAudioInputStream(soundFile);
                clip = AudioSystem.getClip();
                clip.open(ais);
                clip.start();
            } catch (UnsupportedAudioFileException
                     | IOException
                     | LineUnavailableException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void stopSound() {
        if (clip != null) clip.stop();
    }
}
