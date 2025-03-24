package musicApp.controllers;

import musicApp.models.Library;
import musicApp.models.Song;
import musicApp.utils.DataProvider;
import musicApp.views.playlistNavigator.PlaylistNavigatorView;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PlaylistNavigatorController extends ViewController<PlaylistNavigatorView, PlaylistNavigatorController>{

    private List<Library> playlists = new ArrayList<>();
    private Library selectedLibrary;
    private PlayerController playerController;

    /**
     * Instantiates a new View controller.
     *
     * @param playerController the player controller
     */
    public PlaylistNavigatorController(PlayerController playerController) {
        super(new PlaylistNavigatorView());
        this.playerController = playerController;
        initView("/fxml/PlaylistNavigator.fxml");
        loadPlaylists();
    }

    public void loadPlaylists() {
        playlists = playerController.getPlaylists();
        playlists.addFirst(playerController.getLibrary());
        view.update(playlists);
    }

    public Library getSelectedLibrary() {
        return selectedLibrary;
    }

    public void setSelectedLibrary(Library library) {
        this.selectedLibrary = library;
        playerController.updateShownPlaylist(library);
    }

    public void handleSelectLibrary(Library library) {
        setSelectedLibrary(library);
    }

    public void createPlaylist(String name, Path imagePath) {
        Library playlist = new Library(new ArrayList<>(), name, imagePath);
        playlists.add(playlist);
        DataProvider dataProvider = new DataProvider();
        dataProvider.writePlaylists(playlists.subList(1, playlists.size()));
        loadPlaylists();
    }

    public boolean isDeletable(Library library) {
        return !(playlists.getFirst().equals(library) || playlists.get(1).equals(library));
    }

    public void deletePlaylist(Library library) {
        if (isDeletable(library)) {
            playlists.remove(library);
            DataProvider dataProvider = new DataProvider();
            dataProvider.writePlaylists(playlists.subList(1, playlists.size()));
            loadPlaylists();
        }
    }

    public void appendToQueue(Library library) {
        playerController.appendPlaylistToQueue(library);
    }

    public void replaceQueue(Library library) {
        playerController.replaceQueue(library);
    }

    public void refreshUI() {
        view.update(playlists);
    }

    /**
     * Add a song to the library if it is not already in it.
     * Remove it if it is.
     *
     * @param song to add
     */
    public void toggleFavorites(Song song) {
        Library favorites = playlists.get(1);
        if (favorites.toList().contains(song)) {
            favorites.remove(song);
        } else {
            favorites.add(song);
        }
        if (selectedLibrary == favorites) {
            playerController.updateShownPlaylist(favorites);
        }
        DataProvider dataProvider = new DataProvider();
        dataProvider.writePlaylists(playlists.subList(1, playlists.size()));
        refreshUI();
    }

    /**
     * Check if a song is in the favorites.
     *
     * @param song to check
     * @return true if the song is in the favorites
     */
    public boolean isFavorite(Song song) {
        Library favorites = playlists.get(1);
        return favorites.toList().contains(song);
    }
}
