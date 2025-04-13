package musicApp.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import musicApp.models.Equalizer;
import musicApp.models.Library;
import musicApp.models.Settings;
import musicApp.models.Song;
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
        String configFolder = "Deezify";
        if (os.contains("win")) {
            settingFolder = Path.of(System.getenv("APPDATA"), configFolder);
        } else if (os.contains("mac")) {
            settingFolder = Path.of(System.getProperty("user.home"), "Library", "Application Support", configFolder);
        } else {
            settingFolder = Path.of(System.getProperty("user.home"), ".config", configFolder);
        }
        createFolderIfNotExists(settingFolder);
        settingsFile = settingFolder.resolve("settings.json");
        playlistsFile = settingFolder.resolve("playlists.json");
    }
    /**
     * Creates a folder at the specified path if it does not already exist.
     *
     * <p>If the folder already exists, no action is taken. If an error occurs during the folder creation,
     * an error message will be printed to the console.</p>
     *
     * @param folder The path of the folder to create.
     */
    public void createFolderIfNotExists(Path folder) {
        if (!Files.exists(folder)) {
            try {
                Files.createDirectories(folder);
            } catch (IOException e) {
                System.out.println("An error occurred while creating the folder");
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
    /**
     * Retrieves the settings from a JSON file located at the specified path.
     *
     * <p>The file is parsed into a {@link Settings} object using Gson. If an error occurs during the file reading
     * or parsing (e.g., due to invalid JSON syntax, IO issues), an error message is logged, and default settings
     * are returned instead.</p>
     *
     * @param path The path to the JSON settings file.
     * @return A {@link Settings} object populated with the data from the file, or default settings if an error occurs.
     */
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
    private List<Library> readPlaylists() throws IllegalArgumentException {
        if (!Files.exists(playlistsFile)) {
            writePlaylists(List.of());
        }
        return getPlaylists(playlistsFile);
    }

    /**
     * Loads the main library from the specified music directory.
     *
     * <p>This method attempts to load all songs from the provided music directory using the {@link MusicLoader}.
     * If successful, it returns a new {@link Library} object containing all the songs. In case of any
     * {@link IOException}, an empty library with a default name ("??library??") is returned as a fallback.</p>
     *
     * @param musicDirectory The directory from which to load the songs. This is usually the user's default
     *                       music folder.
     * @return A {@link Library} containing all songs from the specified directory, or an empty library
     *         if loading fails due to an IOException.
     */
    public Library loadMainLibrary(Path musicDirectory) {
        try {
            MusicLoader loader = new MusicLoader();
            List<Song> songs = loader.getAllSongs(musicDirectory);
            return new Library(songs, "??library??", null);
        } catch (IOException e) {
            System.err.println("Failed to load main library: " + e.getMessage());
            return new Library(new ArrayList<>(), "??library??", null);
        }
    }

    /**
     * Loads all libraries, starting with the main library, followed by the user-defined playlists.
     *
     * <p>This method first loads the main library, which contains all songs available in the default music folder.</p>
     * <p>Then, it loads the playlists from the playlists file, if available, and combines both the main library and the playlists into a single list.</p>
     *
     * @return A list containing the main library followed by the playlists.
     *         The main library is loaded first, followed by any existing playlists.
     */
    public List<Library> loadAllLibraries(){
        Library mainLibrary = loadMainLibrary(readSettings().getMusicFolder());
        List<Library> playlists = readPlaylists();
        List<Library> libraries = new ArrayList<>();
        libraries.add(mainLibrary);
        libraries.addAll(playlists);
        return libraries;
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

    /**
     * Checks and ensures that a playlist with the name "??favorites??" exists in the provided list of playlists.
     *
     * <p>If the list of playlists is {@code null} or does not contain a playlist named "??favorites??", a new playlist
     * with this name is added to the beginning of the list. The updated list is then written back to storage.</p>
     *
     * @param playlists The list of playlists to check.
     * @return The updated list of playlists, including the "??favorites??" playlist if necessary.
     */
    private List<Library> checkPlaylists(List<Library> playlists) {
        List<Library> validPlaylists = playlists != null ? new ArrayList<>(playlists) : new ArrayList<>();

        if (validPlaylists.isEmpty() || !validPlaylists.getFirst().getName().equals("??favorites??")) {
            Library favorites = new Library(new ArrayList<>(), "??favorites??", null);
            validPlaylists.addFirst(favorites);
        }

        writePlaylists(validPlaylists);
        return validPlaylists;
    }

    /**
     * Returns the path to the directory where lyrics are stored.
     *
     * <p>The path is constructed by resolving the "lyrics" subdirectory within the settings folder.</p>
     *
     * @return The path to the lyrics directory.
     */
    public Path getLyricsDir() {
        return settingFolder.resolve("lyrics");
    }

}