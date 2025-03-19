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
    exports musicApp.enums;

    opens musicApp to javafx.fxml, javafx.controls, javafx.media;
    opens musicApp.controllers to javafx.fxml, javafx.controls, javafx.media;
    opens musicApp.models to javafx.fxml, javafx.controls, javafx.media;
    opens musicApp.views to javafx.fxml, javafx.controls, javafx.media;
    opens musicApp.utils to javafx.fxml, javafx.controls, javafx.media;
}