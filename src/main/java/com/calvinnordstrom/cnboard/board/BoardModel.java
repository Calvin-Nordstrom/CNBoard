package com.calvinnordstrom.cnboard.board;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class BoardModel {
    private ObservableList<Sound> sounds;
    private Settings settings;
    private final AudioRouter router;
    private final InputHandler inputHandler;
    private final ModelSerializer modelSerializer;

    public BoardModel() {
        TargetDataLine inputLine = AudioUtils.getDefaultTarget();
        SourceDataLine outputLine = AudioUtils.getSourceByName("CABLE Input (VB-Audio Virtual Cable)");
        router = new AudioRouter(inputLine, outputLine);
        inputHandler = new InputHandler(this);
        modelSerializer = new ModelSerializer();

        resetSounds();
        resetSettings();

        router.start();
    }

    public void resetSounds() {
        sounds = modelSerializer.loadSounds();
        if (!modelSerializer.soundsFileExists()) {
            modelSerializer.saveSounds(sounds);
        }
        modelSerializer.attachSoundListeners(sounds);
    }

    public void resetSettings() {
        settings = modelSerializer.loadSettings();
        if (!modelSerializer.settingsFileExists()) {
            modelSerializer.saveSettings(settings);
        }
        modelSerializer.attachSettingsListeners(settings);
    }

    /**
     * Saves the model objects to storage, including the list of {@code Sound}
     * objects and the {@code Settings} object.
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
        return FXCollections.unmodifiableObservableList(sounds);
    }

    public Settings getSettings() {
        return settings;
    }

    public AudioRouter getRouter() {
        return router;
    }
}
