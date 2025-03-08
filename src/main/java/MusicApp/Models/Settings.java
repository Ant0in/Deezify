package MusicApp.Models;

import java.nio.file.Path;

/**
 * Settings class to store the balance and music folder.
 */
public class Settings {
    private double balance;
    private Path musicDirectory;

    /**
     * Constructor
     * @param balance The balance of the application.
     * @param musicFolder The path to the music folder.
     */
    public Settings(double balance, Path musicFolder) {
        this.balance = balance;
        this.musicDirectory = musicFolder;
    }

    /**
     * Parses a settings string to a Settings object.
     * The settings string must be in the following format:
     * balance=0.0
     * musicFolder=/path/to/music/folder
     *
     * @param settings The settings string.
     * @return The Settings object.
     */
    public static Settings fromString(String settings) {
        if (settings == null || settings.trim().isEmpty()) {
            throw new IllegalArgumentException("Settings string cannot be null or empty");
        }

        String[] lines = settings.split("\n");
        if (lines.length < 2) {
            throw new IllegalArgumentException("Settings string must contain both balance and musicFolder");
        }

        try {
            double balance = Double.parseDouble(lines[0].split("=")[1]);
            Path musicFolder = Path.of(lines[1].split("=")[1]);
            return new Settings(balance, musicFolder);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid settings format", e);
        }
    }

    /**
     * Get the balance of the settings.
     * @return The balance.
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Get the music folder of the settings.
     * @return The music folder.
     */
    public Path getMusicDirectory() {
        return this.musicDirectory;
    }

    /**
     * Set the balance of the settings.
     * @param balance The balance.
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * Set the music folder of the settings.
     * @param musicDirectory The music folder.
     */
    public void setMusicFolder(Path musicDirectory) {
        this.musicDirectory = musicDirectory;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Settings)) {
            return false;
        }
        Settings settings = (Settings) obj;
        return settings.getBalance() == this.getBalance() && settings.getMusicDirectory().equals(this.getMusicDirectory());
    }

    @Override
    public String toString() {
        return "balance=" + balance + "\nmusicFolder=" + musicDirectory.toString();
    }
}
