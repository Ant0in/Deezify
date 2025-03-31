package musicApp.controllers.songs;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import musicApp.controllers.ViewController;
import musicApp.models.Library;
import musicApp.views.songs.SongContextMenuView;

import java.util.List;

public class SongContextMenuController extends ViewController<SongContextMenuView, SongContextMenuController> {

    public SongCellController songCellController;
    private final boolean isMainLibrary;

    public SongContextMenuController(SongCellController cellController) {
        super(new SongContextMenuView());
        this.songCellController = cellController;
        this.isMainLibrary = cellController.isShowingMainLibrary();

        initView();
    }

    // Add this method
    private void initView() {
        view.init();
    }


    public void handleEditMetadata() {
        if (songCellController.getSong().isSong()) {
            songCellController.openMetadataEditor();
        }
    }

    public void handleRemoveFromPlaylist() {
        songCellController.removeSongFromPlaylist();
    }

    public boolean isShowingMainLibrary() {
        return isMainLibrary;
    }

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

    public void showAt(double x, double y) {
        view.show(songCellController.getRoot(), x, y);
    }

    public void refreshTranslation() {
        view.refreshTranslation();
    }
}