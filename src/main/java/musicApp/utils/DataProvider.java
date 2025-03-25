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
        String configFileName = "musicapp.conf";
        if (os.contains("win")) {
            this.settingFile = Path.of(System.getenv("APPDATA"), configFileName);
        } else if (os.contains("mac")) {
            this.settingFile = Path.of(System.getProperty("user.home"), "Library", "Application Support", configFileName);
        } else {
            this.settingFile = Path.of(System.getProperty("user.home"), ".config", configFileName);
        }
    }

    /**
     * Returns the default music directory based on the operating system.
     * If the default music directory does not exist, it will be created.
     *
     * @return The default music directory.
     */
    public static Path getDefaultMusicFolder() {
        String os = System.getProperty("os.name").toLowerCase();
        Path defaultMusicFolder;
        Path fallbackMusicFolder;
        String musicFolderName = "Music";
        String fallbackMusicFolderName = "musicApp";

        if (os.contains("win")) {
            defaultMusicFolder = Path.of(System.getenv("USERPROFILE"), musicFolderName);
            fallbackMusicFolder = Path.of(System.getenv("USERPROFILE"), fallbackMusicFolderName);
        } else if (os.contains("mac")) {
            defaultMusicFolder = Path.of(System.getProperty("user.home"), musicFolderName);
            fallbackMusicFolder = Path.of(System.getProperty("user.home"), fallbackMusicFolderName);
        } else {
            try {
                Process process = new ProcessBuilder("xdg-user-dir", "MUSIC").start();
                defaultMusicFolder = Path.of(new String(process.getInputStream().readAllBytes()).trim());
            } catch (IOException e) {
                defaultMusicFolder = Path.of(System.getProperty("user.home"), musicFolderName);
            }
            fallbackMusicFolder = Path.of(System.getProperty("user.home"), fallbackMusicFolderName);
        }
        if (Files.exists(defaultMusicFolder)) {
            return defaultMusicFolder;
        }

        if (!Files.exists(fallbackMusicFolder)) {
            try {
                Files.createDirectories(fallbackMusicFolder);
            } catch (IOException e) {
                System.out.println("An error occurred while creating the default music directory");
            }
        }
        return fallbackMusicFolder;
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
        String settingsBytes = readFileBytes(settingFile);
        if (settingsBytes == null) {
            Settings defaultSettings = new Settings(0.0, getDefaultMusicFolder());
            writeSettings(defaultSettings);
            return defaultSettings;
        }
        return new Settings(settingsBytes);
    }

    public String readFileBytes(Path path) {
        try {
            return new String(Files.readAllBytes(path));
        } catch (IOException e) {
            return null;
        }
    }
}
