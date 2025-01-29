package com.calvinnordstrom.cnboard.view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.kordamp.ikonli.codicons.Codicons;
import org.kordamp.ikonli.javafx.FontIcon;

public class TitleBar extends BorderPane {
    private final Stage stage;
    private final ImageView iconView;
    private final Label label;
    private final Button minimize;
    private final Button close;
    private double xOffset = 0;
    private double yOffset = 0;

    public TitleBar(Stage stage, Image icon) {
        this.stage = stage;
        iconView = new ImageView(icon);
        label = new Label(stage.getTitle());
        minimize = createTitleButton(new FontIcon(Codicons.CHROME_MINIMIZE));
        close = createTitleButton(new FontIcon(Codicons.CHROME_CLOSE));

        init();
        initEventHandlers();
    }

    private void init() {
        iconView.setFitWidth(24);
        iconView.setFitHeight(24);
        iconView.setSmooth(true);

        HBox left = new HBox(iconView, label);
        setLeft(left);
        HBox right = new HBox(minimize, close);
        setRight(right);

        minimize.prefHeightProperty().bind(heightProperty());
        close.prefHeightProperty().bind(heightProperty());

        getStyleClass().add("title-bar");
        label.getStyleClass().addAll("title-bar_text");
        left.getStyleClass().add("title-bar_left");
        close.getStyleClass().add("title-bar_close-button");
        right.getStyleClass().add("title-bar_right");
    }

    private void initEventHandlers() {
        setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
        setOnMouseReleased(_ -> {
            stage.setY(Math.max(stage.getY(), 0));
        });

        label.textProperty().bind(stage.titleProperty());
        minimize.setOnAction(_ -> stage.setIconified(true));
        close.setOnAction(_ -> stage.close());
    }

    public void setTitle(String title) {
        stage.setTitle(title);
    }

    public void setIcon(Image icon) {
        iconView.setImage(icon);
    }

    private static Button createTitleButton(FontIcon icon) {
        icon.setIconColor(Color.WHITE);

        Button b = new Button();
        b.setGraphic(icon);

        b.getStyleClass().add("title-bar_button");

        return b;
    }
}
