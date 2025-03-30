package musicApp.controllers;

import musicApp.models.Library;
import musicApp.views.PlaylistEditView;

import java.nio.file.Path;

public class PlaylistEditController extends ViewController<PlaylistEditView, PlaylistEditController> {

    private final PlaylistNavigatorController navigatorController;
    private final Library playlistToEdit;

    /**
     * Create a controller for creating a new playlist
     *
     * @param navigatorController The parent navigator controller
     */
    public PlaylistEditController(PlaylistNavigatorController navigatorController) {
        super(new PlaylistEditView(true));
        this.navigatorController = navigatorController;
        this.playlistToEdit = null;
        view.init();
        view.show();
    }

    /**
     * Create a controller for editing an existing playlist
     *
     * @param navigatorController The parent navigator controller
     * @param playlist            The playlist to edit
     */
    public PlaylistEditController(PlaylistNavigatorController navigatorController, Library playlist) {
        super(new PlaylistEditView(false));
        this.navigatorController = navigatorController;
        this.playlistToEdit = playlist;
        view.init();
        view.populateFields(playlist);
        view.show();
    }

    /**
     * Handle saving the playlist - either create a new one or update an existing one
     *
     * @param name      The playlist name
     * @param imagePath The playlist image path
     */
    public void handleSave(String name, Path imagePath) {
        if (playlistToEdit == null) {
            navigatorController.createPlaylist(name, imagePath);
        } else {
            navigatorController.updatePlaylist(playlistToEdit, name, imagePath);
        }
        view.close();
    }
}
