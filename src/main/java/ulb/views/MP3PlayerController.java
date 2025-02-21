package ulb.views;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.event.ActionEvent;

public class MP3PlayerController {
    @FXML private Label songTitleLabel;
    @FXML private ProgressBar progressBar;
    @FXML private Slider volumeSlider;
    @FXML private Button playButton;

    @FXML
    public void handlePlay(ActionEvent event) {
        System.out.println("Play button clicked!");
        songTitleLabel.setText("Playing: Song.mp3");
    }

    @FXML
    public void handlePause(ActionEvent event) {
        System.out.println("Pause button clicked!");
    }

    @FXML
    public void handleStop(ActionEvent event) {
        System.out.println("Stop button clicked!");
        songTitleLabel.setText("No song playing");
    }

    @FXML
    public void handleNext(ActionEvent event) {
        System.out.println("Next button clicked!");
    }

    @FXML
    public void handlePrev(ActionEvent event) {
        System.out.println("Previous button clicked!");
    }
}

