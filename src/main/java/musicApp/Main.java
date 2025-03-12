package musicApp;

import javafx.application.Application;
import javafx.stage.Stage;
import musicApp.controllers.MetaController;

/**
 * Hello world!
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        MetaController meta = new MetaController(primaryStage);
        meta.switchScene(MetaController.Scenes.MAINWINDOW);
    }
}