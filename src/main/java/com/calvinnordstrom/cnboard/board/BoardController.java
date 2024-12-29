package com.calvinnordstrom.cnboard.soundboard;

import com.calvinnordstrom.cnboard.view.BoardView;
import javafx.scene.Parent;

public class BoardController {
    private final BoardManager manager;
    private final BoardView view;

    public BoardController(BoardManager manager, BoardView view) {
        this.manager = manager;
        this.view = view;

        init();
    }

    private void init() {

    }

    public Parent getView() {
        return view;
    }
}
