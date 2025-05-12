package musicApp.models;

import javafx.beans.property.*;
import musicApp.exceptions.BadSongException;

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
    private Song loadedSong;
    private double balance;
    private double crossfadeDuration;
    private double speed;
    private final Double audioSpectrumInterval;


    private Supplier<Song> nextSongSupplier;

    private Boolean isTransitioning;


    public AudioPlayer() {
        // Initialize properties in the constructor
        progress = new SimpleDoubleProperty(0.0);
        currentSongString = new SimpleStringProperty("None");
        isPlaying = new SimpleBooleanProperty(false);
        isLoaded = new SimpleBooleanProperty(false);
        volume = new SimpleDoubleProperty(1.0);
        equalizerBandsGain = new ArrayList<>(Collections.nCopies(10, 0.0));
        loadedSong = null;
        isTransitioning = false;
        balance = 0.0;
        speed = 1.0;
        audioSpectrumInterval = 0.05; // Optimal Speed (lower is blinky, higher is laggy)
    }

    /**
     * Load a song into the player.
     *
     * @param song The song to load.
     */
    public void loadSong(Song song) throws BadSongException {
        loadedSong = song;
        currentSongString.set(song.getSource());
    }

    /**
     * Set the next song supplier.
     *
     * @param _nextSongSupplier The next song supplier, which is a function that returns a Song.
     */
    public void setNextSongSupplier(Supplier<Song> _nextSongSupplier)  {
        nextSongSupplier = _nextSongSupplier;
    }

    /**
     * Returns the list of equalizer band gains.
     *
     * @return list of gains for each equalizer band
     */
    public List<Double> getEqualizerBandsGain() {
        return equalizerBandsGain;
    }

    /**
     * Change speed of the loaded song.
     */
    public void setSpeed(double newSpeed) {
        speed = newSpeed;
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
    }

    /**
     * Set the duration of the crossfade.
     *
     * @param _crossfadeDuration The duration of the crossfade in seconds.
     */
    public void setCrossfadeDuration(double _crossfadeDuration) {
        crossfadeDuration = _crossfadeDuration;
    }

    /**
     * Gets the current balance setting.
     *
     * @return balance value
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Retrieves the current playback speed multiplier.
     *
     * @return speed multiplier
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Gets the interval for audio spectrum analysis.
     *
     * @return spectrum interval in seconds
     */
    public double getAudioSpectrumInterval() {
        return audioSpectrumInterval;
    }

    /**
     * Marks the player state as loaded.
     */
    public void setLoaded() {
        isLoaded.set(true);
    }

    /**
     * Returns the number of equalizer bands configured.
     *
     * @return count of bands
     */
    public int getEqualizerBandsGainSize() {
        return equalizerBandsGain.size();
    }

    /**
     * Retrieves the gain value for a specific equalizer band.
     *
     * @param bandIndex index of the band (0-based)
     * @return gain value of that band
     */
    public double getEqualizerBandGain(int bandIndex) {
        return equalizerBandsGain.get(bandIndex);
    }

    /**
     * Checks if a transition is currently in progress.
     *
     * @return true if transitioning, false otherwise
     */
    public boolean isTransitioning() {
        return isTransitioning;
    }

    /**
     * Sets the transitioning flag to manage crossfade state.
     *
     * @param transitioning new transitioning state
     */
    public void setTransitioning(boolean transitioning) {
        isTransitioning = transitioning;
    }

    /**
     * Gets the configured crossfade duration.
     *
     * @return crossfade duration in seconds
     */
    public double getCrossfadeDuration() {
        return crossfadeDuration;
    }

    /**
     * Retrieves the next song from the supplier.
     *
     * @return next Song instance
     */
    public Song getNextSongSupplier() {
        return nextSongSupplier.get();
    }

    /**
     * Updates the playback progress value.
     *
     * @param newProgress progress ratio (0.0 to 1.0)
     */
    public void setProgress(double newProgress) {
        progress.set(newProgress);
    }

    /**
     * Replaces the entire list of equalizer band gains.
     *
     * @param newEqualizerBandsGain list of new gain values
     */
    public void setEqualizerBandsGain(List<Double> newEqualizerBandsGain) {
        equalizerBandsGain = newEqualizerBandsGain;
    }

    /**
     * Sets the playing flag to update playback status.
     *
     * @param playing new playing state
     */
    public void setPlaying(boolean playing) {
        isPlaying.set(playing);
    }
}
