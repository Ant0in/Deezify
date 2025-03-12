package MusicApp.Controllers;

import MusicApp.Models.AudioPlayer;
import MusicApp.Models.Song;
import MusicApp.Views.ControlPanelView;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.IOException;

public class ControlPanelController {
    private final PlayerController playerController;
    private ControlPanelView controlPanelView;
    private final AudioPlayer audioPlayer;
    private double currentSpeed = 1.0;

    public ControlPanelController(PlayerController playerController){
        this.playerController = playerController;
        audioPlayer = new AudioPlayer();
        initControlPanelView();
    }

    private void initControlPanelView(){
        this.controlPanelView = new ControlPanelView();
        this.controlPanelView.setControlPanelController(this);
        try {
            this.controlPanelView.initializeScene("/fxml/ControlPanel.fxml");
            this.controlPanelView.initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Pane getRoot() {
        return controlPanelView.getRoot();
    }

    /**
     * Return whether the song is playing.
     * @return Whether the song is playing.
     */
    public BooleanProperty isPlaying(){
        return audioPlayer.isPlaying();
    }

    /**
     * Get the progress of the song.
     * @return The progress of the song.
     */
    public DoubleProperty progressProperty() {
        return audioPlayer.progressProperty();
    }

    /**
     * Get the current time of the song.
     * @return The current time of the song.
     */
    public Duration getCurrentTime() {
        return audioPlayer.getCurrentTime();
    }

    /**
     * Get the total duration of the song.
     * @return The total duration of the song.
     */
    public Duration getTotalDuration(){
        return audioPlayer.getTotalDuration();
    }

    /**
     * Seek to a specific duration in the song.
     * @param duration The duration to seek to.
     */
    public void seek(double duration){
        audioPlayer.seek(duration);
    }

    /**
     * Change speed of the currently playing song.
     * @param speed The speed to set.
     */
    public void changeSpeed(double speed) {
        this.currentSpeed = speed;
        applyCurrentSpeed();
    }

    /**
     * Get the volume property.
     * @return The volume property.
     */
    public DoubleProperty volumeProperty() {
        return audioPlayer.volumeProperty();
    }

    public Song getCurrentSong() {
        return this.playerController.getCurrentSong();
    }

    /**
     * Get the current song property.
     * @return The current song property.
     */
    public StringProperty currentSongProperty() {
        return audioPlayer.currentSongStringProperty();
    }

    public void toggleShuffle(boolean isEnabled) {
        this.playerController.toggleShuffle(isEnabled);
    }

    public void close(){
        audioPlayer.close();
    }

    /**
     * Handle the pause song button.
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
     * Handle the next song button.
     */
    public void handleNextSong() {
        this.playerController.handleNextSong();
    }

    /**
     * Handle the previous song button.
     */
    public void handlePreviousSong() {
        playerController.handlePreviousSong();
    }


    public void setBalance(double balance) {
        this.audioPlayer.setBalance(balance);
    }

    /**
     * Apply the current speed to the audio player.
     */
    public void applyCurrentSpeed() {
        audioPlayer.changeSpeed(currentSpeed);
    }

    /**
     * Load and Play the currently selected song.
     */
    public void playCurrent(Song song) {
        audioPlayer.loadSong(song);
        applyCurrentSpeed();
        audioPlayer.setOnEndOfMedia(this.playerController::skip);
        audioPlayer.unpause();
        System.out.println("Playing: " + song.getSongName());

    }

    /**
     * !! This method is not used in the current implementation !!
     * Set the volume of the audio player.
     * @param volume The volume level (0.0 to 1.0).
     */
    public void setVolume(double volume) {
        audioPlayer.setVolume(volume);
    }
}
