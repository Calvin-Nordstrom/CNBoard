package com.calvinnordstrom.cnboard.view.control;

import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class SliderControl {
    private final DoubleProperty value;
    private final VBox view = new VBox();
    private final Label label;
    private final Slider slider;
    private final TextField textField;
    private final Label symbolLabel;

    public SliderControl(String text, double min, double max, DoubleProperty value, String symbol) {
        this.value = Objects.requireNonNull(value);
        label = new Label(text);
        slider = new Slider(min, max, value.get());
        textField = new TextField(format(value.get()));
        symbolLabel = new Label(symbol);

        init();
        initEventHandlers();
    }

    private void init() {
        HBox labelPane = new HBox(label);
        HBox sliderPane = new HBox(slider);
        HBox symbolPane = new HBox(symbolLabel);
        HBox hBox = new HBox(sliderPane, textField, symbolPane);
        view.getChildren().addAll(labelPane, hBox);

        HBox.setHgrow(slider, Priority.ALWAYS);
        HBox.setHgrow(sliderPane, Priority.ALWAYS);

        label.getStyleClass().add("title");
        labelPane.getStyleClass().add("control_label-pane");
        slider.getStyleClass().add("slider-control_slider");
        sliderPane.getStyleClass().add("slider-control_slider-pane");
        textField.getStyleClass().add("slider-control_text-field");
        symbolLabel.getStyleClass().add("title");
        symbolPane.getStyleClass().add("slider-control_symbol-pane");
        hBox.getStyleClass().add("slider-control_hBox");
    }

    private void initEventHandlers() {
        slider.valueProperty().bindBidirectional(value);
        value.addListener((_, _, newValue) -> {
            textField.setText(format(newValue.doubleValue()));
        });
        textField.setOnAction(_ -> {
            try {
                double newValue = Double.parseDouble(textField.getText());
                if (newValue >= slider.getMin() && newValue <= slider.getMax()) {
                    value.set(newValue);
                } else {
                    textField.setText(format(value.get()));
                }
            } catch (NumberFormatException e) {
                textField.setText(format(value.get()));
            }
        });
    }

    private String format(double value) {
        return String.format("%.2f", value);
    }

    public Node asNode() {
        return view;
    }
}
