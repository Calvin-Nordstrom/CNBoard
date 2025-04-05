module com.calvinnordstrom.cnboard {
    requires javafx.controls;
    requires javafx.fxml;
    requires jnativehook;
    requires java.logging;
    requires java.desktop;
    requires java.compiler;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.codicons;
    requires jdk.jdi;
    requires org.jetbrains.annotations;
    requires jdk.xml.dom;

    opens com.calvinnordstrom.cnboard to javafx.fxml;
    exports com.calvinnordstrom.cnboard;
    exports com.calvinnordstrom.cnboard.util;
    opens com.calvinnordstrom.cnboard.util to javafx.fxml;
    exports com.calvinnordstrom.cnboard.model;
    opens com.calvinnordstrom.cnboard.model to javafx.fxml;
    exports com.calvinnordstrom.cnboard.controller;
    opens com.calvinnordstrom.cnboard.controller to javafx.fxml;
    exports com.calvinnordstrom.cnboard.view;
    opens com.calvinnordstrom.cnboard.view to javafx.fxml;
    exports com.calvinnordstrom.cnboard.service;
    opens com.calvinnordstrom.cnboard.service to javafx.fxml;
}