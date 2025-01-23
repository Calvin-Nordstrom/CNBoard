package com.calvinnordstrom.cnboard.board;

import com.calvinnordstrom.cnboard.util.Resources;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jnativehook.GlobalScreen;

import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BoardManager {
    private final ObservableList<Sound> sounds = FXCollections.observableArrayList();

    public BoardManager() {
        TargetDataLine inputLine = AudioUtils.getDefaultTarget();
        SourceDataLine outputLine = AudioUtils.getSourceByName("CABLE Input (VB-Audio Virtual Cable)");
//        SourceDataLine playbackLine = AudioUtils.getDefaultSource();
//        SourceDataLine playbackLine = AudioUtils.getSourceByName("Speakers (Logitech PRO X Gaming Headset)");
        AudioRouter router = new AudioRouter(inputLine, outputLine);
        router.start();

        Logger.getLogger(GlobalScreen.class.getPackageName()).setLevel(Level.OFF);
        GlobalScreen.addNativeKeyListener(new KeyListener(sounds, router));

        File iconFile = new File("src/main/resources/com/calvinnordstrom/cnboard/board/icons/bruh.png");
        File soundFile = new File("src/main/resources/com/calvinnordstrom/cnboard/board/sounds/bruh.wav");
        for (int i = 0; i < 7; i++) {
            sounds.add(new Sound.Builder()
                    .title("Bruh " + i)
                    .iconFile(Resources.DEFAULT_ICON_FILE)
                    .soundFile(soundFile)
                    .keyCode(i + 2)
                    .volume(50)
                    .enabled(true)
                    .build());
        }
        sounds.add(new Sound.Builder()
                .title("Bruh")
                .iconFile(Resources.DEFAULT_ICON_FILE)
                .soundFile(soundFile)
                .keyCode(50)
                .volume(50)
                .enabled(true)
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
