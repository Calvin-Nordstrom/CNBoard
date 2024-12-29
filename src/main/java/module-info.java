module com.calvinnordstrom.cnboard {
    requires javafx.controls;
    requires javafx.fxml;
    requires jnativehook;
    requires java.logging;
    requires java.desktop;


    opens com.calvinnordstrom.cnboard to javafx.fxml;
    exports com.calvinnordstrom.cnboard;
    exports com.calvinnordstrom.cnboard.board;
    opens com.calvinnordstrom.cnboard.board to javafx.fxml;
}