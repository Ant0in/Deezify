package musicApp.controllers.playlists;

import javafx.scene.control.Alert;
import musicApp.controllers.ViewController;
import musicApp.models.Library;
import musicApp.services.LanguageService;
import musicApp.views.playlists.PlaylistContextMenuView;

public class PlaylistContextMenuController extends ViewController<PlaylistContextMenuView> implements PlaylistContextMenuView.PlaylistContextMenuViewListener {
    private final PlaylistNavigatorController playlistNavigatorController;
    private Library selectedLibrary;

    public PlaylistContextMenuController(PlaylistNavigatorController controller) {
        super(new PlaylistContextMenuView());
        view.setListener(this);
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
        if (playlistNavigatorController.isModifiable(selectedLibrary)) {
            playlistNavigatorController.openEditPlaylistDialog(selectedLibrary);
        } else {
            alertService.showAlert(LanguageService.getInstance().get("error.edit_playlist"), Alert.AlertType.WARNING);
        }
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
