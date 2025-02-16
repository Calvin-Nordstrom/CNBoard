package com.calvinnordstrom.cnboard.board;

import javafx.collections.ObservableList;

import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class BoardModel {
    private ObservableList<Sound> sounds;
    private Settings settings;
    private final AudioRouter router;
    private final InputHandler inputHandler;
    private final FileManager fileManager;

    public BoardModel() {
        TargetDataLine inputLine = AudioUtils.getDefaultTarget();
        SourceDataLine outputLine = AudioUtils.getSourceByName("CABLE Input (VB-Audio Virtual Cable)");
        router = new AudioRouter(inputLine, outputLine);
        inputHandler = new InputHandler(this);
        fileManager = new FileManager();

        loadModel();

        router.start();
    }

    public void saveModel() {
        fileManager.saveSounds(sounds);
        fileManager.saveSettings(settings);
    }

    private void loadModel() {
        boolean soundsFileExists = fileManager.soundsFileExists();
        sounds = fileManager.loadSounds();
        settings = fileManager.loadSettings();

        if (!soundsFileExists) {
            sounds.add(Sounds.createBruhSound());
            sounds.add(Sounds.createTacoBellSound());
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
