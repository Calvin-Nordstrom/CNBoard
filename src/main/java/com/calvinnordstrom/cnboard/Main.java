package com.calvinnordstrom.cnboard;

import com.calvinnordstrom.cnboard.board.BoardController;
import com.calvinnordstrom.cnboard.board.BoardModel;
import com.calvinnordstrom.cnboard.board.BoardView;
import com.calvinnordstrom.cnboard.board.KeyListener;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getPackageName());
    public static final String TITLE = "CNBoard";
    public static final String VERSION = "0.0";

    @Override
    public void start(Stage stage) {
        Logger.getLogger(GlobalScreen.class.getPackageName()).setLevel(Level.OFF);
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            LOGGER.severe(e.getMessage());
        }

        BoardModel model = new BoardModel();
        BoardController controller = new BoardController(model);
        BoardView view = new BoardView(model, controller);

        GlobalScreen.addNativeKeyListener(new KeyListener(controller));

//        TitleBar titleBar = new TitleBar(stage, Resources.DEFAULT_ICON);
//        VBox content = new VBox(titleBar, view);
//
//        StackPane root = new StackPane(content);
//        DropShadow dropShadow = new DropShadow();
//        dropShadow.setColor(Color.rgb(0, 0, 0, 0.15));
//        dropShadow.setRadius(10);
//        dropShadow.setOffsetX(4);
//        dropShadow.setOffsetY(4);
//        root.setEffect(dropShadow);
//        root.setPadding(new Insets(0, 20, 20, 0));

        Node root = view.asNode();
        Scene scene = new Scene((Parent) root);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("static/css/styles.css")).toExternalForm());

//        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.setTitle(TITLE + " " + VERSION);
        stage.setMinWidth(960);
        stage.setMinHeight(540);
        stage.show();

//        ((Region) root).prefHeightProperty().bind(stage.heightProperty());

//        root.getStyleClass().add("root");
    }

    @Override
    public void stop() {
        try {
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException e) {
            LOGGER.severe(e.getMessage());
        }

        System.out.println("STOPPING...");

        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}