package musicApp.controllers.playlists;


import musicApp.exceptions.DeletePlaylistException;
import musicApp.models.Library;
import musicApp.models.Song;
import musicApp.services.LanguageService;
import musicApp.services.PlaylistService;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PlaylistsController {
    private final PlaylistService playlistService;
    private final List<Library> playlists;

    private final int MAIN_LIBRARY_INDEX = 0;
    private static final int FAVORITES_INDEX = 1;



    public PlaylistsController(Path musicFolder) {
        playlistService = new PlaylistService();
        playlists = playlistService.loadAllLibraries(musicFolder);
    }


    public void updateMainLibrary(Path newMusicFolder) {
        Library mainLibrary = playlistService.loadMainLibrary(newMusicFolder);
        setMainLibrary(mainLibrary);
    }

    private void setMainLibrary(Library newMainLibrary) {
        playlists.set(MAIN_LIBRARY_INDEX, newMainLibrary);
    }

    public Library getMainLibrary() {
        return playlists.get(MAIN_LIBRARY_INDEX);
    }

    public Library getFavoritesPlaylist() {
        return playlists.get(FAVORITES_INDEX);
    }

    public List<Library> getPlaylists() {
        return playlists;
    }

    public void createPlaylist(String name, Path imagePath) {
        Library playlist = new Library(new ArrayList<>(), name, imagePath);
        playlists.add(playlist);
        writePlaylists();
    }

    public boolean isLibraryModifiable(Library library) {
        return !(playlists.getFirst().equals(library) || playlists.get(FAVORITES_INDEX).equals(library));
    }

    public void deletePlaylist(Library library) throws DeletePlaylistException {
        if (isLibraryModifiable(library)) {
            playlists.remove(library);
            writePlaylists();
        } else {
            throw new DeletePlaylistException(LanguageService.getInstance().get("error.delete_playlist"));
        }
    }

    public void toggleFavorites(Song song) {
        Library favorites = getFavoritesPlaylist();
        if (favorites.toList().contains(song)) {
            favorites.remove(song);
        } else {
            favorites.add(song);
        }
        writePlaylists();
    }

    public void addSongToPlaylist(Song song, Library playlist) {
        playlist.add(song);
        writePlaylists();
    }

    public void removeSongFromPlaylist(Song song, Library playlist) {
        playlist.remove(song);
        writePlaylists();
    }

    /**
     * Saves the playlists ( except for main library)
     */
    private void writePlaylists() {
        playlistService.writePlaylists(playlists.subList(FAVORITES_INDEX, playlists.size()));
    }

    public boolean isFavorite(Song song) {
        return getFavoritesPlaylist().contains(song);
    }

    public void updatePlaylist(Library playlist, String name, Path imagePath) {
        playlist.setName(name);
        playlist.setImagePath(imagePath);
        writePlaylists();
    }
}
