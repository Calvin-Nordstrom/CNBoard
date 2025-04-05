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
    exports com.calvinnordstrom.cnboard.board;
    opens com.calvinnordstrom.cnboard.board to javafx.fxml;
    exports com.calvinnordstrom.cnboard.util;
    opens com.calvinnordstrom.cnboard.util to javafx.fxml;
}