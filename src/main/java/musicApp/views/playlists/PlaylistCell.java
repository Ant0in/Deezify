package musicApp.views.playlists;

import javafx.scene.control.ListCell;
import musicApp.controllers.playlists.PlaylistCellController;
import musicApp.models.Library;

/**
 * A custom ListCell for displaying a playlist in a ListView.
 * This class extends ListCell and uses a PlaylistCellController
 */
public class PlaylistCell extends ListCell<Library> {

    private final PlaylistCellController playlistCellController;

    /**
     * Instantiates a new Playlist cell.
     *
     * @param controller the controller
     */
    public PlaylistCell(PlaylistCellController controller) {
        super();
        playlistCellController = controller;
    }

    /**
     * Initializes the cell with the given library.
     *
     * @param library The library to be displayed in the cell.
     * @param empty   Indicates whether the cell is empty or not.
     */
    @Override
    protected void updateItem(Library library, boolean empty) {
        super.updateItem(library, empty);
        if (empty || library == null) {
            setGraphic(null);
            setText(null);
        } else {
            setGraphic(playlistCellController.getRoot());
            playlistCellController.update(library);
        }
    }
}
