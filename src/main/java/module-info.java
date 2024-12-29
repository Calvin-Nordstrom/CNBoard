module com.calvinnordstrom.cnboard {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.calvinnordstrom.cnboard to javafx.fxml;
    exports com.calvinnordstrom.cnboard;
}