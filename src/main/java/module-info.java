module com.example.putihmerah {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.putihmerah to javafx.fxml;
    exports com.example.putihmerah;
}