package musicApp.utils;

import musicApp.models.Settings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


/**
 * This class is responsible for reading and writing the settings of the application.
 * The settings are stored in:
 * - Mac: ~/Library/Application Support/musicapp
 * - Windows: %APPDATA%/musicapp
 * - Linux: ~/.config/musicapp
 * <p>
 * The settings are stored in the following format:
 * balance=0.0
 * musicFolder=/path/to/music/folder
 * <p>
 * The default music folder is the user's music folder.
 * If the settings file does not exist, it will be created with the default settings.
 */
public class DataProvider {
    private final Path settingFile;

    /**
     * Constructor
     */
    public DataProvider() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            this.settingFile = Path.of(System.getenv("APPDATA"), "musicapp");
        } else if (os.contains("mac")) {
            this.settingFile = Path.of(System.getProperty("user.home"), "Library", "Application Support", "musicapp");
        } else {
            this.settingFile = Path.of(System.getProperty("user.home"), ".config", "musicapp");
        }
    }

    /**
     * Returns the default music directory based on the operating system.
     * If the default music directory does not exist, it will be created.
     *
     * @return The default music directory.
     */
    public static Path getDefaultMusicDir() {
        String os = System.getProperty("os.name").toLowerCase();
        Path defaultMusicDir;
        Path fallbackMusicDir;

        if (os.contains("win")) {
            defaultMusicDir = Path.of(System.getenv("USERPROFILE"), "Music");
            fallbackMusicDir = Path.of(System.getenv("USERPROFILE"), "MusicApp");
        } else if (os.contains("mac")) {
            defaultMusicDir = Path.of(System.getProperty("user.home"), "Music");
            fallbackMusicDir = Path.of(System.getProperty("user.home"), "MusicApp");
        } else {
            try {
                Process process = new ProcessBuilder("xdg-user-dir", "MUSIC").start();
                defaultMusicDir = Path.of(new String(process.getInputStream().readAllBytes()).trim());
            } catch (IOException e) {
                defaultMusicDir = Path.of(System.getProperty("user.home"), "Music");
            }
            fallbackMusicDir = Path.of(System.getProperty("user.home"), "MusicApp");
        }
        if (Files.exists(defaultMusicDir)) {
            return defaultMusicDir;
        }

        if (!Files.exists(fallbackMusicDir)) {
            try {
                Files.createDirectories(fallbackMusicDir);
            } catch (IOException e) {
                System.out.println("An error occurred while creating the default music directory");
            }
        }
        return fallbackMusicDir;
    }

    /**
     * Writes the settings to the settings file.
     *
     * @param settings The settings to write.
     */
    public void writeSettings(Settings settings) {
        try {
            java.io.FileWriter writer = new java.io.FileWriter(this.settingFile.toString());
            writer.write(settings.toString());
            writer.close();
        } catch (Exception e) {
            System.out.println("An error occurred while writing the settings file");
        }
    }

    /**
     * Reads the settings from the settings file.
     * If the settings file does not exist, it will be created with the default settings.
     *
     * @return The settings read from the settings file.
     * @throws IOException If an error occurs while reading the settings file.
     */
    public Settings readSettings() throws IOException {
        if (!Files.exists(settingFile)) {
            writeSettings(new Settings(0.0, getDefaultMusicDir()));
            return new Settings(0.0, getDefaultMusicDir());
        }
        return Settings.fromString(new String(Files.readAllBytes(settingFile)));
    }
}
