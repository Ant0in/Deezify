package musicApp.views;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import musicApp.controllers.MediaPlayerController;
import musicApp.models.Song;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * The MediaPlayer view.
 */
public class MediaPlayerView extends View<MediaPlayerView, MediaPlayerController> {

    @FXML
    private Button pauseSongButton, nextSongButton, previousSongButton;
    @FXML
    private ComboBox<String> speedBox;
    @FXML
    private ImageView imageCover;
    @FXML
    private Label volumeLabel, songProgressTimeLabel, currentSongLabel, currentArtistLabel;
    @FXML
    private ProgressBar songProgressBar;
    @FXML
    private Slider volumeSlider;
    @FXML
    private ToggleButton shuffleToggle;

    /**
     * Instantiates a new Media player view.
     */
    public MediaPlayerView() {
    }

    @Override
    public void init() {
        initBindings();
        initSpeed();
        setButtonActions();
    }


    private void setButtonActions() {
        pauseSongButton.setOnAction(_ -> viewController.handlePauseSong());
        nextSongButton.setOnAction(_ -> viewController.handleNextSong());
        previousSongButton.setOnAction(_ -> viewController.handlePreviousSong());
        shuffleToggle.setOnAction(_ -> viewController.toggleShuffle());
    }

    private void initBindings() {
        bindButtons();
        bindSongProgress();
        bindVolumeControls();
        bindCurrentSongControls();
        bindCurrentSongCover();
        bindButtonsImages();
        bindAllControlActivation();
    }

    /**
     * Bind buttons.
     */
    public void bindButtons() {
        ImageView playIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/images/play_white.png")).toExternalForm()));
        playIcon.setFitWidth(20);
        playIcon.setFitHeight(20);

        ImageView pauseIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/images/pause_white.png")).toExternalForm()));
        pauseIcon.setFitWidth(20);
        pauseIcon.setFitHeight(20);

        this.viewController.isPlaying().addListener((_, _, isPlaying) -> {
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
    private void bindSongProgress() {
        songProgressBar.progressProperty().bind(viewController.progressProperty());
        songProgressTimeLabel.textProperty().bind(
                Bindings.createStringBinding(
                        this::getFormattedSongProgress,  // Extracted method
                        viewController.progressProperty()
                )
        );
        songProgressBar.setOnMouseClicked(e -> {
            double progress = e.getX() / songProgressBar.getWidth();
            viewController.seek(progress);
        });
    }

    /**
     * Get the formatted song progress.
     *
     * @return The formatted song progress.
     */
    private String getFormattedSongProgress() {
        Duration currentTime = viewController.getCurrentTime();
        Duration totalDuration = viewController.getTotalDuration();
        if (totalDuration == null || totalDuration.isUnknown() || totalDuration == Duration.ZERO) {
            return formatDuration(currentTime) + " / --:--";
        }

        return formatDuration(currentTime) + " / " + formatDuration(totalDuration);
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
     * Initialize speed box.
     */
    private void initSpeed() {
        speedBox.getItems().clear();
        speedBox.getItems().addAll("0.25x", "0.5x", "0.75x", "1x", "1.25x", "1.5x", "1.75x", "2x");
        speedBox.setValue("1x");
        speedBox.setOnAction(_ -> {
            String speed = speedBox.getValue();
            double rate = getSpeedValue(speed);
            viewController.changeSpeed(rate);
        });
    }

    /**
     * Get the current speed value.
     *
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
        viewController.volumeProperty().bind(
                volumeSlider.valueProperty().divide(100)
        );

        // For gradual filling
        volumeSlider.valueProperty().addListener((_, _, newValue) -> {
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
                            Song currentSong = viewController.getLoadedSong();
                            return currentSong == null ? "" : currentSong.getTitle();
                        },
                        viewController.currentSongProperty()
                )
        );

        currentArtistLabel.textProperty().bind(
                Bindings.createStringBinding(
                        () -> {
                            Song currentSong = viewController.getLoadedSong();
                            return currentSong == null ? "" : currentSong.getArtist();
                        },
                        viewController.currentSongProperty()
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
                            Song currentSong = viewController.getLoadedSong();
                            if (currentSong == null || currentSong.getCover() == null) {
                                return defaultCoverImage;
                            }
                            try {
                                Image coverImage = currentSong.getCoverImage();
                                return coverImage != null ? coverImage : defaultCoverImage;
                            } catch (Exception e) {
                                System.err.println("Failed to load cover image for song: " + currentSong.getTitle());
                                return defaultCoverImage;
                            }
                        },
                        viewController.currentSongProperty()
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

        try {
            ImageView shuffleIcon = new ImageView(Objects.requireNonNull(getClass().getResource("/images/shuffle.png")).toExternalForm());
            shuffleIcon.setFitWidth(20);
            shuffleIcon.setFitHeight(20);
            shuffleToggle.setGraphic(shuffleIcon);
        } catch (NullPointerException e) {
            System.err.println("Failed to load shuffle icon");
        }
    }


    private void bindAllControlActivation() {
        List<Control> controls = Arrays.asList(pauseSongButton, nextSongButton, previousSongButton, shuffleToggle, speedBox, volumeSlider);
        updateControlsState(controls, true);
        viewController.currentSongProperty().addListener((_, _, newVal) -> {
            boolean songIsPlaying = (newVal != null && !newVal.equals("None"));
            updateControlsState(controls, !songIsPlaying);

            if (!viewController.getLoadedSong().isSong()) {
                speedBox.setValue("1x");
                speedBox.setDisable(true);
                viewController.changeSpeed(1.0);
            } else {
                speedBox.setDisable(false);
            }
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
