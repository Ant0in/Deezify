package musicApp;

import javafx.application.Application;
import javafx.stage.Stage;
import musicApp.controllers.MetaController;

import java.io.InputStream;
import java.util.logging.LogManager;

/**
 * Launcher class that extends the JavaFX Application class.
 *
 * @see Application
 * @see MetaController
 */
public class Launcher extends Application {

    public static void main(String[] args) {

        // Load the logging configuration programmatically
        try (InputStream is = Launcher.class.getClassLoader().getResourceAsStream("logging.properties")) {
            if (is != null) {
                LogManager.getLogManager().readConfiguration(is);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setResizable(true);
        MetaController meta = new MetaController(primaryStage);
        meta.switchScene(MetaController.Scenes.USERSWINDOW);
    }
}