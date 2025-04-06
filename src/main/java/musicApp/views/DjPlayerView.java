package musicApp.views;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import musicApp.controllers.DjPlayerController;


public class DjPlayerView extends View<DjPlayerView, DjPlayerController> {

    @FXML private Button playButton;
    @FXML private Button pauseButton;

    public DjPlayerView() {
        // ...
    }

    @Override
    public void init() {
        setOnAction();
    }

    public void setOnAction() {
        playButton.setOnAction(_ -> viewController.play());
        pauseButton.setOnAction(_ -> viewController.pause());
    }

}
