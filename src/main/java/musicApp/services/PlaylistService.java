package musicApp.services;

import musicApp.exceptions.SettingsFilesException;
import musicApp.models.Library;
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

    public PlaylistService(Path playlistsPath) throws SettingsFilesException {
        jsonRepository = new JsonRepository();
        jsonRepository.setPlaylistsPath(playlistsPath);
    }

    public void writePlaylists(List<Library> playlists) {
        jsonRepository.writePlaylists(playlists);
    }

    /**
     * Returns all the playlists excluding the main library.
     *
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
     * The main library is loaded first, followed by any existing playlists.
     */
    public List<Library> loadAllLibraries(Path musicFolder, Path userMusicFolder) {
        Library mainLibrary = loadMainLibrary(musicFolder);
        Library userMainLibrary = loadUserMainLibrary(userMusicFolder);
        List<Library> playlists = readPlaylists();
        List<Library> libraries = new ArrayList<>();
        libraries.add(mainLibrary);
        libraries.add(userMainLibrary);
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
     * if loading fails due to an IOException.
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

    public Library loadUserMainLibrary(Path userMusicFolder) {
        try {
            PathRepository loader = new PathRepository();
            List<Song> songs = loader.getAllSongs(userMusicFolder);
            return new Library(songs, "??user_library??", null);
        } catch (IOException e) {
            System.err.println("Failed to load user library: " + e.getMessage());
            return new Library(new ArrayList<>(), "??user_library??", null);
        }
    }

    public Path addSongToLibrary(File song, Path libraryPath) throws IOException {
        PathRepository loader = new PathRepository();
        return loader.copyFileToDirectory(song, libraryPath);
    }

    public void setPlaylistsPath(Path userPlaylistPath) {
        jsonRepository.setPlaylistsPath(userPlaylistPath);
    }
}
