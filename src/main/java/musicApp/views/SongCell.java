package musicApp.views;

import javafx.scene.control.ListCell;
import musicApp.controllers.SongCellController;
import musicApp.models.Song;


public class SongCell extends ListCell<Song> {

    private final SongCellController songCellController;

    public SongCell(SongCellController songCellController) {
        this.songCellController = songCellController;
    }

    @Override
    protected void updateItem(Song song, boolean empty) {
        super.updateItem(song, empty);
        if (empty || song == null) {
            setGraphic(null);
        } else {
            if (!song.equals(songCellController.getSong())) {
                setGraphic(songCellController.getRoot());
            }
            songCellController.update(song);
        }
    }
}

