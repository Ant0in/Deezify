package MusicApp.Views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainWindow extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Parent root = FXMLLoader.load(getClass().getResource("/fxml/main_window.fxml"));
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/main_layout.fxml")));
        //Group root = new Group();
        Scene scene = new Scene(root);

        // Apply the CSS file
        // scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm());

        primaryStage.setTitle("Music Player");

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
