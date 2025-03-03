package MusicApp.Models;

import java.nio.file.Path;

public class Settings {
    private double balance;
    private Path musicDirectory;

    public Settings(double balance, Path musicFolder) {
        this.balance = balance;
        this.musicDirectory = musicFolder;
    }

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

    public double getBalance() {
        return balance;
    }

    public Path getMusicDirectory() {
        return this.musicDirectory;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

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
