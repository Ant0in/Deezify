package musicApp.views.playlists;

import javafx.scene.control.ListCell;
import javafx.scene.layout.Pane;
import musicApp.models.Library;

/**
 * A custom ListCell for displaying a playlist in a ListView.
 * This class extends ListCell and uses a PlaylistCellController
 */
public class PlaylistCell extends ListCell<Library> {

    private final PlaylistCellListener listener;

    /**
     * Instantiates a new Playlist cell.
     *
     * @param _listener the controller
     */
    public PlaylistCell(PlaylistCellListener _listener) {
        listener = _listener;
    }

    /**
     * The listener interface for handling playlist cell events.
     * Implement this interface to provide the cell layout and update functionality.
     */
    public interface PlaylistCellListener {
        Pane getRoot();

        void update(Library library);
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
            setGraphic(listener.getRoot());
            listener.update(library);
        }
    }
}
