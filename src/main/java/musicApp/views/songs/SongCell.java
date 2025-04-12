package musicApp.views.songs;

import javafx.scene.control.ListCell;
import musicApp.controllers.songs.SongCellController;
import musicApp.models.Song;


public class SongCell extends ListCell<Song> {

    private final SongCellController songCellController;

    public SongCell(SongCellController controller) {
        songCellController = controller;
    }

    @Override
    protected void updateItem(Song song, boolean empty) {
        super.updateItem(song, empty);
        if (empty || song == null) {
            setGraphic(null);
            setText(null);
        } else {
            setGraphic(songCellController.getRoot());
            songCellController.update(song);
        }
    }
}

