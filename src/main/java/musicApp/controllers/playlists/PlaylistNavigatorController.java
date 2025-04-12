package musicApp.controllers.playlists;

import musicApp.controllers.PlayerController;
import musicApp.controllers.ViewController;
import musicApp.models.Library;
import musicApp.models.Song;
import musicApp.utils.DataProvider;
import musicApp.views.playlists.PlaylistNavigatorView;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PlaylistNavigatorController extends ViewController<PlaylistNavigatorView, PlaylistNavigatorController> {

    private final PlayerController playerController;
    private List<Library> playlists = new ArrayList<>();
    private Library selectedLibrary;

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
        view.update(playlists);
    }

    /**
     * Get the selected library.
     *
     * @return the selected library
     */
    public Library getSelectedLibrary() {
        return selectedLibrary;
    }

    /**
     * Set the selected library.
     *
     * @param library the library
     */
    public void setSelectedLibrary(Library library) {
        this.selectedLibrary = library;
        playerController.updateShownPlaylist(library);
    }

    public void handleSelectLibrary(Library library) {
        setSelectedLibrary(library);
    }

    /**
     * Create a new playlist.
     *
     * @param name      the name of the playlist
     * @param imagePath the path to the image of the playlist
     */
    public void createPlaylist(String name, Path imagePath) {
        Library playlist = new Library(new ArrayList<>(), name, imagePath);
        playlists.add(playlist);
        DataProvider dataProvider = new DataProvider();
        dataProvider.writePlaylists(playlists.subList(1, playlists.size()));
        refreshUI();
    }

    /**
     * Check if a playlist is deletable.
     * For now, the favorites and the library are not deletable.
     *
     * @param library the playlist
     * @return true if the playlist is deletable
     */
    public boolean isDeletable(Library library) {
        return !(playlists.getFirst().equals(library) || playlists.get(1).equals(library));
    }

    /**
     * Delete a playlist.
     *
     * @param library the playlist
     */
    public void deletePlaylist(Library library) {
        if (isDeletable(library)) {
            playlists.remove(library);
            DataProvider dataProvider = new DataProvider();
            dataProvider.writePlaylists(playlists.subList(1, playlists.size()));
            refreshUI();
        }
    }

    /**
     * Append all the songs of a playlist to the queue.
     *
     * @param library the playlist
     */
    public void appendToQueue(Library library) {
        playerController.appendPlaylistToQueue(library);
    }

    /**
     * Replace the queue with the songs of a playlist.
     *
     * @param library the playlist
     */
    public void replaceQueue(Library library) {
        playerController.replaceQueue(library);
    }

    /**
     * Refresh the UI.
     */
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
     * Add a song to a playlist.
     *
     * @param song     to add
     * @param playlist to add the song to
     */
    public void addSongToPlaylist(Song song, Library playlist) {
        playlist.add(song);
        DataProvider dataProvider = new DataProvider();
        dataProvider.writePlaylists(playlists.subList(1, playlists.size()));
        refreshUI();
    }

    /**
     * Remove a song from a playlist.
     *
     * @param song     to remove
     * @param playlist to remove the song from
     */
    public void removeSongFromPlaylist(Song song, Library playlist) {
        playlist.remove(song);
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

    /**
     * Update an existing playlist.
     *
     * @param playlist  the playlist to update
     * @param name      the new name
     * @param imagePath the new image path
     */
    public void updatePlaylist(Library playlist, String name, Path imagePath) {
        playlist.setName(name);
        playlist.setImagePath(imagePath);
        DataProvider dataProvider = new DataProvider();
        dataProvider.writePlaylists(playlists.subList(1, playlists.size()));
        refreshUI();
    }
}
