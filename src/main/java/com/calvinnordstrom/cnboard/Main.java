package com.calvinnordstrom.cnboard;

import com.calvinnordstrom.cnboard.board.BoardController;
import com.calvinnordstrom.cnboard.board.BoardManager;
import com.calvinnordstrom.cnboard.board.view.BoardView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {
    public static final Logger LOGGER = Logger.getLogger(Main.class.getPackageName());
    public static final String TITLE = "CNBoard";
    public static final String VERSION = "0.0";

    @Override
    public void start(Stage stage) {
        BoardManager model = new BoardManager();
        BoardView view = new BoardView(model.getSounds());
        BoardController controller = new BoardController(model, view);

        Scene scene = new Scene(controller.getView(), 960, 540);
//        scene.setOnKeyPressed(keyEvent -> KeyListener.getInstance().keyPressed(keyEvent.getCode()));
//        scene.setOnKeyReleased(keyEvent -> KeyListener.getInstance().keyReleased(keyEvent.getCode()));
        scene.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("css/styles.css")).toExternalForm());
        stage.setScene(scene);
        stage.setTitle(TITLE + " " + VERSION);
        stage.show();

        try {
            Logger.getLogger(GlobalScreen.class.getPackageName()).setLevel(Level.OFF);
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
    }

    @Override
    public void stop() {
        try {
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}