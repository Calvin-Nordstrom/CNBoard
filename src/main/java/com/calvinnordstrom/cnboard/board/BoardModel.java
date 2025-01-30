package com.calvinnordstrom.cnboard.board;

import com.calvinnordstrom.cnboard.util.Resources;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class BoardModel {
    private final ObservableList<Sound> sounds = FXCollections.observableArrayList();
    private final Settings settings = new Settings();
    private final AudioRouter router;
    private final InputHandler inputHandler;

    public BoardModel() {
        TargetDataLine inputLine = AudioUtils.getDefaultTarget();
        SourceDataLine outputLine = AudioUtils.getSourceByName("CABLE Input (VB-Audio Virtual Cable)");
//        SourceDataLine playbackLine = AudioUtils.getDefaultSource();
//        SourceDataLine playbackLine = AudioUtils.getSourceByName("Speakers (Logitech PRO X Gaming Headset)");
        router = new AudioRouter(inputLine, outputLine);
        router.start();

        inputHandler = new InputHandler(this);

        sounds.add(Sounds.createBruhSound());
        sounds.add(Sounds.createTacoBellSound());

        for (int i = 2; i < 7; i++) {
            sounds.add(new Sound.Builder()
                    .title("Bruh " + i)
                    .iconFile(Resources.DEFAULT_ICON_FILE)
                    .soundFile(Resources.BRUH_SOUND_FILE)
                    .keyCode(i + 2)
                    .volume(50)
                    .enabled(true)
                    .build());
        }
    }

    public void keyPress(int keyCode) {
        inputHandler.onKeyPressed(keyCode);
    }

    public void keyRelease(int keyCode) {
        inputHandler.onKeyReleased(keyCode);
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

    public Settings getSettings() {
        return settings;
    }

    public AudioRouter getRouter() {
        return router;
    }
}
