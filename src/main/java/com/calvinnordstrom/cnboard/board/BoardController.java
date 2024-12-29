package com.calvinnordstrom.cnboard.board;

import com.calvinnordstrom.cnboard.board.view.BoardView;
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
