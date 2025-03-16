package musicApp.views;

import javafx.scene.control.ListCell;
import musicApp.controllers.SongController;
import musicApp.models.Song;


public class SongCell extends ListCell<Song> {

    private final SongController songController;

    public SongCell(SongController songController) {
        this.songController = songController;
    }

    @Override
    protected void updateItem(Song song, boolean empty) {
        super.updateItem(song, empty);
        if (empty || song == null) {
            setGraphic(null);
        } else {
            if (!song.equals(songController.getSong())) {
                setGraphic(songController.getRoot());
            }
            songController.update(song);
        }
    }
}

