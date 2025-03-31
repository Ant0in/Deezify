package musicApp.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import musicApp.models.Equalizer;
import musicApp.models.Library;
import musicApp.models.Settings;
import musicApp.utils.gsonTypeAdapter.LibraryTypeAdapter;
import musicApp.utils.gsonTypeAdapter.SettingsTypeAdapter;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


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
    private final Path settingFolder;
    private final Path settingsFile;
    private final Path playlistsFile;

    /**
     * Constructor
     */
    public DataProvider() {
        String os = System.getProperty("os.name").toLowerCase();
        String configFolder = "musicapp";
        if (os.contains("win")) {
            this.settingFolder = Path.of(System.getenv("APPDATA"), configFolder);
        } else if (os.contains("mac")) {
            this.settingFolder = Path.of(System.getProperty("user.home"), "Library", "Application Support", configFolder);
        } else {
            this.settingFolder = Path.of(System.getProperty("user.home"), ".config", configFolder);
        }
        createFolderIfNotExists(settingFolder);
        this.settingsFile = settingFolder.resolve("settings.json");
        this.playlistsFile = settingFolder.resolve("playlists.json");
    }

    private void createFolderIfNotExists(Path folder) {
        if (!Files.exists(folder)) {
            try {
                Files.createDirectories(folder);
            } catch (IOException e) {
                System.out.println("An error occurred while creating the settings folder");
            }
        }
    }

    /**
     * Returns the folder path based on the operating system.
     *
     * @param folderName The name of the folder.
     * @return The folder path.
     */
    private Path getFolderByOS(String folderName) {
        Path folderPath;
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            folderPath = Path.of(System.getenv("USERPROFILE"), folderName);
        } else if (os.contains("mac")) {
            folderPath = Path.of(System.getProperty("user.home"), folderName);
        } else {
            folderPath = Path.of(System.getProperty("user.home"), folderName);
        }
        return folderPath;
    }

    /**
     * Returns the default music directory based on the operating system.
     * If the default music directory does not exist, it will be created.
     *
     * @return The default music directory.
     */
    public Path getDefaultMusicFolder() {
        Path musicFolder = getFolderByOS("Music");
        Path backupMusicFolder = getFolderByOS("MusicApp");

        if (Files.exists(musicFolder)) {
            return musicFolder;
        }

        createFolderIfNotExists(backupMusicFolder);
        return backupMusicFolder;
    }

    /**
     * Writes the settings to the settings file.
     *
     * @param settings The settings to write.
     */
    public void writeSettings(Settings settings) {
        try (java.io.FileWriter writer = new java.io.FileWriter(settingsFile.toString())) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Settings.class, new SettingsTypeAdapter())
                    .serializeNulls()
                    .create();
            gson.toJson(settings, writer);
        } catch (IOException e) {
            System.err.println("An error occurred while writing the settings file: " + e.getMessage());
        }
    }

    /**
     * Reads the settings from the settings file.
     * If the settings file does not exist, it will be created with the default settings.
     *
     * @return The settings read from the settings file.
     */
    public Settings readSettings() {
        if (!Files.exists(settingsFile)) {
            Settings defaultSettings = new Settings(0, getDefaultMusicFolder(), new Equalizer());
            writeSettings(defaultSettings);
            return defaultSettings;
        }
        return getSettings(settingsFile);
    }

    protected Settings getSettings(Path path) {
        try (FileReader reader = new FileReader(path.toFile())) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Settings.class, new SettingsTypeAdapter())
                    .serializeNulls()
                    .create();
            return gson.fromJson(reader, Settings.class);
        } catch (JsonIOException | JsonSyntaxException | IOException e) {
            System.err.println("An error occurred while reading the settings file: " + e.getMessage());
            return new Settings(0, getDefaultMusicFolder(), new Equalizer());
        }
    }

    /**
     * Reads the playlists from the playlists file.
     * If the playlists file does not exist, it will be created with an empty list of playlists.
     *
     * @return The playlists read from the playlists file.
     * @throws IllegalArgumentException If an error occurs while reading the playlists file.
     */
    public List<Library> readPlaylists() throws IllegalArgumentException {
        if (!Files.exists(playlistsFile)) {
            writePlaylists(List.of());
        }
        return getPlaylists(playlistsFile);
    }

    /**
     * Reads the playlists from the given path.
     * Mainly kept in protected scope for testing purposes.
     *
     * @param path The path to read the playlists from.
     * @return The playlists read from the given path.
     * @throws IllegalArgumentException If an error occurs while reading the playlists from the given path.
     */
    protected List<Library> getPlaylists(Path path) throws IllegalArgumentException {
        try (FileReader reader = new FileReader(path.toFile())) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Library.class, new LibraryTypeAdapter())
                    .serializeNulls()
                    .create();
            Type playlistListType = new TypeToken<List<Library>>() {
            }.getType();
            List<Library> playlists = gson.fromJson(reader, playlistListType);
            playlists.forEach(this::checkValidPlaylist);
            return checkPlaylists(playlists);
        } catch (JsonIOException | JsonSyntaxException | IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Writes the playlists to the playlists file.
     *
     * @param playlists The playlists to write.
     */
    public void writePlaylists(List<Library> playlists) {
        try (java.io.FileWriter writer = new java.io.FileWriter(playlistsFile.toString())) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Library.class, new LibraryTypeAdapter())
                    .serializeNulls()
                    .create();
            gson.toJson(playlists, writer);
        } catch (IOException e) {
            System.err.println("An error occurred while writing the playlists file");
        }
    }

    /**
     * Check the validity of a playlist.
     *
     * @param playlist The playlist to check.
     * @throws IllegalArgumentException If the playlist is invalid.
     */
    private void checkValidPlaylist(Library playlist) throws IllegalArgumentException {
        if (playlist.getName() == null || playlist.getName().isEmpty()) {
            throw new IllegalArgumentException("Playlist name cannot be empty");
        }
        if (playlist.toList() == null) {
            throw new IllegalArgumentException("Playlist song list cannot be null");
        }
    }

    private List<Library> checkPlaylists(List<Library> playlists) {
        List<Library> validPlaylists = playlists != null ? new ArrayList<>(playlists) : new ArrayList<>();

        if (validPlaylists.isEmpty() || !validPlaylists.getFirst().getName().equals("??favorites??")) {
            Library favorites = new Library(new ArrayList<>(), "??favorites??", null);
            validPlaylists.addFirst(favorites);
        }

        writePlaylists(validPlaylists);
        return validPlaylists;
    }
}
