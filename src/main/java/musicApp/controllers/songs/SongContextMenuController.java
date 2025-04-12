package musicApp.controllers.songs;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import musicApp.controllers.ViewController;
import musicApp.models.Library;
import musicApp.views.songs.SongContextMenuView;

import java.util.List;

/**
 * The type Song context menu controller.
 */
public class SongContextMenuController extends ViewController<SongContextMenuView, SongContextMenuController> {

    /**
     * The Song cell controller.
     */
    public SongCellController songCellController;
    private final boolean isMainLibrary;
    private StringProperty languageProperty;

    /**
     * Instantiates a new Song context menu controller.
     *
     * @param cellController the cell controller
     */
    public SongContextMenuController(SongCellController cellController) {
        super(new SongContextMenuView());
        songCellController = cellController;
        isMainLibrary = cellController.isShowingMainLibrary();
        initView("/fxml/SongContextMenu.fxml", true);
        initLanguageProperty();
    }

    private void initLanguageProperty() {
        languageProperty = LanguageManager.getInstance().getLanguageProperty();
        languageProperty.addListener((_, _, _) -> refreshTranslation());
    }

    /**
     * Handle edit metadata.
     */
    public void handleEditMetadata() {
        if (songCellController.getSong().isSong()) {
            songCellController.openMetadataEditor();
        } else {
            String errorMessage = LanguageManager.getInstance().get("error.edit_metadata");
            alertService.showAlert(errorMessage, Alert.AlertType.WARNING);
        }
    }

    /**
     * Handle remove from playlist.
     */
    public void handleRemoveFromPlaylist() {
        songCellController.removeSongFromPlaylist();
    }

    /**
     * Is showing main library boolean.
     *
     * @return the boolean
     */
    public boolean isShowingMainLibrary() {
        return isMainLibrary;
    }

    /**
     * Populate playlist menu items.
     *
     * @param addToMenu      the add to menu
     * @param removeFromMenu the remove from menu
     */
    public void populatePlaylistMenuItems(Menu addToMenu, Menu removeFromMenu) {
        List<Library> playlists = songCellController.getPlaylists();

        playlists.stream().skip(2).forEach(playlist -> {
            MenuItem playlistItem = new MenuItem(playlist.getName());
            playlistItem.setOnAction(event -> songCellController.addSongToPlaylist(playlist));
            addToMenu.getItems().add(playlistItem);

            if (isShowingMainLibrary() && songCellController.containsSong(playlist)) {
                MenuItem removeItem = new MenuItem(playlist.getName());
                removeItem.setOnAction(event -> songCellController.removeSongFromPlaylist(playlist));
                if (removeFromMenu != null) {
                    removeFromMenu.getItems().add(removeItem);
                }
            }
        });
    }

    /**
     * Show at.
     *
     * @param x the x
     * @param y the y
     */
    public void showAt(double x, double y) {
        view.show(songCellController.getRoot(), x, y);
    }

    /**
     * Refresh translation.
     */
    public void refreshTranslation() {
        view.refreshTranslation();
    }
}