package musicApp.controllers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Duration;
import musicApp.exceptions.BadSongException;
import musicApp.exceptions.EqualizerGainException;
import musicApp.models.AudioPlayer;
import musicApp.models.Song;
import musicApp.views.MediaPlayerView;

import java.util.List;

/**
 * The  MediaPlayer controller.
 */
public class MediaPlayerController extends ViewController<MediaPlayerView> implements MediaPlayerView.MediaPlayerViewListener {
    private final PlayerController playerController;
    private final MiniPlayerController miniPlayerController;
    private final AudioPlayer audioPlayer;

    /**
     * Instantiates a new Media player controller.
     *
     * @param controller the player controller
     */
    public MediaPlayerController(PlayerController controller) {
        super(new MediaPlayerView());
        view.setListener(this);
        playerController = controller;
        miniPlayerController = new MiniPlayerController();
        audioPlayer = new AudioPlayer(miniPlayerController);

        initView("/fxml/MediaPlayer.fxml");
    }


    /**
     * Is playing boolean property.
     *
     * @return Whether the song is playing.
     */
    public BooleanProperty isPlaying() {
        return audioPlayer.isPlaying();
    }

    /**
     * Get the progress of the song.
     *
     * @return The progress of the song.
     */
    public DoubleProperty progressProperty() {
        return audioPlayer.getProgressProperty();
    }

    /**
     * Get the current time of the song.
     *
     * @return The current time of the song.
     */
    public Duration getCurrentTime() {
        return audioPlayer.getCurrentTime();
    }

    /**
     * Get the total duration of the song.
     *
     * @return The total duration of the song.
     */
    public Duration getTotalDuration() {
        return audioPlayer.getTotalDuration();
    }

    /**
     * Seek to a specific duration in the song.
     *
     * @param duration The duration to seek to.
     */
    public void seek(double duration) {
        audioPlayer.seek(duration);
    }

    /**
     * Change speed of the currently playing song.
     *
     * @param speed The speed to set.
     */
    public void changeSpeed(double speed) {
        audioPlayer.changeSpeed(speed);
    }

    /**
     * Get the volume property.
     *
     * @return The volume property.
     */
    public DoubleProperty volumeProperty() {
        return audioPlayer.getVolumeProperty();
    }

    /**
     * Gets current song.
     *
     * @return the current song
     */
    public Song getLoadedSong() {
        return audioPlayer.getLoadedSong();
    }

    /**
     * Get the current song property.
     *
     * @return The current song property.
     */
    public StringProperty currentSongProperty() {
        return audioPlayer.getCurrentSongStringProperty();
    }

    /**
     * Get the isPlaying property.
     *
     * @return The isPlaying property.
     */
    public BooleanProperty isPlayingProperty() {
        return audioPlayer.isPlaying();
    }

    /**
     * Toggle shuffle.
     */
    public void toggleShuffle() {
        playerController.toggleShuffle();
    }

    /**
     * Close the audio player.
     */
    public void close() {
        audioPlayer.close();
    }

    /**
     * Methode that handles the pause song button.
     */
    public void handlePauseSong() {
        if (isPlaying().get()) {
            pause();
        } else {
            unpause();
        }
    }

    /**
     * Pause the currently playing song.
     */
    public void pause() {
        audioPlayer.pause();
    }

    /**
     * Unpause the currently paused song.
     */
    public void unpause() {
        audioPlayer.unpause();
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
        audioPlayer.setBalance(balance);
    }

    /**
     * Load and Play the currently selected song.
     *
     * @param song the song
     * @throws EqualizerGainException 
     */
    public void playCurrent(Song song) throws BadSongException {
        audioPlayer.loadSong(song);
        audioPlayer.setOnEndOfMedia(playerController::skip);
        audioPlayer.unpause();
        miniPlayerController.loadSong(song);
    }

    /**
     * Set the equalizer bands.
     *
     * @param equalizerBandsGain The gain of the equalizer bands.
     * @throws EqualizerGainException 
     */
    public void setEqualizerBands(List<Double> equalizerBandsGain) throws EqualizerGainException {
        audioPlayer.updateEqualizerBandsGain(equalizerBandsGain);
    }

    public List<Double> getEqualizerBands() {
        return audioPlayer.getEqualizerBandsGain();
    }

    public void toggleLyrics(boolean show) {
        playerController.toggleLyrics(show);
    }

    public void toggleMiniPlayer() {
        miniPlayerController.toggleView();
    }
}