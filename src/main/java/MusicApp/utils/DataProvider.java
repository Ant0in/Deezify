package MusicApp.utils;

import MusicApp.Models.Settings;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class DataProvider {
    private Path settingFile;

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

    private Path getDefaultMusicDir() {
        String os = System.getProperty("os.name").toLowerCase();
        Path defaultMusicDir;

        if (os.contains("win")) {
            defaultMusicDir = Path.of(System.getenv("USERPROFILE"), "Music");
        } else if (os.contains("mac")) {
            defaultMusicDir = Path.of(System.getProperty("user.home"), "Music");
        } else {
            try {
                Process process = Runtime.getRuntime().exec("xdg-user-dir MUSIC");
                defaultMusicDir = Path.of(new String(process.getInputStream().readAllBytes()).trim());
            } catch (IOException e) {
                defaultMusicDir = Path.of(System.getProperty("user.home"), "Music");
            }
        }
        return defaultMusicDir;
    }

    public void writeSettings(Settings settings) {
        try {
            java.io.FileWriter writer = new java.io.FileWriter(this.settingFile.toString());
            writer.write(settings.toString());
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Settings readSettings() throws IOException {
        if (!Files.exists(settingFile)) {
            writeSettings(new Settings(0.0, getDefaultMusicDir()));
            return new Settings(0.0, getDefaultMusicDir());
        }
        return Settings.fromString(new String(Files.readAllBytes(settingFile)));
    }
}
