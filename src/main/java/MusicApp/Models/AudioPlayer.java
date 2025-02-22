package MusicApp.Models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Duration;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class AudioPlayer {
    private MediaPlayer mediaPlayer;
    private final DoubleProperty progress = new SimpleDoubleProperty(0.0);
    private final StringProperty currentSong = new SimpleStringProperty("None");;

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

    public void play() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public Duration getCurrentTime() {
        return (mediaPlayer != null) ? mediaPlayer.getCurrentTime() : Duration.ZERO;
    }

    public Duration getTotalDuration() {
        return (mediaPlayer != null && mediaPlayer.getTotalDuration() != null)
                ? mediaPlayer.getTotalDuration() : Duration.ZERO;
    }

    public DoubleProperty progressProperty() {
        return progress;
    }

    public void setVolume(double volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }

    public Boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }

    public StringProperty currentSongProperty() {
        return currentSong;
    }


}
