package com.calvinnordstrom.cnboard.view;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SliderControl extends VBox {
    private final DoubleProperty value;
    private final Label label;
    private final Slider slider;
    private final TextField textField;
    private final Label symbolLabel;

    public SliderControl(String text, double min, double max, DoubleProperty boundValue, String symbol) {
        double val = boundValue.get();
        value = new SimpleDoubleProperty(val);
        label = new Label(text);
        slider = new Slider(min, max, val);
        textField = new TextField(String.valueOf(val));
        symbolLabel = new Label(symbol);

        boundValue.bindBidirectional(value);

        init();
        initEventHandlers();
    }

    private void init() {
        HBox labelPane = new HBox(label);
        HBox sliderPane = new HBox(slider);
        HBox symbolPane = new HBox(symbolLabel);
        HBox hbox = new HBox(sliderPane, textField, symbolPane);
        getChildren().addAll(labelPane, hbox);

        labelPane.getStyleClass().add("control_label-pane");
        slider.getStyleClass().add("slider-control_slider");
        sliderPane.getStyleClass().add("slider-control_slider-pane");
        textField.getStyleClass().add("slider-control_text-field");
        symbolPane.getStyleClass().add("slider-control_symbol-pane");
        hbox.getStyleClass().add("slider-control_hbox");
    }

    private void initEventHandlers() {
        slider.valueProperty().bindBidirectional(value);
        value.addListener((_, _, newValue) -> {
            textField.setText(String.format("%.2f", newValue.doubleValue()));
        });
        textField.setOnAction(_ -> {
            try {
                double newValue = Double.parseDouble(textField.getText());
                if (newValue >= slider.getMin() && newValue <= slider.getMax()) {
                    value.set(newValue);
                } else {
                    textField.setText(String.format("%.2f", value.get()));
                }
            } catch (NumberFormatException e) {
                textField.setText(String.format("%.2f", value.get()));
            }
        });
    }
}