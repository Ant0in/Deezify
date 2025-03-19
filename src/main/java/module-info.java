module ulb.MusicApp {
    // Export your packages to make them accessible to other modules
    exports musicApp.controllers;
    exports musicApp.exceptions;
    exports musicApp.models;
    exports musicApp.views;
    exports musicApp.utils;

    // If you have any services you want to expose via ServiceLoader
    // provides some.service.Interface with musicApp.implementation.Class;

    // Declare dependencies on other modules
    requires java.base; // This is implicit, but good to declare
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.prefs;
    requires jaudiotagger;


    opens musicApp to javafx.fxml, javafx.controls, javafx.media;
    opens musicApp.controllers to javafx.fxml, javafx.controls, javafx.media;
    opens musicApp.models to javafx.fxml, javafx.controls, javafx.media;
    opens musicApp.views to javafx.fxml, javafx.controls, javafx.media;
    opens musicApp.utils to javafx.fxml, javafx.controls, javafx.media, jaudiotagger;
}