package MusicApp;

import MusicApp.Controllers.MetaController;
import MusicApp.Views.MainWindow;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Hello world!
 *
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