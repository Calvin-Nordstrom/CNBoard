package com.calvinnordstrom.cnboard.view;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class KeybindControl implements NativeKeyListener {
    private static final String DEFAULT_TEXT = "Set keybind";
    private static final String WAITING_TEXT = "Type a key...";
    private final IntegerProperty value;
    private final VBox view = new VBox();
    private final Label label;
    private final Button keybindButton = new Button(DEFAULT_TEXT);
    private final Label keyCodeLabel;
    private boolean waitingForKey = false;

    public KeybindControl(String text, IntegerProperty boundValue) {
        int val = boundValue.get();
        value = new SimpleIntegerProperty(val);
        label = new Label(text);
        keyCodeLabel = new Label(NativeKeyEvent.getKeyText(val));

        boundValue.bindBidirectional(value);

        init();
        initEventHandlers();
    }

    private void init() {
        HBox labelPane = new HBox(label);
        HBox keybindPane = new HBox(keybindButton, keyCodeLabel);
        view.getChildren().addAll(labelPane, keybindPane);

        label.getStyleClass().addAll("text", "title");
        labelPane.getStyleClass().add("control_label-pane");
        keybindButton.getStyleClass().add("button-width-100");
        keyCodeLabel.getStyleClass().addAll("text", "title");
        keybindPane.getStyleClass().add("keybind-control_keybind-pane");
    }

    private void initEventHandlers() {
        keybindButton.setOnMouseClicked(_ -> {
            if (waitingForKey) {
                waitingForKey = false;
                keybindButton.setText(DEFAULT_TEXT);
            } else {
                waitingForKey = true;
                keybindButton.setText(WAITING_TEXT);
            }
        });
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent event) {}

    @Override
    public void nativeKeyPressed(NativeKeyEvent event) {
        Platform.runLater(() -> {
            if (waitingForKey) {
                waitingForKey = false;
                keybindButton.setText(DEFAULT_TEXT);

                int keyCode = event.getKeyCode();
                value.set(keyCode);
                keyCodeLabel.setText(NativeKeyEvent.getKeyText(keyCode));
            }
        });
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent event) {}

    public Node asNode() {
        return view;
    }
}
