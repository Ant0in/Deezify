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


    public void handleDeletePlaylist() {
        playlistNavigatorController.deletePlaylist(selectedLibrary);
    }

    public void handleEditPlaylist() {
        if (playlistNavigatorController.isModifiable(selectedLibrary)) {
            playlistNavigatorController.openEditPlaylistDialog(selectedLibrary);
        } else {
            alertService.showAlert(LanguageService.getInstance().get("error.edit_playlist"), Alert.AlertType.WARNING);
        }
    }

    public void handleAppendToQueue() {
        playlistNavigatorController.appendToQueue(selectedLibrary);
    }

    public void handleReplaceQueue() {
        playlistNavigatorController.replaceQueue(selectedLibrary);
    }

    public void showAt(double x, double y, Library newSelectedLibrary) {
        selectedLibrary = newSelectedLibrary;
        view.show(playlistNavigatorController.getRoot(), x, y);
    }
}
