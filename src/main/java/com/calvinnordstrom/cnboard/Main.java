package com.calvinnordstrom.cnboard;

import com.calvinnordstrom.cnboard.controller.BoardController;
import com.calvinnordstrom.cnboard.model.BoardModel;
import com.calvinnordstrom.cnboard.view.BoardView;
import com.calvinnordstrom.cnboard.controller.KeyListener;
import com.calvinnordstrom.cnboard.util.Resources;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class Main extends Application {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getPackageName());
    private static final String TITLE = "CNBoard";
    private static final String VERSION = "0.0";
    private static final double WIDTH = 1440;
    private static final double MIN_WIDTH = 480;
    private static final double HEIGHT = 960;
    private static final double MIN_HEIGHT = 360;

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
        scene.getStylesheets().add(Resources.STYLES_PATH);

//        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.setTitle(TITLE + " " + VERSION);
        stage.setWidth(WIDTH);
        stage.setMinWidth(MIN_WIDTH);
        stage.setHeight(HEIGHT);
        stage.setMinHeight(MIN_HEIGHT);
//        stage.setMaximized(true);
        stage.setOnHidden(_ -> Platform.exit());
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

        System.exit(0);
    }

    public static void main(String[] args) {
        printLineCount();

        launch(args);
    }

    private static void printLineCount() {
        Path rootDir = Paths.get("C:\\Users\\calno\\Desktop\\IntelliJ Projects\\CNBoard\\src\\main\\java\\com\\calvinnordstrom\\cnboard");
        try (Stream<Path> paths = Files.walk(rootDir)) {
            long totalLines = paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .mapToLong(Main::countLinesInFile)
                    .sum();
            System.out.println(totalLines + " total lines of code");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static long countLinesInFile(Path path) {
        long lineCount = 0;
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            while (reader.readLine() != null) {
                lineCount++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lineCount;
    }
}