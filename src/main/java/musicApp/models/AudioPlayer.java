package musicApp.models;

import javafx.beans.property.*;
import javafx.scene.control.Alert;
import javafx.scene.media.*;
import javafx.util.Duration;
import musicApp.exceptions.BadSongException;
import musicApp.exceptions.EqualizerGainException;
import musicApp.services.AlertService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * AudioPlayer
 * Class that manages the audio playing.
 */
public class AudioPlayer {

    private final DoubleProperty progress;
    private final StringProperty currentSongString;
    private final BooleanProperty isPlaying;
    private final BooleanProperty isLoaded;
    private final DoubleProperty volume;
    private List<Double> equalizerBandsGain;
    private MediaPlayer mediaPlayer;
    private Song loadedSong;
    private double balance;
    private double speed;
    private final AudioSpectrumListener audioSpectrumListener;
    private final Double audioSpectrumInterval;

    private static final double DEFAULT_TRANSITION_DURATION = 5;

    private Supplier<Song> nextSongSupplier;

    private AudioClip transitionClip;
    private Boolean isTransitioning;


    public AudioPlayer(AudioSpectrumListener _audioSpectrumListener) {
        // Initialize properties in the constructor
        progress = new SimpleDoubleProperty(0.0);
        currentSongString = new SimpleStringProperty("None");
        isPlaying = new SimpleBooleanProperty(false);
        isLoaded = new SimpleBooleanProperty(false);
        volume = new SimpleDoubleProperty(1.0);
        equalizerBandsGain = new ArrayList<>(Collections.nCopies(10, 0.0));
        mediaPlayer = null;
        loadedSong = null;
        isTransitioning = false;
        balance = 0.0;
        speed = 1.0;
        audioSpectrumListener = _audioSpectrumListener;
        audioSpectrumInterval = 0.05; // Optimal Speed (lower is blinky, higher is laggy)
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
        loadedSong = song;
        Media media = new Media(song.getSource());
        mediaPlayer = new MediaPlayer(media);

        currentSongString.set(song.getSource());
        mediaPlayer.volumeProperty().bind(volume);
        mediaPlayer.setBalance(balance);
        mediaPlayer.setRate(speed);
        mediaPlayer.setAudioSpectrumListener(audioSpectrumListener);
        mediaPlayer.setAudioSpectrumInterval(audioSpectrumInterval);
        mediaPlayer.setOnReady(() -> {
            isLoaded.set(true);
            try {
                applyEqualizerBandsGain();
            } catch (EqualizerGainException e) {
                AlertService alertService = new AlertService();
                alertService.showExceptionAlert(e, Alert.AlertType.ERROR);
            }
        });
        if (isTransitioning) {
            mediaPlayer.setOnReady(() -> {
                mediaPlayer.seek(Duration.seconds(DEFAULT_TRANSITION_DURATION));
                isTransitioning = false;
                transitionClip.stop();
                transitionClip = null;
            });
        }
        // Update the progression property while playing
        setupProgressListener();
        setTransition();
    }

    private void setTransition() {
        mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            Duration totalDuration = mediaPlayer.getTotalDuration();
            if (totalDuration != null) {
                double remainingTime = totalDuration.toSeconds() - newValue.toSeconds();
                if (remainingTime <= DEFAULT_TRANSITION_DURATION && Boolean.FALSE.equals(isTransitioning)) {
                    isTransitioning = true;
                    if (transitionClip == null) {
                        System.out.println(nextSongSupplier.get());
                        transitionClip = new AudioClip(nextSongSupplier.get().getFilePath().toUri().toString());
                    }
                    transitionClip.play();
                }
                if (Boolean.TRUE.equals(isTransitioning)) {
                    //volume.set(volume.get() * (remainingTime / (DEFAULT_TRANSITION_DURATION)));
                    double volumeValue = volume.get() * ((DEFAULT_TRANSITION_DURATION - remainingTime) / DEFAULT_TRANSITION_DURATION);
                    System.out.println(volumeValue);
                    transitionClip.setVolume(volumeValue);
                }
            }
        });
    }

    public void setNextSongSupplier(Supplier<Song> _nextSongSupplier)  {
        nextSongSupplier = _nextSongSupplier;
    }

    /**
     * Set up a listener to update the progress property.
     */
    private void setupProgressListener() {
        mediaPlayer.currentTimeProperty().addListener((_, _, newTime) -> {
            if (mediaPlayer.getTotalDuration().greaterThan(Duration.ZERO)) {
                progress.set(newTime.toSeconds() / mediaPlayer.getTotalDuration().toSeconds());
            } else {
                progress.set(0.0);
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
        for (int bandIndex = 0; bandIndex < equalizerBandsGain.size(); bandIndex++) {
            EqualizerBand bandToSet = audioEqualizer.getBands().get(bandIndex);
            double gain = equalizerBandsGain.get(bandIndex);
            bandToSet.setGain(gain);
        }
    }

    public List<Double> getEqualizerBandsGain() {
        return equalizerBandsGain;
    }

    /**
     * Update the equalizer bands gain.
     * @param newEqualizerBandsGain The new equalizer bands gain.
     */
    public void updateEqualizerBandsGain(List<Double> newEqualizerBandsGain) throws EqualizerGainException {
        equalizerBandsGain = newEqualizerBandsGain;
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
    public void changeSpeed(double newSpeed) {
        speed = newSpeed;
        if (mediaPlayer != null) {
            mediaPlayer.setRate(speed);
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
        return progress;
    }

    /**
     * Check if the song is playing.
     *
     * @return True if the song is playing, False otherwise.
     */
    public BooleanProperty getIsPlayingProperty() {
        return isPlaying;
    }

    /**
     * Get the current song property.
     *
     * @return The current song property.
     */
    public StringProperty getCurrentSongStringProperty() {
        return currentSongString;
    }

    /**
     * Get the loaded song.
     *
     * @return The loaded song.
     */
    public Song getLoadedSong() {
        return loadedSong;
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
    public DoubleProperty getVolumeProperty() {
        return volume;
    }

    /**
     * Set the balance of the AudioPlayer.
     *
     * @param newBalance The balance of the AudioPlayer.
     */
    public void setBalance(double newBalance) {
        balance = newBalance;
        if (mediaPlayer != null) {
            mediaPlayer.setBalance(balance);
        }
    }
}
