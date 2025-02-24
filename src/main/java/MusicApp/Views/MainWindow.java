package MusicApp.Views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * MainWindow
 * Class that represents the main window of the application.
 */
public class MainWindow extends Application {

    /**
     * Main method.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Start the application.
     * @param primaryStage The primary stage of the application.
     * @throws IOException If an error occurs while loading the FXML file.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/main_layout.fxml")));
        //Group root = new Group();
        Scene scene = new Scene(root);

        // Apply the CSS file
        // scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/style1.css")).toExternalForm());

        primaryStage.setTitle("Music Player");

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
