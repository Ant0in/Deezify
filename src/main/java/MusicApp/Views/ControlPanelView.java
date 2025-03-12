package MusicApp.Views;

import MusicApp.Controllers.ControlPanelController;
import MusicApp.Models.Song;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ControlPanelView {

    private ControlPanelController controlPanelController;
    private Scene scene;

    @FXML
    private Button pauseSongButton, nextSongButton, previousSongButton;
    @FXML
    private ComboBox<String> speedBox;
    @FXML
    private ImageView imageCover;
    @FXML
    private Label volumeLabel, songProgressTimeLabel,currentSongLabel, currentArtistLabel;
    @FXML
    private ProgressBar songProgressBar;
    @FXML
    private Slider volumeSlider;
    @FXML
    private ToggleButton shuffleToggle;

    private Pane controlPanelRoot;

    public ControlPanelView(){
    }

    public void setControlPanelController(ControlPanelController controlPanelController){
        this.controlPanelController = controlPanelController;
    }

    /**
     * Initialize the FXML scene.
     * @param fxmlPath The path to the FXML file.
     * @throws IOException If an error occurs while loading the FXML file.
     */
    public void initializeScene(String fxmlPath) throws IOException {
        URL url = PlayerView.class.getResource(fxmlPath);
        FXMLLoader loader = new FXMLLoader(url);
        loader.setController((Object) this);
        controlPanelRoot = loader.load();
        this.scene = new Scene(controlPanelRoot);
    }

    public Pane getRoot(){
        return controlPanelRoot;
    }
    public void initialize(){
        initBindings();
        initSpeed();
        setButtonActions();
    }


    private void setButtonActions() {
        pauseSongButton.setOnAction(event -> controlPanelController.handlePauseSong());
        nextSongButton.setOnAction(event -> controlPanelController.handleNextSong());
        previousSongButton.setOnAction(event -> controlPanelController.handlePreviousSong());
        shuffleToggle.setOnAction(event -> {
            controlPanelController.toggleShuffle(shuffleToggle.isSelected());
        });
    }
    private void initBindings(){
        bindButtons();
        bindSongProgress();
        bindVolumeControls();
        bindCurrentSongControls();
        bindCurrentSongCover();
        bindButtonsImages();
        bindAllControlActivation();
    }

    public void bindButtons(){
        ImageView playIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/images/play_white.png")).toExternalForm()));
        playIcon.setFitWidth(20);
        playIcon.setFitHeight(20);

        ImageView pauseIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/images/pause_white.png")).toExternalForm()));
        pauseIcon.setFitWidth(20);
        pauseIcon.setFitHeight(20);

        this.controlPanelController.isPlaying().addListener((obs, wasPlaying, isPlaying) -> {
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
     * Bind the song progress bar and label.
     */
    private void bindSongProgress(){
        songProgressBar.progressProperty().bind(controlPanelController.progressProperty());
        songProgressTimeLabel.textProperty().bind(
                Bindings.createStringBinding(
                        this::getFormattedSongProgress,  // Extracted method
                        controlPanelController.progressProperty()
                )
        );
        songProgressBar.setOnMouseClicked(e -> {
            double progress = e.getX() / songProgressBar.getWidth();
            controlPanelController.seek(progress);
        });
    }

    /**
     * Get the formatted song progress.
     * @return The formatted song progress.
     */
    private String getFormattedSongProgress() {
        Duration currentTime = controlPanelController.getCurrentTime();
        Duration totalDuration = controlPanelController.getTotalDuration();

        if (totalDuration == null || totalDuration.isUnknown()) {
            return formatDuration(currentTime) + " / --:--";
        }

        return formatDuration(currentTime) + " / " + formatDuration(totalDuration);
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
     * Initialize speed box.
     */
    private void initSpeed() {
        speedBox.getItems().clear();
        speedBox.getItems().addAll("0.25x", "0.5x", "0.75x", "1x","1.25x", "1.5x", "1.75x", "2x");
        speedBox.setValue("1x");
        speedBox.setOnAction(e -> {
            String speed = speedBox.getValue();
            double rate = getSpeedValue(speed);
            controlPanelController.changeSpeed(rate);
        });
    }

    /**
     * Get the current speed value.
     * @param speedLabel The speed label.
     * @return The speed value.
     */
    private double getSpeedValue(String speedLabel) {
        return switch (speedLabel) {
            case "0.25x" -> 0.25;
            case "0.5x" -> 0.5;
            case "0.75x" -> 0.75;
            case "1.25x" -> 1.25;
            case "1.5x" -> 1.5;
            case "1.75x" -> 1.75;
            case "2x" -> 2.0;
            default -> 1.0;
        };
    }

    /**
     * Initialize the volume controls.
     */
    private void bindVolumeControls() {
        volumeSlider.setValue(50);
        volumeLabel.textProperty().bind(
                volumeSlider.valueProperty().asString("%.0f")
        );
        controlPanelController.volumeProperty().bind(
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
    private void bindCurrentSongControls(){
        currentSongLabel.textProperty().bind(
                Bindings.createStringBinding(
                        () -> {
                            Song currentSong = controlPanelController.getCurrentSong();
                            return currentSong == null ? "" : currentSong.getSongName();
                        },
                        controlPanelController.currentSongProperty()
                )
        );

        currentArtistLabel.textProperty().bind(
                Bindings.createStringBinding(
                        () -> {
                            Song currentSong = controlPanelController.getCurrentSong();
                            return currentSong == null ? "" : currentSong.getArtistName();
                        },
                        controlPanelController.currentSongProperty()
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
                            Song currentSong = controlPanelController.getCurrentSong();
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
                        controlPanelController.currentSongProperty()
                )
        );
    }

    /**
     * Load the default cover image.
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


        ImageView shuffleIcon = new ImageView(getClass().getResource("/images/shuffle.png").toExternalForm());
        shuffleIcon.setFitWidth(20);
        shuffleIcon.setFitHeight(20);
        shuffleToggle.setGraphic(shuffleIcon);
    }


    private void bindAllControlActivation() {
        List<Control> controls = Arrays.asList( pauseSongButton, nextSongButton, previousSongButton,shuffleToggle, speedBox, volumeSlider);
        updateControlsState(controls, true);
        controlPanelController.currentSongProperty().addListener((obs, oldVal, newVal) -> {
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

}
