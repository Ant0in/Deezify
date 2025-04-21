package musicApp.models;

import com.google.gson.annotations.Expose;

import java.nio.file.Path;
import java.util.List;

/**
 * Settings class to store the balance and music folder.
 */
public class Settings {
    @Expose
    private double balance;
    @Expose
    private Path musicFolder;
    private final Equalizer equalizer;
    private boolean musicFolderChanged;
    
    /**
     * Constructor for Settings.
     *
     * @param _balance      The balance of the settings.
     * @param _musicFolder  The music folder of the settings.
     * @param _equalizer    The equalizer of the settings.
     */
    public Settings(double _balance, Path _musicFolder, Equalizer _equalizer) {
        equalizer = _equalizer;
        musicFolderChanged = false;
        balance = _balance;
        musicFolder = _musicFolder;
    }  

    /**
     * Get the balance of the settings.
     *
     * @return The balance.
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Set the balance of the settings.
     *
     * @param newBalance The balance.
     */
    public void setBalance(double newBalance) {
        balance = newBalance;
    }

    /**
     * Get the music folder of the settings.
     *
     * @return The music folder.
     */
    public Path getMusicFolder() {
        return musicFolder;
    }

    /**
     * Get the music folder of the settings as a string.
     *
     * @return The music folder as a string.
     */
    public String getMusicFolderString() {
        return musicFolder.toString();
    }

    /**
     * Set the music folder of the settings.
     *
     * @param newMusicFolder The music folder.
     */
    public void setMusicFolder(Path newMusicFolder) {
        musicFolder = newMusicFolder;
        musicFolderChanged = true;
    }

    /**
     * Get the equalizer of the settings.
     * @return The equalizer.
     */
    public Equalizer getEqualizer() {
        return equalizer;
    }

    /**
     * Get the equalizer bands of the settings.
     * @return The equalizer bands.
     */
    public List<Double> getEqualizerBands() {
        return equalizer.getBandsGain();
    }

    /**
     * Checks if the music folder has changed.
     * @return True if the music folder has changed, false otherwise.
     */
    public boolean isMusicFolderChanged() {
        return musicFolderChanged;
    }

    /**
     * Checks if the settings object is equal to another settings object.
     * @param obj The object to compare with.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Settings settings)) {
            return false;
        }
        return Double.compare(settings.getBalance(), getBalance()) == 0 &&
                settings.getMusicFolder().equals(getMusicFolder()) &&
                settings.getEqualizerBands().equals(getEqualizerBands());
    }

    /**
     * Returns a string representation of the settings object.
     * @return A string representation of the settings object.
     */
    @Override
    public String toString() {
        return "balance=" + balance + "\n" +
                "musicFolder=" + musicFolder.toString() + "\n" +
                "equalizerBands=" + equalizer.toString();
    }
}