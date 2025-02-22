package ulb.views;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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

    @FXML private Label songProgressTimeLabel;

    @FXML private ListView <String> playListView;

    @FXML private ListView <String> queueListView;

    @FXML private Button addSongButton;
    @FXML private Button deleteSongButton;
    @FXML private Button clearQueueButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initPlayerController();
        initProgressBindings();
        initVolumeControls();
        updatePlayListView();
        updateQueueListView();


        // When an item is selected in the library, clear selection in the queue.
        playListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                queueListView.getSelectionModel().clearSelection();
            }
        });

        // When an item is selected in the queue, clear selection in the library.
        queueListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                playListView.getSelectionModel().clearSelection();
            }
        });
    }

    private void changeSong() {
        resetVolumeSlider();
    }

    private void initPlayerController() {
        // Initialize your player controller.
        playerController = new PlayerController();
        playerController.setSongChangeListener(this::changeSong);
    }

    private void initProgressBindings() {
        songProgressBar.progressProperty().bind(playerController.progressProperty());
        songProgressTimeLabel.textProperty().bind(Bindings.createStringBinding(() -> {
            Duration current = playerController.getCurrentTime();
            Duration total = playerController.getTotalDuration();
            return String.format("%s/%s", formatDuration(current), formatDuration(total));
        }, playerController.progressProperty()));
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

    private void updatePlayListView() {
        this.playListView.getItems().clear();
        for (String songPathString : playerController.getLibrary()) {
            this.playListView.getItems().add(songPathString);
        }

    }

    private void updateQueueListView() {
        this.queueListView.getItems().clear();
        for (String songPathString : playerController.getQueue()) {
            this.queueListView.getItems().add(songPathString);
        }
    }

    private void resetVolumeSlider() {
        volumeSlider.setValue(50);
    }


    @FXML
    public void handlePlaySong(){
        int selectedSongIndex = -1;
        if (!queueListView.getSelectionModel().isEmpty()) {
            System.out.println("Playing from queue");
            selectedSongIndex = queueListView.getSelectionModel().getSelectedIndex();
            playerController.playFromQueue(selectedSongIndex);
        } else if (!playListView.getSelectionModel().isEmpty()) {
            System.out.println("Playing from library");
            selectedSongIndex = playListView.getSelectionModel().getSelectedIndex();
            playerController.playFromLibrary(selectedSongIndex);
        }
        if (selectedSongIndex == -1) {
            // Optionally display a warning to the user
            System.out.println("No song selected.");
            return;
        }
        // resetVolumeSlider();
    }

    @FXML
    public void handleStopSong(){
        playerController.stop();
    }

    @FXML
    public void handlePreviousSong(){
        playerController.previous();
        // resetVolumeSlider();
    }

    @FXML
    public void handleNextSong(){
        playerController.next();
        // resetVolumeSlider();
    }

    @FXML
    public void handleAddSong(){
        // Add the selected song from the library to the queue.
        if (!playListView.getSelectionModel().isEmpty()) {
            int selectedSongIndex = playListView.getSelectionModel().getSelectedIndex();
            playerController.addToQueue(selectedSongIndex);
            updateQueueListView();
        }
    }

    @FXML
    public void handleDeleteSong(){
        // Remove the selected song from the queue.
        if (!queueListView.getSelectionModel().isEmpty()) {
            int selectedSongIndex = queueListView.getSelectionModel().getSelectedIndex();
            playerController.removeFromQueue(selectedSongIndex);
            updateQueueListView();
        }
    }

    @FXML
    public void handleClearQueue(){
        playerController.clearQueue();
        updateQueueListView();
    }

}
