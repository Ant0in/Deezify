package musicApp.controllers.playlists;

import javafx.scene.control.Alert;
import musicApp.controllers.PlayerController;
import musicApp.controllers.ViewController;
import musicApp.exceptions.DeletePlaylistException;
import musicApp.models.Library;
import musicApp.models.Song;
import musicApp.services.LanguageService;
import musicApp.views.playlists.PlaylistNavigatorView;

import java.nio.file.Path;
import java.util.List;

/**
 * The type Playlist navigator controller.
 */
public class PlaylistNavigatorController extends ViewController<PlaylistNavigatorView> implements PlaylistNavigatorView.PlaylistNavigatorViewListener {

    private final PlayerController playerController;
    private final PlaylistsController playlistsController;
    private Library selectedLibrary;
    private final PlaylistContextMenuController playlistContextMenuController;


    /**
     * Instantiates a new View controller.
     *
     * @param controller the player controller
     */
    public PlaylistNavigatorController(PlayerController controller, Path musicFolder, Path userMusicFolder) {
        super(new PlaylistNavigatorView());
        view.setListener(this);
        playerController = controller;
        playlistsController = new PlaylistsController(musicFolder, userMusicFolder, controller.getUserPlaylistPath());
        playlistContextMenuController = new PlaylistContextMenuController(this);
        initView("/fxml/PlaylistNavigator.fxml");
        loadPlaylists();
    }

    /**
     * Load playlists.
     */
    public void loadPlaylists() {
        view.update(playlistsController.getPlaylists());
    }

    /**
     * Set the selected library.
     *
     * @param library the library
     */
    public void setSelectedLibrary(Library library) {
        selectedLibrary = library;
        playerController.updateShownPlaylist(library);
    }


    /**
     * Create a new playlist.
     *
     * @param name      the name of the playlist
     * @param imagePath the path to the image of the playlist
     */
    public void createPlaylist(String name, Path imagePath) {
        playlistsController.createPlaylist(name, imagePath);
        refreshUI();
    }

    /**
     * Check if a playlist is deletable.
     * For now, the favorites and the library are not deletable.
     *
     * @param library the playlist
     * @return true if the playlist is deletable
     */
    public boolean isModifiable(Library library) {
        return playlistsController.isLibraryModifiable(library);
    }

    /**
     * Delete a playlist.
     *
     * @param library the playlist
     */
    public void deletePlaylist(Library library) {
        try{
            playlistsController.deletePlaylist(library);
            refreshUI();
        } catch (DeletePlaylistException e){
            alertService.showAlert(LanguageService.getInstance().get("error.delete_playlist"), Alert.AlertType.WARNING);
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
        view.update(playlistsController.getPlaylists());
    }

    /**
     * Add a song to the library if it is not already in it.
     * Remove it if it is.
     *
     * @param song to add
     */
    public void toggleFavorites(Song song) {
        playlistsController.toggleFavorites(song);
        Library favorites = playlistsController.getFavoritesPlaylist();
        if (selectedLibrary == favorites) {
            playerController.updateShownPlaylist(favorites);
        }
        refreshUI();
    }

    /**
     * Add a song to a playlist.
     *
     * @param song     to add
     * @param playlist to add the song to
     */
    public void addSongToPlaylist(Song song, Library playlist) {
        playlistsController.addSongToPlaylist(song, playlist);
        refreshUI();
    }

    /**
     * Remove a song from a playlist.
     *
     * @param song     to remove
     * @param playlist to remove the song from
     */
    public void removeSongFromPlaylist(Song song, Library playlist) {
        playlistsController.removeSongFromPlaylist(song, playlist);
        refreshUI();
    }

    /**
     * Check if a song is in the favorites.
     *
     * @param song to check
     * @return true if the song is in the favorites
     */
    public boolean isFavorite(Song song) {
        return playlistsController.isFavorite(song);
    }

    /**
     * Update an existing playlist.
     *
     * @param playlist  the playlist to update
     * @param name      the new name
     * @param imagePath the new image path
     */
    public void updatePlaylist(Library playlist, String name, Path imagePath) {
        playlistsController.updatePlaylist(playlist, name, imagePath);
        refreshUI();
    }

    /**
     * Open dialog for creating a new playlist.
     */
    public void handleCreatePlaylist() {
        new EditPlaylistController(this);
    }

    /**
     * Open dialog for editing an existing playlist.
     *
     * @param playlist The playlist to edit
     */
    public void openEditPlaylistDialog(Library playlist) {
        new EditPlaylistController(this, playlist);
    }

    public void handleShowContextMenu(double x, double y, Library library) {
        playlistContextMenuController.showAt(x, y, library);
    }

    public void updateMainLibrary(Path newMusicFolder) {
        playlistsController.updateMainLibrary(newMusicFolder);
    }

    public void updateUserMainLibrary(Path newUserMusicFolder) {
        playlistsController.updateUserMainLibrary(newUserMusicFolder);
    }

    public void updateUserPlaylists(Path userPlaylistPath) {
        playlistsController.updateUserPlaylists(userPlaylistPath);
    }

    public Library getMainLibrary() {
        return playlistsController.getMainLibrary();
    }

    public List<Library> getPlaylists() {
        return playlistsController.getPlaylists();
    }
}
