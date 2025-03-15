module MusicApp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.prefs;

    requires jaudiotagger;

    exports MusicApp;
    exports MusicApp.Controllers;
    exports MusicApp.Exceptions;
    exports MusicApp.Models;
    exports MusicApp.Views;
    exports MusicApp.Utils;

    opens MusicApp to javafx.fxml, javafx.controls, javafx.media;
    opens MusicApp.Controllers to javafx.fxml, javafx.controls, javafx.media;
    opens MusicApp.Models to javafx.fxml, javafx.controls, javafx.media;
    opens MusicApp.Views to javafx.fxml, javafx.controls, javafx.media;
    opens MusicApp.Utils to javafx.fxml, javafx.controls, javafx.media;
}