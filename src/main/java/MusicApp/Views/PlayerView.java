package MusicApp.Views;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.beans.binding.Bindings;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import MusicApp.Controllers.PlayerController;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * PlayerView
 * Class that represents the view of the music player.
 */
public class PlayerView implements Initializable {

    private PlayerController playerController;

    @FXML
    private Button playSongButton, pauseSongButton, nextSongButton, previousSongButton;
    @FXML
    private Label volumeLabel, songProgressTimeLabel;
    @FXML
    private Slider volumeSlider;
    @FXML
    private ProgressBar songProgressBar;
    @FXML
    private ListView<String> playListView, queueListView;
    @FXML
    private Button addSongButton, deleteSongButton, clearQueueButton;
    @FXML
    private Label currentSongLabel;
    @FXML
    private TextField songInput;
    @FXML
    private AnchorPane selectedSongAnchorPane;
    @FXML
    private AnchorPane playingSongAnchorPane;

    /**
     * Initialize the view.
     * @param location The location of the FXML file.
     * @param resources The resources of the FXML file.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        playerController = new PlayerController();
        playerController.setSongChangeListener(this::onSongChange);

        initBindings();
        initVolumeControls();
        initCurrentSongControls();
        initSongInput();
        updatePlayListView();
        updateQueueListView();

        setupListSelectionListeners();

        playListView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                handlePlaySong();
            }
        });
    }

    /**
     * Called when the current song changes.
     */
    private void onSongChange() {
        resetVolumeSlider();
    }

    /**
     * Initialize the bindings between the view and the controller.
     */
    private void initBindings() {
        songProgressBar.progressProperty().bind(playerController.progressProperty());
        songProgressTimeLabel.textProperty().bind(Bindings.createStringBinding(() ->
            formatDuration(playerController.getCurrentTime()) + "/" + formatDuration(playerController.getTotalDuration()),
            playerController.progressProperty()
        ));
        songProgressBar.setOnMouseClicked(e -> {
            double progress = e.getX() / songProgressBar.getWidth();
            playerController.seek(progress);
        });
        pauseSongButton.textProperty().bind(Bindings.when(playerController.isPlaying())
                .then("⏸")
                .otherwise("▶"));

        playingSongAnchorPane.visibleProperty().bind(playerController.currentSongProperty().isNotEqualTo("None"));
        selectedSongAnchorPane.visibleProperty().bind(
                playListView.getSelectionModel().selectedItemProperty().isNotNull()
                        .or(queueListView.getSelectionModel().selectedItemProperty().isNotNull())
        );
    }

    /**
     * Initialize the song input for the search
     */
    private void initSongInput() {
        songInput.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.isEmpty()) {
                playListView.getItems().setAll(playerController.searchLibrary(newVal));
            } else {
                updatePlayListView();
            }
        });
    }

    /**
     * Format a duration to a string.
     * @param duration The duration to format.
     * @return The formatted duration.
     */
    private String formatDuration(Duration duration) {
        int minutes = (int) duration.toMinutes();
        int seconds = (int) (duration.toSeconds() % 60);
        return String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * Initialize the current song controls.
     */
    private void initCurrentSongControls(){
        currentSongLabel.textProperty().bind(playerController.currentSongProperty());
    }

    /**
     * Initialize the volume controls.
     */
    private void initVolumeControls() {
        volumeSlider.setValue(50);
        updateVolumeLabel(50);

        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            int volume = newVal.intValue();
            updateVolumeLabel(volume);
            playerController.setVolume(volume * 0.01);
        });
    }

    /**
     * Update the volume label.
     * @param volume The volume to set.
     */
    private void updateVolumeLabel(int volume) {
        volumeLabel.setText(String.valueOf(volume));
    }

    /**
     * Setup the list selection listeners.
     */
    private void setupListSelectionListeners() {
        playListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) queueListView.getSelectionModel().clearSelection();
        });

        queueListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) playListView.getSelectionModel().clearSelection();
        });
    }

    /**
     * Update the play list view.
     */
    private void updatePlayListView() {
        playListView.getItems().setAll(playerController.getLibrary());
    }

    /**
     * Update the queue list view.
     */
    private void updateQueueListView() {
        queueListView.getItems().setAll(playerController.getQueue());
    }

    /**
     * Reset the volume slider.
     */
    private void resetVolumeSlider() {
        volumeSlider.setValue(50);
    }

    /**
     * Clears the selection in both the queue and playlist ListViews.
     */

    private void clearSelections(){
        queueListView.getSelectionModel().clearSelection();
        playListView.getSelectionModel().clearSelection();
    }

    /**
     * Handle the play song button.
     */
    @FXML
    private void handlePlaySong() {
        int songIndexFromQueue = queueListView.getSelectionModel().getSelectedIndex();
        int songIndexFromLibrary = playListView.getSelectionModel().getSelectedIndex();
        if (songIndexFromQueue!=-1){
            System.out.println("The selected song index : " + songIndexFromQueue);
            playerController.playFromQueue(songIndexFromQueue);
        }else if (songIndexFromLibrary != -1){
            playerController.playFromLibrary(songIndexFromLibrary);
        }else{
            System.out.println("No song selected.");
        }
        updateQueueListView();
        clearSelections();
    }

    /**
     * Handle the pause song button.
     */
    @FXML
    private void handlePauseSong() {
        if (playerController.isPlaying().get()) {
            playerController.pause();
        } else {
            playerController.unpause();
        }
    }

    /**
     * Handle the previous song button.
     */
    @FXML
    private void handlePreviousSong() {
        playerController.prec();
    }

    /**
     * Handle the next song button.
     */
    @FXML
    private void handleNextSong() {
        playerController.skip();
        updateQueueListView();
    }

    /**
     * Handle the add song button.
     */
    @FXML
    private void handleAddSong() {
        int index = playListView.getSelectionModel().getSelectedIndex();
        if (index != -1) {
            playerController.addToQueue(index);
            updateQueueListView();
        }
    }

    /**
     * Handle the delete song button.
     */
    @FXML
    private void handleDeleteSong() {
        int index = queueListView.getSelectionModel().getSelectedIndex();
        if (index != -1) {
            playerController.removeFromQueue(index);
            updateQueueListView();
        }
    }

    /**
     * Handle the clear queue button.
     */
    @FXML
    private void handleClearQueue() {
        playerController.clearQueue();
        updateQueueListView();
    }
}
