module musicApp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.prefs;

    requires jaudiotagger;

    exports musicApp;
    exports musicApp.controllers;
    exports musicApp.exceptions;
    exports musicApp.models;
    exports musicApp.views;
    exports musicApp.utils;

    opens musicApp to javafx.fxml, javafx.controls, javafx.media;
    opens musicApp.Controllers to javafx.fxml, javafx.controls, javafx.media;
    opens musicApp.Models to javafx.fxml, javafx.controls, javafx.media;
    opens musicApp.Views to javafx.fxml, javafx.controls, javafx.media;
    opens musicApp.utils to javafx.fxml, javafx.controls, javafx.media;
}