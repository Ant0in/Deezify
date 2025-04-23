package musicApp.views;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import musicApp.models.Song;
import musicApp.services.ViewService;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * The MediaPlayer view.
 */
public class MediaPlayerView extends View {
    
    private MediaPlayerViewListener listener;

    @FXML
    private Button pauseSongButton, nextSongButton, previousSongButton, djButton;
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
    private ToggleButton shuffleToggle, lyricsToggle, miniPlayerToggle;


    /**
     * Listener interface for handling user actions from the media player view.
     */
    public interface MediaPlayerViewListener extends ViewService.ViewServiceListener{
        void handlePauseSong();

        void handleNextSong();

        void handlePreviousSong();

        void toggleShuffle();

        void toggleMiniPlayer();

        void handleLaunchDjMode();

        void seek(double duration);

        void changeSpeed(double speed);

        void toggleLyrics(boolean show);

        BooleanProperty isPlaying();

        DoubleProperty progressProperty();

        Duration getCurrentTime();

        Duration getTotalDuration();

        DoubleProperty volumeProperty();

        Song getLoadedSong();

        StringProperty currentSongProperty();
    }

    /**
     * Sets listener.
     *
     * @param newListener the listener
     */
    public void setListener(MediaPlayerViewListener newListener) {
        listener = newListener;
    }


    @Override
    public void init() {
        initBindings();
        initSpeed();
        setButtonActions();
    }


    private void setButtonActions() {
        pauseSongButton.setOnAction(_ -> listener.handlePauseSong());
        nextSongButton.setOnAction(_ -> listener.handleNextSong());
        djButton.setOnAction(_ -> listener.handleLaunchDjMode());
        previousSongButton.setOnAction(_ -> listener.handlePreviousSong());
        shuffleToggle.setOnAction(_ -> listener.toggleShuffle());
        lyricsToggle.setOnAction(_ -> listener.toggleLyrics(lyricsToggle.isSelected()));
        miniPlayerToggle.setOnAction(_ -> listener.toggleMiniPlayer() );
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
        ViewService viewService = new ViewService();
        ImageView playIcon = viewService.createIcon("/images/play_white.png");
        ImageView pauseIcon = viewService.createIcon("/images/pause_white.png");

        listener.isPlaying().addListener((_, _, isPlaying) -> {
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
        songProgressBar.progressProperty().bind(listener.progressProperty());
        songProgressTimeLabel.textProperty().bind(
                Bindings.createStringBinding(
                        this::getFormattedSongProgress,  // Extracted method
                        listener.progressProperty()
                )
        );
        songProgressBar.setOnMouseClicked(e -> {
            double progress = e.getX() / songProgressBar.getWidth();
            listener.seek(progress);
        });
    }

    /**
     * Get the formatted song progress.
     *
     * @return The formatted song progress.
     */
    private String getFormattedSongProgress() {
        Duration currentTime = listener.getCurrentTime();
        Duration totalDuration = listener.getTotalDuration();
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
            listener.changeSpeed(rate);
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
        listener.volumeProperty().bind(
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
                            Song currentSong = listener.getLoadedSong();
                            return currentSong == null ? "" : currentSong.getTitle();
                        },
                        listener.currentSongProperty()
                )
        );

        currentArtistLabel.textProperty().bind(
                Bindings.createStringBinding(
                        () -> {
                            Song currentSong = listener.getLoadedSong();
                            return currentSong == null ? "" : currentSong.getArtist();
                        },
                        listener.currentSongProperty()
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
                            Song currentSong = listener.getLoadedSong();
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
                        listener.currentSongProperty()
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
        ViewService viewService = new ViewService();
        viewService.setButtonIcon(pauseSongButton, "/images/play_white.png", listener);
        viewService.setButtonIcon(nextSongButton, "/images/next_white.png", listener);
        viewService.setButtonIcon(previousSongButton, "/images/previous_white.png", listener);
        viewService.setButtonIcon(shuffleToggle, "/images/shuffle.png", listener);
        viewService.setButtonIcon(lyricsToggle, "/images/lyrics.png", listener);
        viewService.setButtonIcon(djButton, "/images/dj.png", listener);
    }

    private void bindAllControlActivation() {
        List<Control> controls = Arrays.asList( pauseSongButton, nextSongButton, previousSongButton,shuffleToggle, speedBox, volumeSlider, lyricsToggle, djButton);
        updateControlsState(controls, true);
        listener.currentSongProperty().addListener((_, _, newVal) -> {
            boolean songIsPlaying = (newVal != null && !newVal.equals("None"));
            updateControlsState(controls, !songIsPlaying);

            if (!listener.getLoadedSong().isSong()) {
                speedBox.setValue("1x");
                speedBox.setDisable(true);
                listener.changeSpeed(1.0);
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
