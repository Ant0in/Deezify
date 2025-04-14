module ulb.musicApp {
    // Export your packages to make them accessible to other modules
    exports musicApp.controllers;
    exports musicApp.controllers.settings;
    exports musicApp.controllers.songs;
    exports musicApp.controllers.playlists;
    exports musicApp.exceptions;
    exports musicApp.models;
    exports musicApp.services;
    exports musicApp.repositories;
    exports musicApp.repositories.gsonTypeAdapter;
    exports musicApp.views;
    exports musicApp.views.playlists;
    exports musicApp.views.songs;
    exports musicApp.views.settings;
    exports musicApp.enums;

    // If you have any services you want to expose via ServiceLoader
    // provides some.service.Interface with musicApp.implementation.Class;

    // Declare dependencies on other modules
    requires java.base; // This is implicit, but good to declare
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.prefs;
    requires jaudiotagger;
    requires com.google.gson;
    requires java.desktop;


    opens musicApp to javafx.fxml, javafx.controls, javafx.media, javafx.graphics;
    opens musicApp.models to javafx.fxml, javafx.controls, javafx.media, com.google.gson;
    opens musicApp.views to javafx.fxml, javafx.controls, javafx.media;
    opens musicApp.repositories.gsonTypeAdapter to javafx.controls, javafx.fxml, javafx.media;
    opens musicApp.views.playlists to javafx.controls, javafx.fxml, javafx.media;
    opens musicApp.controllers to javafx.fxml, javafx.controls, javafx.media;
    opens musicApp.controllers.settings to javafx.controls, javafx.fxml, javafx.media;
    opens musicApp.controllers.songs to javafx.controls, javafx.fxml, javafx.media, musicApp.controllers;
    opens musicApp.controllers.playlists to javafx.controls, javafx.fxml, javafx.media;
    opens musicApp.views.songs to javafx.controls, javafx.fxml, javafx.media;
    opens musicApp.views.settings to javafx.controls, javafx.fxml, javafx.media;
    opens musicApp.services to jaudiotagger, javafx.controls, javafx.fxml, javafx.media;
    opens musicApp.repositories to jaudiotagger, javafx.controls, javafx.fxml, javafx.media;

}