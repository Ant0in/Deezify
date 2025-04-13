package musicApp.controllers.playlists;

import javafx.stage.Stage;
import musicApp.controllers.ViewController;
import musicApp.models.Library;
import musicApp.utils.LanguageManager;
import musicApp.views.playlists.EditPlaylistView;

import java.nio.file.Path;

/**
 * The type Playlist edit controller.
 */
public class EditPlaylistController extends ViewController<EditPlaylistView, EditPlaylistController> {

    private final PlaylistNavigatorController navigatorController;
    private final Library playlistToEdit;
    private final Stage stage;
    private final boolean isCreation;

    /**
     * Create a controller for creating a new playlist
     *
     * @param _controller The parent navigator controller
     */
    public EditPlaylistController(PlaylistNavigatorController _controller) {
        super(new EditPlaylistView());
        stage = new Stage();
        isCreation = true;
        navigatorController = _controller;
        playlistToEdit = null;
        initView("/fxml/EditPlaylist.fxml");
        stage.setTitle(LanguageManager.getInstance().get("create_playlist.title"));
        stage.setScene(view.getScene());
        stage.show();
    }

    /**
     * Create a controller for editing an existing playlist
     *
     * @param _controller The parent navigator controller
     * @param _playlist   The playlist to edit
     */
    public EditPlaylistController(PlaylistNavigatorController _controller, Library _playlist) {
        super(new EditPlaylistView());
        stage = new Stage();
        isCreation = false;
        navigatorController = _controller;
        playlistToEdit = _playlist;
        initView("/fxml/EditPlaylist.fxml");
        stage.setTitle(LanguageManager.getInstance().get("edit_playlist.title"));
        view.init();
        view.populateFields(_playlist);
        stage.show();
    }

    public boolean isCreation() {
        return isCreation;
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
        close();
    }

    public Stage getStage() {
        return stage;
    }

    public void close() {
        stage.close();
    }
}
