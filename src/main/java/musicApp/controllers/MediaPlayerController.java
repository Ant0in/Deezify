package musicApp.controllers;

import musicApp.models.AudioPlayer;
import musicApp.models.Song;
import musicApp.views.MediaPlayerView;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Duration;

import java.util.List;

/**
 * The  MediaPlayer controller.
 */
public class MediaPlayerController extends ViewController<MediaPlayerView, MediaPlayerController> {
    private final PlayerController playerController;
    private final AudioPlayer audioPlayer;

    /**
     * Instantiates a new Media player controller.
     *
     * @param playerController the player controller
     */
    public MediaPlayerController(PlayerController playerController){
        super(new MediaPlayerView());
        this.playerController = playerController;
        audioPlayer = new AudioPlayer();
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
        return audioPlayer.progressProperty();
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
    public Duration getTotalDuration(){
        return audioPlayer.getTotalDuration();
    }

    /**
     * Seek to a specific duration in the song.
     *
     * @param duration The duration to seek to.
     */
    public void seek(double duration){
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
        return audioPlayer.volumeProperty();
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
        return audioPlayer.currentSongStringProperty();
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
        this.playerController.toggleShuffle();
    }

    /**
     * Close the audio player.
     */
    public void close(){
        audioPlayer.close();
    }

    /**
     * Methode that handles the pause song button.
     */
    public void handlePauseSong(){
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
        this.playerController.skip();
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
        this.audioPlayer.setBalance(balance);
    }

    /**
     * Load and Play the currently selected song.
     *
     * @param song the song
     */
    public void playCurrent(Song song) {
        audioPlayer.loadSong(song);
        audioPlayer.setOnEndOfMedia(this.playerController::skip);
        audioPlayer.unpause();
        System.out.println("Playing: " + song.getTitle());

    }

    /**
     * !! This method is not used in the current implementation !!
     * Set the volume of the audio player.
     *
     * @param volume The volume level (0.0 to 1.0).
     */
    public void setVolume(double volume) {
        audioPlayer.setVolume(volume);
    }

    private void updateEqualizerBand(int bandIndex, double value){
        audioPlayer.updateEqualizerBand(bandIndex, value);
    }

    public void setEqualizerBands(List<Double> equalizerBands){
        for(int i = 0; i<equalizerBands.size(); i++){
            updateEqualizerBand(i, equalizerBands.get(i));
        }
    }
}
