package musicApp.controllers;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.util.Duration;
import musicApp.exceptions.BadSongException;
import musicApp.exceptions.EqualizerGainException;
import musicApp.models.Song;
import musicApp.services.ViewService;
import musicApp.views.MediaPlayerView;

import java.util.List;
import java.util.function.Consumer;

/**
 * The  MediaPlayer controller.
 */
public class MediaPlayerController extends ViewController<MediaPlayerView>
        implements MediaPlayerView.MediaPlayerViewListener, ViewService.ViewServiceListener {
    private final PlayerController playerController;
    private final MiniPlayerController miniPlayerController;
    private final DjPlayerController djPlayerController;
    private final AudioPlayerController audioPlayerController;

    /**
     * Instantiates a new Media player controller.
     *
     * @param controller        the player controller
     * @param balance           the balance
     * @param crossfadeDuration the crossfade duration
     * @param equalizerBands    the equalizer bands
     */
    public MediaPlayerController(PlayerController controller, double balance, double crossfadeDuration, List<Double> equalizerBands) {
        super(new MediaPlayerView());
        view.setListener(this);
        playerController = controller;
        miniPlayerController = new MiniPlayerController();
        audioPlayerController = new AudioPlayerController(miniPlayerController);
        djPlayerController = new DjPlayerController(this);

        initView("/fxml/MediaPlayer.fxml");
        setBalance(balance);
        setCrossfadeDuration(crossfadeDuration);
        try {
            setEqualizerBands(equalizerBands);
        } catch (EqualizerGainException e) {
            alertService.showExceptionAlert(e, Alert.AlertType.ERROR);
        }
    }


    /**
     * Is playing boolean property.
     *
     * @return Whether the song is playing.
     */
    public BooleanProperty getIsPlayingProperty() {
        return audioPlayerController.getIsPlayingProperty();
    }

    /**
     * Get the progress of the song.
     *
     * @return The progress of the song.
     */
    public DoubleProperty progressProperty() {
        return audioPlayerController.getProgressProperty();
    }

    /**
     * Get the current time of the song.
     *
     * @return The current time of the song.
     */
    public Duration getCurrentTime() {
        return new Duration(audioPlayerController.getCurrentTime().toMillis());
    }

    /**
     * Get the total duration of the song.
     *
     * @return The total duration of the song.
     */
    public Duration getTotalDuration() {
        return new Duration(audioPlayerController.getTotalDuration().toMillis());
    }

    /**
     * Seek to a specific duration in the song.
     *
     * @param duration The duration to seek to.
     */
    public void seek(double duration) {
        audioPlayerController.seek(duration);
    }

    /**
     * Change speed of the currently playing song.
     *
     * @param speed The speed to set.
     */
    public void changeSpeed(double speed) {
        audioPlayerController.changeSpeed(speed);
    }

    /**
     * Get the volume property.
     *
     * @return The volume property.
     */
    public DoubleProperty getVolumeProperty() {
        return audioPlayerController.getVolumeProperty();
    }

    /**
     * Gets current song.
     *
     * @return the current song
     */
    public Song getLoadedSong() {
        return audioPlayerController.getLoadedSong();
    }

    /**
     * Get the current song property.
     *
     * @return The current song property.
     */
    public StringProperty getCurrentSongProperty() {
        return audioPlayerController.getCurrentSongStringProperty();
    }


    public void handleNotFoundImage(String errorMessage) {
        alertService.showAlert("MediaPlayerController : " + errorMessage, Alert.AlertType.ERROR);
    }

    /**
     * Toggle shuffle.
     */
    public void toggleShuffle(boolean isShuffle) {
        playerController.toggleShuffle(isShuffle);
    }

    /**
     * Close the audio player.
     */
    public void close() {
        audioPlayerController.close();
    }

    /**
     * Methode that handles the pause song button.
     */
    public void handlePauseSong() {
        if (getIsPlayingProperty().get()) {
            pause();
        } else {
            unpause();
        }
    }

    /**
     * Pause the currently playing song.
     */
    public void pause() {
        audioPlayerController.pause();
    }

    /**
     * Unpause the currently paused song.
     */
    public void unpause() {
        audioPlayerController.unpause();
    }


    /**
     * Method that handles the next song button.
     */
    public void handleNextSong() {
        playerController.skip();
    }

    /**
     * Method that handles the previous song button.
     */
    public void handlePreviousSong() {
        playerController.handlePreviousSong();
    }


    /**
     * Sets balance.
     *
     * @param balance the balance
     */
    public void setBalance(double balance) {
        audioPlayerController.setBalance(balance);
    }

    /**
     * Sets the crossfade duration.
     *
     * @param crossfadeDuration the crossfade duration
     */
    public void setCrossfadeDuration(double crossfadeDuration) {
        audioPlayerController.setCrossfadeDuration(crossfadeDuration);
    }

    /**
     * Load and Play the currently selected song.
     *
     * @param song the song
     * @throws BadSongException the bad song exception
     */
    public void playCurrent(Song song) throws BadSongException {
        audioPlayerController.loadSong(song);
        audioPlayerController.setOnEndOfMedia(playerController::skip);
        audioPlayerController.setNextSongSupplier(playerController.getNextSongSupplier());
        audioPlayerController.unpause();
        miniPlayerController.loadSong(song);
    }

    public List<Double> getEqualizerBands() {
        return audioPlayerController.getEqualizerBandsGain();
    }

    /**
     * Set the equalizer bands.
     *
     * @param equalizerBandsGain The gain of the equalizer bands.
     * @throws EqualizerGainException
     */
    public void setEqualizerBands(List<Double> equalizerBandsGain) throws EqualizerGainException {
        audioPlayerController.updateEqualizerBandsGain(equalizerBandsGain);
    }

    public void toggleLyrics(boolean show) {
        playerController.toggleLyrics(show);
    }

    public void toggleMiniPlayer(boolean show) {
        miniPlayerController.toggleView(show);
    }

    public void handleLaunchDjMode() {
        try {
            djPlayerController.play(getLoadedSong());
        } catch (BadSongException e) {
            alertService.showAlert(e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void handlePlayingStatusChange(Consumer<Boolean> callback) {
        getIsPlayingProperty().addListener((_, _, newVal) -> callback.accept(newVal));
    }

    public void bindProgressProperty(DoubleProperty property) {
        property.bind(progressProperty());
    }

    public void handleProgressChange(Runnable callback) {
        progressProperty().
                addListener((_, _, _) -> callback.run());
    }


    public void bindVolumeProperty(DoubleBinding divide) {
        getVolumeProperty().bind(divide);
    }

    public void handleCurrentSongTitleChange(Consumer<String> callback) {
        getCurrentSongProperty().addListener((_, _, _) ->
                callback.accept(getLoadedSong().getTitle()));
    }

    public void handleCurrentSongArtistChange(Consumer<String> callback) {
        getCurrentSongProperty().addListener((_, _, _) ->
                callback.accept(getLoadedSong().getArtist()));
    }

    public void handleCurrentImageCoverChange(Consumer<Image> callback) {
        getCurrentSongProperty().addListener((_, _, _) ->
                callback.accept(getLoadedSong().getCoverImage()));
    }

    public void handleLoadedSongStatusChange(Consumer<Boolean> callback) {
        getCurrentSongProperty().addListener((_, _, _) ->
                callback.accept(getLoadedSong() != null));
    }

}
