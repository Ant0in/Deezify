package MusicApp;

import MusicApp.Controllers.MetaController;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main class that extends the JavaFX Application class.
 * This class is NOT the entry point of the application.
 *
 * @see Application
 * @see MetaController
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        MetaController meta = new MetaController(primaryStage);
        meta.switchScene(MetaController.Scenes.MAINWINDOW);
    }

    public static void main(String[] args) {
        launch(args);
    }
}