package musicApp;

import javafx.application.Application;
import javafx.stage.Stage;
import musicApp.controllers.MetaController;

import java.io.InputStream;
import java.util.logging.LogManager;

/**
 * Main class that extends the JavaFX Application class.
 * This class is NOT the entry point of the application.
 *
 * @see Application
 * @see MetaController
 */
public class Main extends Application {

    public static void main(String[] args) {

        // Load the logging configuration programmatically
        try (InputStream is = Main.class.getClassLoader().getResourceAsStream("logging.properties")) {
            if (is != null) {
                LogManager.getLogManager().readConfiguration(is);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        MetaController meta = new MetaController(primaryStage);
        meta.switchScene(MetaController.Scenes.MAINWINDOW);
    }
}