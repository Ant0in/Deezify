package musicApp.views;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import musicApp.controllers.DjPlayerController;


public class DjPlayerView extends View<DjPlayerView, DjPlayerController> {

    @FXML
    private Button playButton, pauseButton;

    public DjPlayerView() {
        playButton = new Button("Play");
        pauseButton = new Button("Pause");
        init();
    }

    public Scene getScene() {
        return scene;
    }

    @Override
    public void init() {
        setOnAction();
    }

    public void setOnAction() {
        playButton.setOnAction(_ -> viewController.unpause());
        pauseButton.setOnAction(_ -> viewController.pause());
    }

}
