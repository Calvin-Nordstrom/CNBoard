package com.calvinnordstrom.cnboard.board;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jnativehook.GlobalScreen;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BoardManager {
    private final ObservableList<Sound> sounds = FXCollections.observableArrayList();

    public BoardManager() {
        Logger.getLogger(GlobalScreen.class.getPackageName()).setLevel(Level.OFF);
        GlobalScreen.addNativeKeyListener(new SoundKeyListener(sounds));

        File iconFile = new File("src/main/resources/com/calvinnordstrom/cnboard/board/icons/bruh.png");
        File soundFile = new File("src/main/resources/com/calvinnordstrom/cnboard/board/sounds/bruh.wav");
        for (int i = 0; i < 7; i++) {
            sounds.add(new Sound.Builder()
                    .title("Bruh")
                    .iconFile(iconFile)
                    .soundFile(soundFile)
                    .keyCode(i + 2)
                    .volume(50)
                    .enabled(true)
                    .build());
        }
        sounds.add(new Sound.Builder()
                .title("Bruh")
                .iconFile(iconFile)
                .soundFile(soundFile)
                .keyCode(50)
                .volume(50)
                .enabled(false)
                .build());
    }

    public void addSound(Sound sound) {
        sounds.add(sound);
    }

    public void removeSound(Sound sound) {
        sounds.remove(sound);
    }

    public ObservableList<Sound> getSounds() {
        return sounds;
    }
}
