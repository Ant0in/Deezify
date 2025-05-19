package musicApp.views.songs;

import javafx.scene.control.ListCell;
import javafx.scene.layout.Pane;
import musicApp.models.Song;

/**
 * A custom ListCell for displaying a song in a ListView.
 * This class extends ListCell and uses a SongCellController
 */
public class SongCell extends ListCell<Song> {

    private final SongCellListener listener;

    public SongCell(SongCellListener newListener) {
        listener = newListener;
    }

    @Override
    protected void updateItem(Song song, boolean empty) {
        super.updateItem(song, empty);
        if (empty || song == null) {
            setGraphic(null);
            setText(null);
        } else {
            setGraphic(listener.getRoot());
            listener.update(song);
        }
    }


    /**
     * Listener interface for handling song cell events.
     * Implement this interface to define the UI component and update functionality for a song cell.
     */
    public interface SongCellListener {
        void update(Song song);

        Pane getRoot();
    }
}

