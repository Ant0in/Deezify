package musicApp.controllers.playlists;

import musicApp.controllers.ViewController;
import musicApp.models.Library;
import musicApp.views.playlists.EditPlaylistView;

import java.nio.file.Path;

/**
 * The type Playlist edit controller.
 */
public class EditPlaylistController extends ViewController<EditPlaylistView> implements EditPlaylistView.EditPlaylistViewListener {

    private final Library playlistToEdit;
    private final boolean isCreation;
    private PlaylistNavigatorController navigatorController;

    /**
     * Create a controller for creating a new playlist
     *
     * @param _controller The parent navigator controller
     */
    public EditPlaylistController(PlaylistNavigatorController _controller) {
        super(new EditPlaylistView());
        init(_controller);
        isCreation = true;
        playlistToEdit = null;
        initView("/fxml/EditPlaylist.fxml");
    }

    /**
     * Create a controller for editing an existing playlist
     *
     * @param _controller The parent navigator controller
     * @param _playlist   The playlist to edit
     */
    public EditPlaylistController(PlaylistNavigatorController _controller, Library _playlist) {
        super(new EditPlaylistView());
        init(_controller);
        isCreation = false;
        playlistToEdit = _playlist;
        initView("/fxml/EditPlaylist.fxml");
    }

    /**
     * Initialize the controller and part of the constructor.
     *
     * @param _controller The parent navigator controller
     */
    private void init(PlaylistNavigatorController _controller) {
        view.setListener(this);
        navigatorController = _controller;
    }

    /**
     * Check if the view is in creation mode.
     */
    public boolean isCreation() {
        return isCreation;
    }

    /**
     * Handle saving the playlist - either create a new one or update an existing one
     *
     * @param playlistName The playlist name
     * @param imagePath    The playlist image path
     */
    public void handleSave(String playlistName, Path imagePath) {
        if (playlistToEdit == null) {
            navigatorController.createPlaylist(playlistName, imagePath);
        } else {
            navigatorController.updatePlaylist(playlistToEdit, playlistName, imagePath);
        }
        handleClose();
    }

    /**
     * Handle the close action of the view.
     */
    public void handleClose() {
        view.close();
    }
}
