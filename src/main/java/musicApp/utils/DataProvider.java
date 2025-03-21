package musicApp.utils;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import musicApp.models.Playlist;
import musicApp.models.Settings;
import musicApp.utils.gsonTypeAdapter.PathTypeAdapter;
import musicApp.utils.gsonTypeAdapter.PlaylistTypeAdapter;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
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
        this.settingsFile = settingFolder.resolve("settings.conf");
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
     * Returns the default music directory based on the operating system.
     * If the default music directory does not exist, it will be created.
     *
     * @return The default music directory.
     */
    public Path getDefaultMusicFolder() {
        String os = System.getProperty("os.name").toLowerCase();
        Path defaultMusicFolder;
        Path fallbackMusicFolder;
        String musicFolderName = "Music";
        String fallbackMusicFolderName = "MusicApp";

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

        createFolderIfNotExists(fallbackMusicFolder);
        return fallbackMusicFolder;
    }

    /**
     * Writes the settings to the settings file.
     *
     * @param settings The settings to write.
     */
    public void writeSettings(Settings settings) {
        try {
            java.io.FileWriter writer = new java.io.FileWriter(this.settingsFile.toString());
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
        String settingsBytes = readFileBytes(settingsFile);
        if (settingsBytes == null) {
            Settings defaultSettings = new Settings(0.0, getDefaultMusicFolder());
            writeSettings(defaultSettings);
            return defaultSettings;
        }
        return new Settings(settingsBytes);
    }

    /**
     * Reads the bytes from a file.
     *
     * @param path The path to read the bytes from.
     * @return The bytes read from the file.
     */
    public String readFileBytes(Path path) {
        try {
            return new String(Files.readAllBytes(path));
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Reads the playlists from the playlists file.
     * If the playlists file does not exist, it will be created with an empty list of playlists.
     *
     * @return The playlists read from the playlists file.
     * @throws IllegalArgumentException If an error occurs while reading the playlists file.
     */
    public List<Playlist> readPlaylists() throws IllegalArgumentException {
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
    protected List<Playlist> getPlaylists(Path path) throws IllegalArgumentException {
        try (FileReader reader = new FileReader(path.toFile())) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Path.class, new PathTypeAdapter())
                    .registerTypeAdapter(Playlist.class, new PlaylistTypeAdapter())
                    .serializeNulls()
                    .create();
            Type playlistListType = new TypeToken<List<Playlist>>(){}.getType();
            List<Playlist> playlists = gson.fromJson(reader, playlistListType);
            playlists.forEach(this::checkValidPlaylist);
            return playlists;
        } catch (JsonIOException | JsonSyntaxException | IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Writes the playlists to the playlists file.
     *
     * @param playlists The playlists to write.
     */
    public void writePlaylists(List<Playlist> playlists) {
        try (java.io.FileWriter writer = new java.io.FileWriter(playlistsFile.toString())) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Path.class, new PathTypeAdapter())
                    .registerTypeAdapter(Playlist.class, new PlaylistTypeAdapter())
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
    private void checkValidPlaylist(Playlist playlist) throws IllegalArgumentException {
        if (playlist.getName() == null || playlist.getName().isEmpty()) {
            throw new IllegalArgumentException("Playlist name cannot be empty");
        }
        if (playlist.toList() == null) {
            throw new IllegalArgumentException("Playlist song list cannot be null");
        }
    }
}
