module MusicApp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.prefs;

    requires jaudiotagger;

    exports MusicApp;
    exports MusicApp.controllers;
    exports MusicApp.exceptions;
    exports MusicApp.models;
    exports MusicApp.views;
    exports MusicApp.utils;

    opens MusicApp to javafx.fxml, javafx.controls, javafx.media;
    opens MusicApp.controllers to javafx.fxml, javafx.controls, javafx.media;
    opens MusicApp.models to javafx.fxml, javafx.controls, javafx.media;
    opens MusicApp.views to javafx.fxml, javafx.controls, javafx.media;
    opens MusicApp.utils to javafx.fxml, javafx.controls, javafx.media;
}