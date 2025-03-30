package musicApp.models;

import com.google.gson.annotations.Expose;
import musicApp.utils.DataProvider;

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
    private Equalizer equalizer = new Equalizer();

    /**
     * Constructor
     *
     * @param balance     The balance of the application.
     * @param musicFolder The path to the music folder.
     */
    public Settings(double balance, Path musicFolder, Equalizer equalizer) {
        this.balance = balance;
        this.musicFolder = musicFolder;
        this.equalizer = equalizer;
    }

    /**
     * Parses a settings string to a Settings object.
     * The settings string must be in the following format:
     * balance=0.0
     * musicFolder=/path/to/music/folder
     * if the settings string is null or empty, an IllegalArgumentException is thrown.
     * if the settings string does not contain one of the required fields, it will take the default value.
     *
     * @param settings The settings string.
     */
    public Settings(String settings) throws IllegalArgumentException {
        this.balance = 0.0;
        DataProvider dataProvider = new DataProvider();
        this.musicFolder = dataProvider.getDefaultMusicFolder();

        if (settings == null || settings.isEmpty()) {
            System.err.println("Settings string is null or empty");
        }
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
     * @param balance The balance.
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * Get the music folder of the settings.
     *
     * @return The music folder.
     */
    public Path getMusicDirectory() {
        return this.musicFolder;
    }

    /**
     * Set the music folder of the settings.
     *
     * @param musicFolder The music folder.
     */
    public void setMusicFolder(Path musicFolder) {
        this.musicFolder = musicFolder;
    }

    public Equalizer getEqualizer() {
        return this.equalizer;
    }

    public List<Double> getEqualizerBands() {
        return this.equalizer.getBandsGain();
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Settings settings)) {
            return false;
        }
        return Double.compare(settings.getBalance(), this.getBalance()) == 0 &&
                settings.getMusicDirectory().equals(this.getMusicDirectory()) &&
                settings.getEqualizerBands().equals(this.getEqualizerBands());
    }

    @Override
    public String toString() {
        return "balance=" + balance + "\n" +
                "musicFolder=" + musicFolder.toString() + "\n" +
                "equalizerBands=" + equalizer.toString();
    }
}