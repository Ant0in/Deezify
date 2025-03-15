package MusicApp.Models;

import MusicApp.Utils.DataProvider;

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
     * if the settings string is null or empty, an IllegalArgumentException is thrown.
     * if the settings string does not contain one of the required fields, it will take the default value.
     * @param settings The settings string.
     * @return The Settings object.
     */
    public static Settings fromString(String settings) throws IllegalArgumentException {
        double balance = 0.0;
        Path musicFolder = DataProvider.getDefaultMusicDir();
        if (settings == null || settings.trim().isEmpty()) {
            throw new IllegalArgumentException("Settings string cannot be null or empty");
        }

        String[] lines = settings.split("\n");

        try {
            for (String line : lines) {
                String[] parts = line.split("=");
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Invalid settings format");
                }
                switch (parts[0]) {
                    case "balance":
                        balance = Double.parseDouble(parts[1]);
                        break;
                    case "musicFolder":
                        musicFolder = Path.of(parts[1]);
                        break;
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid settings format", e);
        }
        return new Settings(balance, musicFolder);
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
        if (!(obj instanceof Settings settings)) {
            return false;
        }
        return settings.getBalance() == this.getBalance() && settings.getMusicDirectory().equals(this.getMusicDirectory());
    }

    @Override
    public String toString() {
        return "balance=" + balance + "\nmusicFolder=" + musicDirectory.toString();
    }
}
