package MusicApp.Views;

import MusicApp.Models.Song;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.beans.binding.Bindings;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import MusicApp.Controllers.PlayerController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * PlayerView
 * Class that represents the view of the music player.
 */
public class PlayerView {

    private final PlayerController playerController;
    private Scene scene;

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

    private static final String PLAY_ICON = "▶";
    private static final String PAUSE_ICON = "⏸";


    public PlayerView(PlayerController playerController) throws IOException {
        this.playerController = playerController;
        initializeScene("/fxml/main_layout.fxml");
        initialize();
    }

    public void initializeScene(String fxmlPath) throws IOException {
        URL url = PlayerView.class.getResource(fxmlPath);
        FXMLLoader loader = new FXMLLoader(url);
        loader.setController((Object) this);
        Pane root = loader.load();
        this.scene = new Scene(root);
    }

    public void initialize(){
        initBindings();
        initSongInput();
        updatePlayListView();
        updateQueueListView();
        enableQueueDragAndDrop();
        setupListSelectionListeners();
        enableDoubleClickToPlay();
        setupListSelectionListeners();
    }

    /**
     * Initialize the bindings between the view and the controller.
     */
    private void initBindings() {
        bindButtons();
        bindSongProgress();
        bindPlayingSongAnchor();
        bindSelectedSongAnchor();
        bindVolumeControls();
        bindCurrentSongControls();
    }

    private void bindButtons(){
        pauseSongButton.textProperty().bind(Bindings.when(playerController.isPlaying())
                .then(PAUSE_ICON)
                .otherwise(PLAY_ICON));
    }

    private void bindSongProgress(){
        songProgressBar.progressProperty().bind(playerController.progressProperty());
        songProgressTimeLabel.textProperty().bind(
                Bindings.createStringBinding(
                        this::getFormattedSongProgress,  // Extracted method
                        playerController.progressProperty()
                )
        );
        songProgressBar.setOnMouseClicked(e -> {
            double progress = e.getX() / songProgressBar.getWidth();
            playerController.seek(progress);
        });
    }

    private String getFormattedSongProgress() {
        Duration currentTime = playerController.getCurrentTime();
        Duration totalDuration = playerController.getTotalDuration();

        if (totalDuration == null || totalDuration.isUnknown()) {
            return formatDuration(currentTime) + " / --:--";
        }

        return formatDuration(currentTime) + " / " + formatDuration(totalDuration);
    }


    private void bindPlayingSongAnchor(){
        playingSongAnchorPane.visibleProperty().bind(playerController.currentSongProperty().isNotEqualTo("None"));
    }

    private void bindSelectedSongAnchor(){
        selectedSongAnchorPane.visibleProperty().bind(
                playListView.getSelectionModel().selectedItemProperty().isNotNull()
                        .or(queueListView.getSelectionModel().selectedItemProperty().isNotNull())
        );
    }

    /**
     * Initialize the volume controls.
     */
    private void bindVolumeControls() {
        volumeSlider.setValue(50);
        volumeLabel.textProperty().bind(
                volumeSlider.valueProperty().asString("%.0f")
        );
        playerController.volumeProperty().bind(
                volumeSlider.valueProperty().divide(100)
        );
    }

    private void bindCurrentSongControls(){
        currentSongLabel.textProperty().bind(playerController.currentSongProperty());
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

    public void enableDoubleClickToPlay(){
        playListView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                handlePlaySong();
            }
        });
    }

    public String getTitle(){
        return "Music Player";
    }

    public void show(Stage stage) {
        stage.setScene(this.scene);
        stage.setTitle(getTitle());
        stage.show();
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
        playListView.getItems().setAll(playerController.getLibraryNames());
    }

    /**
     * Update the queue list view.
     */
    private void updateQueueListView() {
        queueListView.getItems().setAll(playerController.getQueueNames());
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
        Song selectedSong = playerController.getFromLibrary(index);
        if (index != -1) {
            playerController.addToQueue(selectedSong);
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
            playerController.removeFromQueue(playerController.getQueue().get(index));
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

    private void enableQueueDragAndDrop() {
        queueListView.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item);
                }
            };

            // Start of dragging
            cell.setOnDragDetected(event -> onDragDetected(event, cell));

            // Allows receiving the dragged element
            cell.setOnDragOver(event -> onDragOver(event, cell));

            // When the element is released
            cell.setOnDragDropped(event -> onDragDropped(event, cell));

            return cell;
        });
    }

    private void onDragDetected(MouseEvent event, ListCell<String> cell) {
        if (!cell.isEmpty()) {
            Dragboard db = cell.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(cell.getItem());
            db.setContent(content);
            event.consume();
        }
    }

    private void onDragOver(DragEvent event, ListCell<String> cell) {
        if (event.getGestureSource() != cell && event.getDragboard().hasString()) {
            event.acceptTransferModes(TransferMode.MOVE);
        }
        event.consume();
    }

    private void onDragDropped(DragEvent event, ListCell<String> cell) {
        Dragboard db = event.getDragboard();
        if (db.hasString()) {
            int draggedIndex = queueListView.getItems().indexOf(db.getString());
            int dropIndex = cell.getIndex();

            if (draggedIndex != dropIndex) {
                playerController.reorderQueue(draggedIndex, dropIndex);
                updateQueueListView();
            }

            event.setDropCompleted(true);
        }
        event.consume();
    }

}
