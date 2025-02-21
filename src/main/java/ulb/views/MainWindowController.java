package ulb.views;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import ulb.models.PlayerController;
import javafx.beans.binding.Bindings;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    private PlayerController playerController;

    @FXML private Button playSongButton;
    @FXML private Button stopSongButton;
    @FXML private Button nextSongButton;
    @FXML private Button previousSongButton;


    @FXML private Label volumeLabel;
    @FXML private Slider volumeSlider;
    private int volumeValue;

    @FXML private ProgressBar songProgressBar;
    @FXML private Label songProgressValueLabel;

    @FXML private Label songProgressTimeLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initPlayerController();
        initProgressBindings();
        initVolumeControls();
    }

    private void initPlayerController() {
        // Initialize your player controller.
        playerController = new PlayerController();
    }

    private void initProgressBindings() {
        songProgressBar.progressProperty().bind(playerController.progressProperty());
        songProgressValueLabel.textProperty().bind(Bindings.createStringBinding(
                () -> String.format("%.0f%%", playerController.progressProperty().get() * 100),
                playerController.progressProperty()
        ));

        songProgressTimeLabel.textProperty().bind(Bindings.createStringBinding(() -> {
            Duration current = playerController.getCurrentTime();
            Duration total = playerController.getTotalDuration();
            return String.format("%s/%s", formatDuration(current), formatDuration(total));
        }, playerController.progressProperty()));

        /*songProgressValueLabel.textProperty().bind(Bindings.createStringBinding(() -> {
            Duration current = playerController.getCurrentTime();
            Duration total = playerController.getTotalDuration();
            return String.format("%s / %s", formatDuration(current), formatDuration(total));
        }, playerController.progressProperty()));*/
    }


    private String formatDuration(Duration duration) {
        int minutes = (int) duration.toMinutes();
        int seconds = (int) (duration.toSeconds() % 60);
        return String.format("%02d:%02d", minutes, seconds);
    }


    private void initVolumeControls() {
        // Set initial volume value and update label.
        volumeValue = (int) volumeSlider.getValue();
        volumeLabel.setText(Integer.toString(volumeValue));

        // Use a lambda expression for brevity.
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            volumeValue = newValue.intValue();
            volumeLabel.setText(Integer.toString(volumeValue));
            playerController.setVolume(volumeValue * 0.01);  // assuming setVolume accepts a double between 0.0 and 1.0
        });
    }

    @FXML
    public void handlePlaySong(){
        playerController.play();
    }

    @FXML
    public void handleStopSong(){
        playerController.stop();
    }

    @FXML
    public void handlePreviousSong(){
        playerController.previous();
    }

    @FXML
    public void handleNextSong(){
        playerController.next();
    }
}
