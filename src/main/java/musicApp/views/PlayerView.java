package musicApp.views;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import musicApp.controllers.PlayerController;
import musicApp.models.Song;
import musicApp.utils.LanguageManager;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


/**
 * PlayerView
 * Class that represents the view of the music player.
 */
public class PlayerView {

    private final PlayerController playerController;
    private Scene scene;

    @FXML
    private Button pauseSongButton, nextSongButton, previousSongButton;
    @FXML
    private Button exitButton, btnSettings;
    @FXML
    private Label volumeLabel, songProgressTimeLabel;
    @FXML
    private Slider volumeSlider;
    @FXML
    private ProgressBar songProgressBar;
    @FXML
    private ListView<Song> playListView, queueListView;
    @FXML
    private Button addSongButton, deleteSongButton, clearQueueButton;
    @FXML
    private Label currentSongLabel, currentArtistLabel;
    @FXML
    private TextField songInput;
    @FXML
    private VBox controls;
    @FXML
    private ImageView imageCover;
    @FXML
    private Pane labelContainer;
    @FXML
    private ToggleButton shuffleToggle;
    @FXML
    private ComboBox<String> speedBox;

    //private static final String PLAY_ICON = "▶";
    //private static final String PAUSE_ICON = "⏸";

    /* To enable drag */
    private double xOffset = 0;
    private double yOffset = 0;

    public PlayerView(PlayerController playerController) throws IOException {
        this.playerController = playerController;
        initializeScene("/fxml/main_layout.fxml");
        initialize();
    }

    /**
     * Initialize the FXML scene.
     *
     * @param fxmlPath The path to the FXML file.
     * @throws IOException If an error occurs while loading the FXML file.
     */
    public void initializeScene(String fxmlPath) throws IOException {
        URL url = PlayerView.class.getResource(fxmlPath);
        FXMLLoader loader = new FXMLLoader(url);
        loader.setController((Object) this);
        Pane root = loader.load();
        this.scene = new Scene(root);
    }

    /**
     * Initialize the view.
     */
    public void initialize() {
        initBindings();
        initSongInput();
        initSpeed();
        initPlayListView();
        updatePlayListView();
        updateQueueListView();
        enableQueueDragAndDrop();
        setupListSelectionListeners();
        enableDoubleClickToPlay();
        enableShuffleToggle();
        setupListSelectionListeners();
        initTranslation();
    }

    /**
     * Initialize the translations of the texts in the view.
     */
    private void initTranslation() {
        addSongButton.setText(LanguageManager.get("button.add"));
        deleteSongButton.setText(LanguageManager.get("button.delete"));
        clearQueueButton.setText(LanguageManager.get("button.clear"));
        songInput.setPromptText(LanguageManager.get("search"));
    }

    /**
     * Initialize the bindings between the view and the controller.
     */
    private void initBindings() {
        bindButtons();
        bindSongProgress();
        bindPlayingSongAnchor();
        bindCurrentSongCover();
        bindVolumeControls();
        bindCurrentSongControls();
        bindButtonsImages();
        bindAllControlActivation();
        bindQueueButtonsActivation();
    }

    /**
     * Bind the buttons.
     */
    private void bindButtons() {
        deleteSongButton.visibleProperty().bind(queueListView.getSelectionModel().selectedItemProperty().isNotNull());
        addSongButton.visibleProperty().bind(playListView.getSelectionModel().selectedItemProperty().isNotNull());

        ImageView playIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/images/play_white.png")).toExternalForm()));
        playIcon.setFitWidth(20);
        playIcon.setFitHeight(20);

        ImageView pauseIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/images/pause_white.png")).toExternalForm()));
        pauseIcon.setFitWidth(20);
        pauseIcon.setFitHeight(20);

        playerController.isPlaying().addListener((obs, wasPlaying, isPlaying) -> {
            if (isPlaying) {
                pauseSongButton.setGraphic(pauseIcon);
                currentSongLabel.setStyle("-fx-text-fill: #4CAF50;");
            } else {
                pauseSongButton.setGraphic(playIcon);
                currentSongLabel.setStyle("-fx-text-fill: white;");
            }
        });
    }

    /**
     * Initialize speed box.
     */
    private void initSpeed() {
        speedBox.getItems().clear();
        speedBox.getItems().addAll("0.25x", "0.5x", "0.75x", "1x", "1.25x", "1.5x", "1.75x", "2x");
        speedBox.setValue("1x");
        speedBox.setOnAction(e -> {
            String speed = speedBox.getValue();
            double rate = playerController.getSpeedValue(speed);
            playerController.changeSpeed(rate);
        });
    }

    /**
     * Bind the song progress bar and label.
     */
    private void bindSongProgress() {
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

    /**
     * Get the formatted song progress.
     *
     * @return The formatted song progress.
     */
    private String getFormattedSongProgress() {
        Duration currentTime = playerController.getCurrentTime();
        Duration totalDuration = playerController.getTotalDuration();

        if (totalDuration == null || totalDuration.isUnknown()) {
            return formatDuration(currentTime) + " / --:--";
        }

        return formatDuration(currentTime) + " / " + formatDuration(totalDuration);
    }


    /**
     * Bind the visibility of the playing song anchor (the controls at the bottom).
     */
    private void bindPlayingSongAnchor() {
        controls.setVisible(true);
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

        // For gradual filling
        volumeSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            double percentage = 100.0 * newValue.doubleValue() / volumeSlider.getMax();
            String style = String.format(
                    "-track-color: linear-gradient(to right, " +
                            "white 0%%, " +
                            "white %1$.1f%%, " +
                            "-default-track-color %1$.1f%%, " +
                            "-default-track-color 100%%);",
                    percentage);
            volumeSlider.setStyle(style);
        });
    }

    /**
     * Bind the current song controls (name and artist).
     */
    private void bindCurrentSongControls() {
        currentSongLabel.textProperty().bind(
                Bindings.createStringBinding(
                        () -> {
                            Song currentSong = playerController.getCurrentSong();
                            return currentSong == null ? "" : currentSong.getSongName();
                        },
                        playerController.currentSongProperty()
                )
        );

        currentArtistLabel.textProperty().bind(
                Bindings.createStringBinding(
                        () -> {
                            Song currentSong = playerController.getCurrentSong();
                            return currentSong == null ? "" : currentSong.getArtistName();
                        },
                        playerController.currentSongProperty()
                )
        );
    }

    /**
     * Bind the current song cover.
     */
    private void bindCurrentSongCover() {
        Image defaultCoverImage = loadDefaultCoverImage();
        imageCover.imageProperty().bind(
                Bindings.createObjectBinding(
                        () -> {
                            Song currentSong = playerController.getCurrentSong();
                            if (currentSong == null || currentSong.getCover() == null) {
                                return defaultCoverImage;
                            }
                            try {
                                Image coverImage = currentSong.getCoverImage();
                                return coverImage != null ? coverImage : defaultCoverImage;
                            } catch (Exception e) {
                                System.err.println("Failed to load cover image for song: " + currentSong.getSongName());
                                return defaultCoverImage;
                            }
                        },
                        playerController.currentSongProperty()
                )
        );
    }

    /**
     * Load the default cover image.
     *
     * @return The default cover image.
     */
    private Image loadDefaultCoverImage() {
        try {
            return new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream("/images/song.png"),
                    "Default cover image not found"
            ));
        } catch (Exception e) {
            System.err.println("Failed to load default cover image");
            return null;
        }
    }

    /**
     * Bind the images of the buttons.
     */
    private void bindButtonsImages() {
        ImageView exitIcon = new ImageView(Objects.requireNonNull(getClass().getResource("/images/cross.png")).toExternalForm());
        exitIcon.setFitWidth(20);
        exitIcon.setFitHeight(20);
        exitButton.setGraphic(exitIcon);

        ImageView playIcon = new ImageView(Objects.requireNonNull(getClass().getResource("/images/play_white.png")).toExternalForm());
        playIcon.setFitWidth(20);
        playIcon.setFitHeight(20);
        pauseSongButton.setGraphic(playIcon);

        ImageView nextIcon = new ImageView(Objects.requireNonNull(getClass().getResource("/images/next_white.png")).toExternalForm());
        nextIcon.setFitWidth(20);
        nextIcon.setFitHeight(20);
        nextSongButton.setGraphic(nextIcon);

        ImageView previousIcon = new ImageView(Objects.requireNonNull(getClass().getResource("/images/previous_white.png")).toExternalForm());
        previousIcon.setFitWidth(20);
        previousIcon.setFitHeight(20);
        previousSongButton.setGraphic(previousIcon);

        ImageView settingsIcon = new ImageView(Objects.requireNonNull(getClass().getResource("/images/settings.png")).toExternalForm());
        settingsIcon.setFitWidth(20);
        settingsIcon.setFitHeight(20);
        btnSettings.setGraphic(settingsIcon);

        ImageView shuffleIcon = new ImageView(getClass().getResource("/images/shuffle.png").toExternalForm());
        shuffleIcon.setFitWidth(20);
        shuffleIcon.setFitHeight(20);
        shuffleToggle.setGraphic(shuffleIcon);
    }


    private void bindAllControlActivation() {
        List<Control> controls = Arrays.asList(pauseSongButton, nextSongButton, previousSongButton, shuffleToggle, speedBox, volumeSlider);
        updateControlsState(controls, true);
        playerController.currentSongProperty().addListener((obs, oldVal, newVal) -> {
            boolean songIsPlaying = (newVal != null && !newVal.equals("None"));
            updateControlsState(controls, !songIsPlaying);
        });
    }

    private void updateControlsState(List<Control> controls, boolean disable) {
        for (Control c : controls) {
            c.setDisable(disable);
            c.getStyleClass().remove("disabled-btn");
            if (disable) {
                c.getStyleClass().add("disabled-btn");
            }
        }
    }

    private void bindQueueButtonsActivation() {
        addSongButton.disableProperty().bind(playListView.getSelectionModel().selectedItemProperty().isNull());
        deleteSongButton.disableProperty().bind(queueListView.getSelectionModel().selectedItemProperty().isNull());
        clearQueueButton.disableProperty().bind(Bindings.isEmpty(queueListView.getItems()));

        applyDisableStyleListener(addSongButton);
        applyDisableStyleListener(deleteSongButton);
        applyDisableStyleListener(clearQueueButton);
    }

    private void applyDisableStyleListener(Control control) {
        control.disableProperty().addListener((obs, wasDisabled, isDisabled) -> {
            if (isDisabled) {
                if (!control.getStyleClass().contains("disabled-btn")) {
                    control.getStyleClass().add("disabled-btn");
                }
            } else {
                control.getStyleClass().remove("disabled-btn");
            }
        });
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
     * Initialize the playlist view.
     */
    private void initPlayListView() {
        playListView.setCellFactory(lv -> new SongCell(playerController));
        updatePlayListView();
    }

    /**
     * Enable the shuffle toggle.
     */
    public void enableShuffleToggle() {
        shuffleToggle.setOnAction(event -> {
            playerController.toggleShuffle(shuffleToggle.isSelected());
            updatePlayListView(); // Update the play list view to show the new order
        });
    }

    /**
     * Enable double click to play
     */
    public void enableDoubleClickToPlay() {
        playListView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                handlePlaySong();
            }
        });
        queueListView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                handlePlaySong();
            }
        });
    }

    /**
     * Enable double click to grow (fullscreen)
     */
    public void enableDoubleClickToGrow(Stage stage) {
        labelContainer.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                stage.setFullScreen(true);
            }
        });
    }

    /**
     * Setup the window close handler.
     *
     * @param stage The stage to setup the handler for.
     */
    private void setupWindowCloseHandler(Stage stage) {
        stage.setOnCloseRequest(e -> {
            playerController.close();
            Platform.exit();
        });
    }

    /**
     * Enable drag of the window.
     */
    private void enableDrag(Stage stage) {
        labelContainer.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        labelContainer.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    /**
     * Get the title of the application.
     *
     * @return The title of the application.
     */
    public String getTitle() {
        return LanguageManager.get("app.title");
    }

    /**
     * Show the stage.
     *
     * @param stage The stage to show.
     */
    public void show(Stage stage) {
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(this.scene);
        stage.setTitle(getTitle());
        enableDrag(stage);
        enableDoubleClickToGrow(stage);
        setupWindowCloseHandler(stage);
        stage.show();
    }

    /**
     * Format a duration to a string.
     *
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
    public void updatePlayListView() {
        playListView.getItems().setAll(playerController.getLibrary().toList());
    }

    /**
     * Update the queue list view.
     */
    public void updateQueueListView() {
        queueListView.getItems().setAll(playerController.getQueue().toList());
    }


    /**
     * Clears the selection in both the queue and playlist ListViews.
     */

    private void clearSelections() {
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
        if (songIndexFromQueue != -1) {
            System.out.println("The selected song index : " + songIndexFromQueue);
            playerController.playFromQueue(songIndexFromQueue);
        } else if (songIndexFromLibrary != -1) {
            playerController.playFromLibrary(songIndexFromLibrary);
        } else {
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

    /**
     * Handle the exit button
     */
    @FXML
    private void handleExitApp() {
        Platform.exit();
    }

    private void enableQueueDragAndDrop() {
        queueListView.setCellFactory(lv -> {
            ListCell<Song> cell = new ListCell<Song>() {
                @Override
                protected void updateItem(Song item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item.getSongName());
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

    /**
     * Handle the drag detected event.
     *
     * @param event The mouse event.
     * @param cell  The list cell.
     */
    private void onDragDetected(MouseEvent event, ListCell<Song> cell) {
        if (!cell.isEmpty()) {
            Dragboard db = cell.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(cell.getItem().getSongName());
            db.setContent(content);
            event.consume();
        }
    }

    /**
     * Handle the drag over event.
     *
     * @param event The drag event.
     * @param cell  The list cell.
     */
    private void onDragOver(DragEvent event, ListCell<Song> cell) {
        if (event.getGestureSource() != cell && event.getDragboard().hasString()) {
            event.acceptTransferModes(TransferMode.MOVE);
        }
        event.consume();
    }

    /**
     * Handle the drag dropped event.
     *
     * @param event The drag event.
     * @param cell  The list cell.
     */
    private void onDragDropped(DragEvent event, ListCell<Song> cell) {
        Dragboard db = event.getDragboard();
        if (db.hasString()) {
            Song draggedSong = queueListView.getItems().stream()
                    .filter(song -> song.getSongName().equals(db.getString()))
                    .findFirst()
                    .orElse(null);

            if (draggedSong != null) {
                int draggedIndex = queueListView.getItems().indexOf(draggedSong);
                int dropIndex = cell.getIndex();

                if (draggedIndex != dropIndex) {
                    playerController.reorderQueue(draggedIndex, dropIndex);
                    updateQueueListView();
                }

                event.setDropCompleted(true);
            }
        }
        event.consume();
    }

    /**
     * Handle the settings button.
     *
     * @param event The action event.
     */
    @FXML
    private void handleSettings(ActionEvent event) {
        playerController.openSettings();
    }

    /**
     * Change the language of the application.
     *
     * @param languageCode The language code. (e.g. "en", "fr", "nl")
     */
    private void switchLanguage(String languageCode) {
        LanguageManager.setLanguage(languageCode);
        refreshUI();
    }

    /**
     * Refresh the UI.
     */
    public void refreshUI() {
        initTranslation();
        Stage stage = (Stage) scene.getWindow();
        stage.setTitle(LanguageManager.get("app.title"));
    }
}
