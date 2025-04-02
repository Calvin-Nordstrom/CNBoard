package com.calvinnordstrom.cnboard.board;

import javafx.collections.ObservableList;

import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class BoardModel {
    private final ObservableList<Sound> sounds;
    private final Settings settings;
    private final AudioRouter router;
    private final InputHandler inputHandler;
    private final ModelSerializer modelSerializer;

    public BoardModel() {
        TargetDataLine inputLine = AudioUtils.getDefaultTarget();
        SourceDataLine outputLine = AudioUtils.getSourceByName("CABLE Input (VB-Audio Virtual Cable)");
        router = new AudioRouter(inputLine, outputLine);
        inputHandler = new InputHandler(this);
        modelSerializer = new ModelSerializer();

        sounds = modelSerializer.loadSounds();
        if (!modelSerializer.soundsFileExists()) {
            modelSerializer.saveSounds(sounds);
        }
        modelSerializer.attachSoundListeners(sounds);

        settings = modelSerializer.loadSettings();
        if (!modelSerializer.settingsFileExists()) {
            modelSerializer.saveSettings(settings);
        }
        modelSerializer.attachSettingsListeners(settings);

        router.start();
    }

    /**
     * Saves the model objects to storage, including the list of {@link Sound}
     * objects and the {@link Settings} object.
     */
    public void saveModel() {
        modelSerializer.saveSounds(sounds);
        modelSerializer.saveSettings(settings);
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
