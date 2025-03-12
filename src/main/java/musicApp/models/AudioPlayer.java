package MusicApp.Models;

import javafx.beans.property.*;
import javafx.util.Duration;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * AudioPlayer
 * Class that manages the audio playing.
 */
public class AudioPlayer {
    private final DoubleProperty progress = new SimpleDoubleProperty(0.0);
    private final StringProperty currentSongString = new SimpleStringProperty("None");
    private final BooleanProperty isPlaying = new SimpleBooleanProperty(false);
    private final DoubleProperty volume = new SimpleDoubleProperty(1.0);
    private MediaPlayer mediaPlayer;
    private Song currentSong = null;
    private double balance = 0.0;

    /**
     * Load a song into the player.
     *
     * @param song The song to load.
     */
    public void loadSong(Song song) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        this.currentSong = song;
        Media media = new Media(song.getFilePath().toUri().toString());
        mediaPlayer = new MediaPlayer(media);

        currentSongString.set(song.toString());
        mediaPlayer.volumeProperty().bind(volume);
        mediaPlayer.setBalance(balance);

        // Mettre à jour la propriété de progression pendant la lecture
        mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            if (mediaPlayer.getTotalDuration().greaterThan(Duration.ZERO)) {
                progress.set(newTime.toSeconds() / mediaPlayer.getTotalDuration().toSeconds());
            }
        });
    }

    /**
     * Unpause the loaded song.
     */
    public void unpause() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
            isPlaying.set(true);
        }
    }

    /**
     * Pause the loaded song.
     */
    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            isPlaying.set(false);
        }
    }

    /**
     * Change speed of the loaded song.
     */
    public void changeSpeed(double speed) {
        if (mediaPlayer != null) {
            mediaPlayer.setRate(speed);
        }
    }

    /**
     * Get the current time of the song.
     *
     * @return The current time of the song.
     */
    public Duration getCurrentTime() {
        return (mediaPlayer != null) ? mediaPlayer.getCurrentTime() : Duration.ZERO;
    }

    /**
     * Get the total duration of the song.
     *
     * @return The total duration of the song.
     */
    public Duration getTotalDuration() {
        return (mediaPlayer != null && mediaPlayer.getTotalDuration() != null)
                ? mediaPlayer.getTotalDuration() : Duration.ZERO;
    }

    /**
     * Get the progress of the song.
     *
     * @return The progress of the song.
     */
    public DoubleProperty progressProperty() {
        return progress;
    }

    /**
     * Get the progress of the song.
     *
     * @return The progress of the song.
     */
    public double getProgress() {
        return progress.get();
    }

    /**
     * Set the volume of the song.
     *
     * @param volume The volume to set.
     */
    public void setVolume(double volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }

    /**
     * Check if the song is playing.
     *
     * @return True if the song is playing, False otherwise.
     */
    public BooleanProperty isPlaying() {
        return isPlaying;
    }

    /**
     * Get the current song property.
     *
     * @return The current song property.
     */
    public StringProperty currentSongStringProperty() {
        return currentSongString;
    }

    /**
     * Get the current song.
     *
     * @return The current song.
     */
    public String getCurrentSongString() {
        return currentSongString.get();
    }

    public Song getCurrentSong() {
        return currentSong;
    }

    /**
     * Get the media player.
     *
     * @return The media player.
     */
    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    /**
     * Stop the song.
     */
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        mediaPlayer = null;
        isPlaying.set(false);
    }

    /**
     * Seek to a specific progress in the song.
     *
     * @param progress The progress to seek to.
     */
    public void seek(double progress) {
        if (mediaPlayer != null) {
            mediaPlayer.seek(mediaPlayer.getTotalDuration().multiply(progress));
        }
    }

    /**
     * Set the action to perform when the song ends.
     *
     * @param action The action to perform.
     */
    public void setOnEndOfMedia(Runnable action) {
        if (mediaPlayer != null) {
            mediaPlayer.setOnEndOfMedia(action);
        }
    }

    /**
     * Close the player.
     */
    public void close() {
        if (mediaPlayer != null) {
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
    }

    /**
     * Get the volume property.
     *
     * @return The volume property.
     */
    public DoubleProperty volumeProperty() {
        return volume;
    }

    /**
     * Get the balance of the song.
     *
     * @return The balance of the song.
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Get the volume of the song.
     *
     * @return The volume of the song.
     */
    public void setBalance(double balance) {
        this.balance = balance;
        if (mediaPlayer != null) {
            mediaPlayer.setBalance(balance);
        }
    }
}
