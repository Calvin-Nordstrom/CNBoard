package com.calvinnordstrom.cnboard.controller;

import com.calvinnordstrom.cnboard.model.BoardModel;
import com.calvinnordstrom.cnboard.model.Sound;

public class BoardController {
    private final BoardModel model;

    public BoardController(BoardModel model) {
        this.model = model;
    }

    public void addSound(Sound sound) {
        model.addSound(sound);
    }

    public void removeSound(Sound sound) {
        model.removeSound(sound);
    }

    public void restoreBruhSound() {
        model.restoreBruhSound();
    }

    public void restoreTacoBellSound() {
        model.restoreTacoBellSound();
    }

    public void restoreAllSounds() {
        model.restoreAllSounds();
    }

    public void keyPress(int keyCode) {
        model.keyPress(keyCode);
    }

    public void keyRelease(int keyCode) {
        model.keyRelease(keyCode);
    }

    public void startLocalAudio(Sound sound) {
        model.startLocalAudio(sound);
    }

    public void stopLocalAudio() {
        model.stopLocalAudio();
    }

    public void exit() {
        model.shutdown();
    }
}
