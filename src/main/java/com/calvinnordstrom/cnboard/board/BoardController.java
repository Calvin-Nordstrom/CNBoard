package com.calvinnordstrom.cnboard.board;

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
