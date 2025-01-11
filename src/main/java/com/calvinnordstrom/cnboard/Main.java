package com.calvinnordstrom.cnboard;

import com.calvinnordstrom.cnboard.board.BoardController;
import com.calvinnordstrom.cnboard.board.BoardManager;
import com.calvinnordstrom.cnboard.board.view.BoardView;
import javafx.application.Application;
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
        try {
            Logger.getLogger(GlobalScreen.class.getPackageName()).setLevel(Level.OFF);
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            LOGGER.severe(e.getMessage());
        }

        BoardManager model = new BoardManager();
        BoardView view = new BoardView(model.getSounds());
        BoardController controller = new BoardController(model, view);

//        TitleBar titleBar = new TitleBar(stage, Resources.DEFAULT_ICON);
//        VBox content = new VBox(titleBar, view);
//
//        StackPane shadowWrapper = new StackPane(content);
//        shadowWrapper.setStyle("-fx-background-color: transparent;");
//        shadowWrapper.setPadding(new Insets(0, 20, 20, 0));
//
//        DropShadow dropShadow = new DropShadow();
//        dropShadow.setColor(Color.rgb(0, 0, 0, 0.25));
//        dropShadow.setRadius(12);
//        dropShadow.setOffsetX(4);
//        dropShadow.setOffsetY(4);
//        shadowWrapper.setEffect(dropShadow);
//
//        Scene scene = new Scene(shadowWrapper);
        Scene scene = new Scene(view);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("css/styles.css")).toExternalForm());

//        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
//        stage.getIcons().add(new Image(Resources.DEFAULT_ICON.getAbsolutePath()));
        stage.setTitle(TITLE + " " + VERSION);
        stage.setMinWidth(960);
        stage.setMinHeight(540);
        stage.show();

        view.prefHeightProperty().bind(stage.heightProperty());
    }

    @Override
    public void stop() {
        try {
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException e) {
            LOGGER.severe(e.getMessage());
        }

        System.out.println("TESTING");
    }

    public static void main(String[] args) {
        launch(args);
    }
}