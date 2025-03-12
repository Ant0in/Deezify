package musicApp;

import musicApp.controllers.MetaController;
import javafx.application.Application;
import javafx.stage.Stage;

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