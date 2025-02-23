package MusicApp.Models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Duration;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * AudioPlayer
 * Class that manages the audio playing.
 */
public class AudioPlayer {
    private MediaPlayer mediaPlayer;
    private final DoubleProperty progress = new SimpleDoubleProperty(0.0);
    private final StringProperty currentSong = new SimpleStringProperty("None");

    /**
     * Load a song into the player.
     * @param song The song to load.
     */
    public void loadSong(Song song) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        Media media = new Media(new java.io.File(song.getCoverPath()).toURI().toString());
        mediaPlayer = new MediaPlayer(media);


        currentSong.set(song.getSongName());

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
        }
    }

    /**
     * Pause the loaded song.
     */
    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    /**
     * Get the current time of the song.
     * @return The current time of the song.
     */
    public Duration getCurrentTime() {
        return (mediaPlayer != null) ? mediaPlayer.getCurrentTime() : Duration.ZERO;
    }

    /**
     * Get the total duration of the song.
     * @return The total duration of the song.
     */
    public Duration getTotalDuration() {
        return (mediaPlayer != null && mediaPlayer.getTotalDuration() != null)
                ? mediaPlayer.getTotalDuration() : Duration.ZERO;
    }

    /**
     * Get the progress of the song.
     * @return The progress of the song.
     */
    public DoubleProperty progressProperty() {
        return progress;
    }

    /**
     * Get the progress of the song.
     * @return The progress of the song.
     */
    public double getProgress() {
        return progress.get();
    }

    /**
     * Set the volume of the song.
     * @param volume The volume to set.
     */
    public void setVolume(double volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }

    /**
     * Check if the song is playing.
     * @return True if the song is playing, false otherwise.
     */
    public Boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }

    /**
     * Get the current song property.
     * @return The current song property.
     */
    public StringProperty currentSongProperty() {
        return currentSong;
    }

    /**
     * Get the current song.
     * @return The current song.
     */
    public String getCurrentSong() {
        return currentSong.get();
    }

    /**
     * Get the media player.
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
    }

    /**
     * Seek to a specific progress in the song.
     * @param progress The progress to seek to.
     */
    public void seek(double progress) {
        if (mediaPlayer != null) {
            mediaPlayer.seek(mediaPlayer.getTotalDuration().multiply(progress));
        }
    }

    public void setOnEndOfMedia(Runnable action) {
        if (mediaPlayer != null) {
            mediaPlayer.setOnEndOfMedia(action);
        }
    }

}
