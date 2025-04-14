package musicApp.services;

import musicApp.models.Library;
import musicApp.models.Settings;
import musicApp.models.Song;
import musicApp.repositories.JsonRepository;
import musicApp.repositories.PathRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PlaylistService {
    JsonRepository jsonRepository;

    public PlaylistService() {
        jsonRepository = new JsonRepository();
    }

    public void writePlaylists(List<Library> playlists) {
        jsonRepository.writePlaylists(playlists);
    }

    /**
     * Returns all the playlists excluding the main library.
     * @return A list of playlists.
     */
    public List<Library> readPlaylists() {
        return jsonRepository.readPlaylists();
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
        SettingsService settingsService = new SettingsService();
        Library mainLibrary = loadMainLibrary(settingsService.readSettings().getMusicFolder());
        List<Library> playlists = readPlaylists();
        List<Library> libraries = new ArrayList<>();
        libraries.add(mainLibrary);
        libraries.addAll(playlists);
        return libraries;
    }

    /**
     * Loads the main library from the specified music directory.
     *
     * <p>This method attempts to load all songs from the provided music directory using the {@link PathRepository}.
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
            PathRepository loader = new PathRepository();
            List<Song> songs = loader.getAllSongs(musicDirectory);
            return new Library(songs, "??library??", null);
        } catch (IOException e) {
            System.err.println("Failed to load main library: " + e.getMessage());
            return new Library(new ArrayList<>(), "??library??", null);
        }
    }

    public Path addSongToMainLibrary(File song) throws IOException {
        PathRepository loader = new PathRepository();
        JsonRepository jsonRepository = new JsonRepository();
        return loader.copyFileToDirectory(song, jsonRepository.getDefaultMusicFolder());
    }
}
