package MusicApp.Views;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.beans.binding.Bindings;
import javafx.util.Duration;
import MusicApp.Controllers.PlayerController;

import java.net.URL;
import java.util.ResourceBundle;

public class PlayerView implements Initializable {

    private PlayerController playerController;

    @FXML private Button playSongButton, pauseSongButton, nextSongButton, previousSongButton;
    @FXML private Label volumeLabel, songProgressTimeLabel;
    @FXML private Slider volumeSlider;
    @FXML private ProgressBar songProgressBar;
    @FXML private ListView<String> playListView, queueListView;
    @FXML private Button addSongButton, deleteSongButton, clearQueueButton;
    @FXML private Label currentSongLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        playerController = new PlayerController();
        playerController.setSongChangeListener(this::onSongChange);

        initBindings();
        initVolumeControls();
        initCurrentSongControls();
        updatePlayListView();
        updateQueueListView();

        setupListSelectionListeners();
    }

    private void onSongChange() {
        resetVolumeSlider();
    }

    private void initBindings() {
        songProgressBar.progressProperty().bind(playerController.progressProperty());
        songProgressTimeLabel.textProperty().bind(Bindings.createStringBinding(() ->
            formatDuration(playerController.getCurrentTime()) + "/" + formatDuration(playerController.getTotalDuration()),
            playerController.progressProperty()
        ));
    }

    private String formatDuration(Duration duration) {
        int minutes = (int) duration.toMinutes();
        int seconds = (int) (duration.toSeconds() % 60);
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void initCurrentSongControls(){
        currentSongLabel.textProperty().bind(playerController.currentSongProperty());
    }

    private void initVolumeControls() {
        volumeSlider.setValue(50);
        updateVolumeLabel(50);

        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            int volume = newVal.intValue();
            updateVolumeLabel(volume);
            playerController.setVolume(volume * 0.01);
        });
    }

    private void updateVolumeLabel(int volume) {
        volumeLabel.setText(String.valueOf(volume));
    }

    private void setupListSelectionListeners() {
        playListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) queueListView.getSelectionModel().clearSelection();
        });

        queueListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) playListView.getSelectionModel().clearSelection();
        });
    }

    private void updatePlayListView() {
        playListView.getItems().setAll(playerController.getLibrary());
    }

    private void updateQueueListView() {
        queueListView.getItems().setAll(playerController.getQueue());
    }

    private void resetVolumeSlider() {
        volumeSlider.setValue(50);
    }

    @FXML
    private void handlePlaySong() {
        int selectedIndex = getSelectedIndex();
        if (selectedIndex != -1) {
            if (!queueListView.getSelectionModel().isEmpty()) {
                playerController.playFromQueue(selectedIndex);
            } else {
                playerController.playFromLibrary(selectedIndex);
            }
            updatePlayPauseButton(true);
        } else {
            System.out.println("No song selected.");
        }
    }

    private int getSelectedIndex() {
        if (!queueListView.getSelectionModel().isEmpty()) {
            return queueListView.getSelectionModel().getSelectedIndex();
        } else if (!playListView.getSelectionModel().isEmpty()) {
            return playListView.getSelectionModel().getSelectedIndex();
        }
        return -1;
    }

    private void updatePlayPauseButton(boolean isPlaying) {
        pauseSongButton.setText(isPlaying ? "⏸" : "▶");
    }

    @FXML
    private void handlePauseSong() {
        if (playerController.isPlaying()) {
            playerController.pause();
            updatePlayPauseButton(false);
        } else {
            playerController.unpause();
            updatePlayPauseButton(true);
        }
    }

    @FXML
    private void handlePreviousSong() {
        playerController.prec();
    }

    @FXML
    private void handleNextSong() {
        playerController.skip();
        updateQueueListView();
    }

    @FXML
    private void handleAddSong() {
        int index = playListView.getSelectionModel().getSelectedIndex();
        if (index != -1) {
            playerController.addToQueue(index);
            updateQueueListView();
        }
    }

    @FXML
    private void handleDeleteSong() {
        int index = queueListView.getSelectionModel().getSelectedIndex();
        if (index != -1) {
            playerController.removeFromQueue(index);
            updateQueueListView();
        }
    }

    @FXML
    private void handleClearQueue() {
        playerController.clearQueue();
        updateQueueListView();
    }
}
