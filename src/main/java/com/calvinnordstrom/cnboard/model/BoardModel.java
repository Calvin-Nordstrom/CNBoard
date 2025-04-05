package com.calvinnordstrom.cnboard.model;

import com.calvinnordstrom.cnboard.service.AudioRouter;
import com.calvinnordstrom.cnboard.service.LocalAudioPlayer;
import com.calvinnordstrom.cnboard.util.AudioUtils;
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
    private final LocalAudioPlayer localAudioPlayer = new LocalAudioPlayer();

    public BoardModel() {
        TargetDataLine inputLine = AudioUtils.getDefaultTarget();
        SourceDataLine outputLine = AudioUtils.getSourceByName("CABLE Input (VB-Audio Virtual Cable)");
        router = new AudioRouter(inputLine, outputLine);
        modelSerializer = new ModelSerializer();

        resetSounds();
        resetSettings();

        inputHandler = new InputHandler(sounds, settings, router);

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

    public void startLocalAudio(Sound sound) {
        float volume = (float) (sound.getVolume() / 100);
        localAudioPlayer.start(sound.getSoundFile(), volume);
    }

    public void stopLocalAudio() {
        localAudioPlayer.stop();
    }

    public Settings getSettings() {
        return settings;
    }

    public AudioRouter getRouter() {
        return router;
    }
}
