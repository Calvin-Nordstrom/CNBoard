package com.calvinnordstrom.cnboard.board;

public class BoardController {
    private final BoardModel model;

    public BoardController(BoardModel model) {
        this.model = model;
    }

    public void onAddSound(Sound sound) {
        model.addSound(sound);
    }

    public void onRemoveSound(Sound sound) {
        model.removeSound(sound);
    }

    public void onKeyPressed(int keyCode) {
        model.keyPress(keyCode);
    }

    public void onKeyReleased(int keyCode) {
        model.keyRelease(keyCode);
    }
}
