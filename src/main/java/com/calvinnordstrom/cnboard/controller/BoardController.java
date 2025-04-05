package com.calvinnordstrom.cnboard.controller;

import com.calvinnordstrom.cnboard.model.Sound;
import com.calvinnordstrom.cnboard.model.BoardModel;

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

    public void keyPress(int keyCode) {
        model.keyPress(keyCode);
    }

    public void keyRelease(int keyCode) {
        model.keyRelease(keyCode);
    }
}
