package MusicApp.models;

import MusicApp.utils.DataProvider;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Settings class to store the balance and music folder.
 */
public class Settings {
    private double balance;
    private Path musicFolder;


    /**
     * Constructor
     *
     * @param balance     The balance of the application.
     * @param musicFolder The path to the music folder.
     */
    public Settings(double balance, Path musicFolder) {
        this.balance = balance;
        this.musicFolder = musicFolder;
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
        this.musicFolder = DataProvider.getDefaultMusicFolder();

        if (settings == null || settings.isEmpty()) {
            System.err.println("Settings string is null or empty");
            return;
        }

        String[] lines = settings.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }

            int equalsPos = line.indexOf('=');
            if (equalsPos <= 0 || equalsPos == line.length() - 1) {
                // Invalid line
                continue;
            }

            String key = line.substring(0, equalsPos);
            String value = line.substring(equalsPos + 1);

            switch (key) {
                case "balance":
                    this.balance = this.parseBalance(value);
                    break;
                case "musicFolder":
                    this.musicFolder = this.parseMusicFolder(value);
                    break;
            }
        }
    }

    /**
     * Parse the balance from a string.
     *
     * @param unparsedBalance The balance as a string.
     * @return The balance as a double.
     */
    private double parseBalance(String unparsedBalance) {
        final double MAX_BALANCE = 1.0;
        final double MIN_BALANCE = -1.0;
        final double DEFAULT_BALANCE = 0.0;
        try {
            double balance = Double.parseDouble(unparsedBalance);
            if (balance < MIN_BALANCE || balance > MAX_BALANCE) {
                System.err.println("Balance must be between " + MIN_BALANCE + " and " + MAX_BALANCE);
                return DEFAULT_BALANCE;
            }
            return balance;
        } catch (NumberFormatException e) {
            System.err.println("Balance must be a number");
            return DEFAULT_BALANCE;
        }
    }

    private Path parseMusicFolder(String unparsedMusicFolder) {
        try {
            Path musicFolder = Path.of(unparsedMusicFolder);
            if (!Files.exists(musicFolder) || !Files.isDirectory(musicFolder)) {
                System.err.println("Music folder does not exist");
                return DataProvider.getDefaultMusicFolder();
            }
            return musicFolder;
        } catch (Exception e) {
            System.err.println("Invalid music folder path");
            return DataProvider.getDefaultMusicFolder();
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
        return "balance=" + balance + "\nmusicFolder=" + musicFolder.toString();
    }
}
