package musicApp.controllers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
import javafx.scene.media.*;
import javafx.util.Duration;
import musicApp.exceptions.BadSongException;
import musicApp.exceptions.EqualizerGainException;
import musicApp.models.AudioPlayer;
import musicApp.models.Song;
import musicApp.services.AlertService;

import java.util.List;
import java.util.function.Supplier;

public class AudioPlayerController {

    private final AudioPlayer audioPlayer;
    private MediaPlayer mediaPlayer;
    private final AudioSpectrumListener audioSpectrumListener;
    private MediaPlayer transitionPlayer;

    public AudioPlayerController(AudioSpectrumListener _audioSpectrumListener) {
        mediaPlayer = null;
        audioSpectrumListener = _audioSpectrumListener;
        audioPlayer = new AudioPlayer();
    }

    /**
     * Load a song into the player.
     *
     * @param song The song to load.
     */
    public void loadSong(Song song) throws BadSongException {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        audioPlayer.loadSong(song);

        Media media = new Media(song.getSource());
        mediaPlayer = new MediaPlayer(media);

        mediaPlayer.volumeProperty().bind(audioPlayer.getVolumeProperty());
        mediaPlayer.setBalance(audioPlayer.getBalance());
        mediaPlayer.setRate(audioPlayer.getSpeed());
        mediaPlayer.setAudioSpectrumListener(audioSpectrumListener);
        mediaPlayer.setAudioSpectrumInterval(audioPlayer.getAudioSpectrumInterval());
        mediaPlayer.setOnReady(() -> {
            audioPlayer.setLoaded();
            try {
                applyEqualizerBandsGain();
            } catch (EqualizerGainException e) {
                AlertService alertService = new AlertService();
                alertService.showExceptionAlert(e, Alert.AlertType.ERROR);
            }
        });
        if (audioPlayer.isTransitioning()) {
            mediaPlayer.setOnReady(() -> {
                mediaPlayer.seek(transitionPlayer.getCurrentTime());
                audioPlayer.setTransitioning(false);
                if (transitionPlayer != null) {
                    transitionPlayer.stop();
                    transitionPlayer = null;
                }
            });
        }
        // Update the progression property while playing
        setupProgressListener();
        setTransition();
    }

    /**
     * Set up the transition between songs.
     */
    private void setTransition() {
        mediaPlayer.currentTimeProperty().addListener((_, _, newValue)
                -> this.transitionHandler(newValue));
    }

    /**
     * Handle the transition between songs.
     *
     * @param progress The current progress of the song.
     */
    private void transitionHandler(Duration progress) {
        Duration totalDuration = mediaPlayer.getTotalDuration();
        if (totalDuration == null) return ;
        double remainingTime = totalDuration.toSeconds() - progress.toSeconds();
        if (remainingTime <= audioPlayer.getCrossfadeDuration() && !audioPlayer.isTransitioning()) {
            audioPlayer.setTransitioning(true);
            if (transitionPlayer == null) {
                try {
                    Song nextSong = audioPlayer.getNextSongSupplier();
                    Media nextMedia = new Media(nextSong.getSource());
                    transitionPlayer = new MediaPlayer(nextMedia);
                } catch (BadSongException e) {
                    audioPlayer.setTransitioning(false);
                    return;
                }
            }
            transitionPlayer.play();
        }
        if (audioPlayer.isTransitioning()) {
            double crossfadeDuration = audioPlayer.getCrossfadeDuration();
            double fadeInVolume = audioPlayer.getVolumeProperty().get() * ((crossfadeDuration - remainingTime) / crossfadeDuration);
            transitionPlayer.setVolume(fadeInVolume);
        }
    }

    /**
     * Set the next song supplier.
     *
     * @param _nextSongSupplier The next song supplier, which is a function that returns a Song.
     */
    public void setNextSongSupplier(Supplier<Song> _nextSongSupplier)  {
        audioPlayer.setNextSongSupplier(_nextSongSupplier);
    }

    /**
     * Set up a listener to update the progress property.
     */
    private void setupProgressListener() {
        mediaPlayer.currentTimeProperty().addListener((_, _, newTime) -> {
            if (mediaPlayer.getTotalDuration().greaterThan(Duration.ZERO)) {
                audioPlayer.setProgress(newTime.toSeconds() / mediaPlayer.getTotalDuration().toSeconds());
            } else {
                audioPlayer.setProgress(0.0);
            }
        });
    }

    /**
     * Apply the equalizer bands gain to the media player.
     */
    private void applyEqualizerBandsGain() throws EqualizerGainException {
        AudioEqualizer audioEqualizer = mediaPlayer.getAudioEqualizer();
        if (audioEqualizer == null) {
            throw new EqualizerGainException("No audio equalizer available");
        }
        int equalizerBandsGainSize = audioPlayer.getEqualizerBandsGainSize();
        for (int bandIndex = 0; bandIndex < equalizerBandsGainSize; bandIndex++) {
            EqualizerBand bandToSet = audioEqualizer.getBands().get(bandIndex);
            double gain = audioPlayer.getEqualizerBandGain(bandIndex);
            bandToSet.setGain(gain);
        }
    }

    public List<Double> getEqualizerBandsGain() {
        return audioPlayer.getEqualizerBandsGain();
    }

    /**
     * Update the equalizer bands gain.
     * @param newEqualizerBandsGain The new equalizer bands gain.
     */
    public void updateEqualizerBandsGain(List<Double> newEqualizerBandsGain) throws EqualizerGainException {
        audioPlayer.setEqualizerBandsGain(newEqualizerBandsGain);
        if (mediaPlayer != null) {
            applyEqualizerBandsGain();
        }
    }

    /**
     * Unpause the loaded song.
     */
    public void unpause() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
            audioPlayer.setPlaying(true);
        }
    }

    /**
     * Pause the loaded song.
     */
    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            audioPlayer.setPlaying(false);
            if (transitionPlayer != null) {
                transitionPlayer.pause();
            }
        }
    }

    /**
     * Change speed of the loaded song.
     */
    public void changeSpeed(double newSpeed) {
        audioPlayer.setSpeed(newSpeed);
        if (mediaPlayer != null) {
            mediaPlayer.setRate(newSpeed);
        }
    }

    /**
     * Get the current time of the song.
     *
     * @return The current time of the song.
     */
    public Duration getCurrentTime() { return (mediaPlayer != null) ? mediaPlayer.getCurrentTime() : Duration.ZERO; }

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
    public DoubleProperty getProgressProperty() {
        return audioPlayer.getProgressProperty();
    }

    /**
     * Check if the song is playing.
     *
     * @return True if the song is playing, False otherwise.
     */
    public BooleanProperty getIsPlayingProperty() {
        return audioPlayer.getIsPlayingProperty();
    }

    /**
     * Get the current song property.
     *
     * @return The current song property.
     */
    public StringProperty getCurrentSongStringProperty() {
        return audioPlayer.getCurrentSongStringProperty();
    }

    /**
     * Get the loaded song.
     *
     * @return The loaded song.
     */
    public Song getLoadedSong() {
        return audioPlayer.getLoadedSong();
    }

    /**
     * Seek to a specific progress in the song.
     *
     * @param progress The progress to seek to.
     */
    public void seek(double progress) {
        if (mediaPlayer != null) {
            mediaPlayer.seek(mediaPlayer.getTotalDuration().multiply(progress));
            if (audioPlayer.isTransitioning()) {
                audioPlayer.setTransitioning(false);
                if (transitionPlayer != null) {
                    transitionPlayer.stop();
                    transitionPlayer = null;
                }
            }
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
        if (transitionPlayer != null) {
            transitionPlayer.dispose();
            transitionPlayer = null;
        }
    }

    /**
     * Get the volume property.
     *
     * @return The volume property.
     */
    public DoubleProperty getVolumeProperty() {
        return audioPlayer.getVolumeProperty();
    }

    /**
     * Set the balance of the AudioPlayer.
     *
     * @param newBalance The balance of the AudioPlayer.
     */
    public void setBalance(double newBalance) {
        audioPlayer.setBalance(newBalance);
        if (mediaPlayer != null) {
            mediaPlayer.setBalance(newBalance);
        }
    }

    /**
     * Set the duration of the crossfade.
     *
     * @param _crossfadeDuration The duration of the crossfade in seconds.
     */
    public void setCrossfadeDuration(double _crossfadeDuration) {
        audioPlayer.setCrossfadeDuration(_crossfadeDuration);
    }
}

