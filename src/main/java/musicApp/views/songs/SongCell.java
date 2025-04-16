package musicApp.views.songs;

import javafx.scene.control.ListCell;
import javafx.scene.layout.Pane;
import musicApp.controllers.songs.SongCellController;
import musicApp.models.Song;

/**
 * A custom ListCell for displaying a song in a ListView.
 * This class extends ListCell and uses a SongCellController
 */
public class SongCell extends ListCell<Song> {

    private final SongCellListener listener;

    public SongCell(SongCellListener _listener) {
        listener = _listener;
    }

    public interface SongCellListener{
        Pane getRoot();
        void update(Song song);
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
}

