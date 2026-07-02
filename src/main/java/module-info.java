module com.example.putihmerah. {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    exports  com.example.putihmerah.app;
    exports  com.example.putihmerah.view;
    exports  com.example.putihmerah.controller;
    exports  com.example.putihmerah.model;
    exports  com.example.putihmerah.util;

    opens  com.example.putihmerah.view to javafx.graphics, javafx.fxml;
    opens  com.example.putihmerah.app to javafx.graphics;
}