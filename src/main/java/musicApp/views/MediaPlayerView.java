package musicApp.views;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import musicApp.services.ViewService;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

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

        void toggleMiniPlayer();

        void handleLaunchDjMode();

        void seek(double duration);

        void changeSpeed(double speed);

        void toggleLyrics(boolean show);

        void toggleShuffle(boolean isShuffle);

        void handlePlayingStatusChange(Consumer<Boolean> callback);

        void bindProgressProperty(DoubleProperty property);

        void handleProgressChange(Runnable callback);

        void bindVolumeProperty(DoubleBinding divide);

        void handleCurrentSongTitleChange(Consumer<String> callback);

        void handleCurrentSongArtistChange(Consumer<String> callback);

        void handleCurrentImageCoverChange(Consumer<Image> callback);

        void handleLoadedSongStatusChange(Consumer<Boolean> callback);

        Duration getCurrentTime();

        Duration getTotalDuration();
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
        setDefaultCoverImage();
    }


    private void setButtonActions() {
        pauseSongButton.setOnAction(_ -> listener.handlePauseSong());
        nextSongButton.setOnAction(_ -> listener.handleNextSong());
        djButton.setOnAction(_ -> listener.handleLaunchDjMode());
        previousSongButton.setOnAction(_ -> listener.handlePreviousSong());
        shuffleToggle.setOnAction(_ -> listener.toggleShuffle(shuffleToggle.isSelected()));
        lyricsToggle.setOnAction(_ -> listener.toggleLyrics(lyricsToggle.isSelected()));
        miniPlayerToggle.setOnAction(_ -> listener.toggleMiniPlayer() );
    }

    private void initBindings() {
        bindPlayPauseButton();
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
    public void bindPlayPauseButton() {
        listener.handlePlayingStatusChange(this::updatePlayPauseButton);
    }

    private void updatePlayPauseButton(boolean isPlaying) {
        ViewService viewService = new ViewService();
        if (isPlaying) {
            ImageView pauseIcon = viewService.createIcon("/images/pause_white.png");
            pauseSongButton.setGraphic(pauseIcon);
            currentSongLabel.setStyle("-fx-text-fill: #4CAF50;");
        } else {
            ImageView playIcon = viewService.createIcon("/images/play_white.png");
            pauseSongButton.setGraphic(playIcon);
            currentSongLabel.setStyle("-fx-text-fill: white;");
        }
    }


    /**
     * Bind the song progress bar and label.
     */
    private void bindSongProgress() {
        listener.bindProgressProperty(songProgressBar.progressProperty());
        listener.handleProgressChange(this::updateProgressBarTime);
        songProgressBar.setOnMouseClicked(e -> {
            double progress = e.getX() / songProgressBar.getWidth();
            listener.seek(progress);
        });
    }

    private void updateProgressBarTime() {
        songProgressTimeLabel.setText(getFormattedSongProgress());
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
        listener.bindVolumeProperty(volumeSlider.valueProperty().divide(100));

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
        listener.handleCurrentSongTitleChange(this::updateCurrentSongLabel);
        listener.handleCurrentSongArtistChange(this::updateCurrentArtistLabel);
    }

    private void updateCurrentSongLabel(String newSongTitle) {
        currentSongLabel.setText(newSongTitle == null ? "" : newSongTitle);
    }

    private void updateCurrentArtistLabel(String newArtist) {
        currentArtistLabel.setText(newArtist == null ? "" : newArtist);
    }



    /**
     * Bind the current song cover.
     */
    private void bindCurrentSongCover() {
        listener.handleCurrentImageCoverChange(this::updateCurrentImageCover);
    }

    private void updateCurrentImageCover(Image coverImage) {
        if (coverImage == null) {setDefaultCoverImage();}
        else {imageCover.setImage(coverImage);}
    }

    private void setDefaultCoverImage(){
        imageCover.setImage(loadDefaultCoverImage());
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
        listener.handleLoadedSongStatusChange(isLoaded ->
                updateLoadedSongChange(controls, isLoaded)
        );
    }

    private void updateLoadedSongChange(List<Control> controls,boolean isLoadedSong){
        updateControlsState(controls, !isLoadedSong);
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
