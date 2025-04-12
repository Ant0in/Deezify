package musicApp.controllers.playlists;

import musicApp.controllers.ViewController;
import javafx.scene.Node;
import musicApp.models.Library;
import musicApp.views.playlists.PlaylistContextMenuView;

public class PlaylistContextMenuController extends ViewController<PlaylistContextMenuView, PlaylistContextMenuController> {
    private final PlaylistNavigatorController playlistNavigatorController;
    private Library selectedLibrary;

    public PlaylistContextMenuController(PlaylistNavigatorController controller) {
        super(new PlaylistContextMenuView());
        this.playlistNavigatorController = controller;
        initView("/fxml/PlaylistContextMenu.fxml", true);
    }

    /**
     * Deletes a playlist.
     */
    public void deletePlaylist() {
        playlistNavigatorController.deletePlaylist(selectedLibrary);
    }

    public void editPlaylist() {
        playlistNavigatorController.openEditPlaylistDialog(selectedLibrary);
    }

    public void appendToQueue() {
        playlistNavigatorController.appendToQueue(selectedLibrary);
    }

    public void replaceQueue() {
        playlistNavigatorController.replaceQueue(selectedLibrary);
    }

    public void showAt(double x, double y, Library selectedLibrary) {
        this.selectedLibrary = selectedLibrary;
        view.show(playlistNavigatorController.getRoot(), x, y);
    }
}
