package musicApp.views.playlists;

import javafx.scene.control.ListCell;
import musicApp.controllers.playlists.PlaylistCellController;
import musicApp.models.Library;

public class PlaylistCell extends ListCell<Library> {

    private final PlaylistCellController playlistCellController;

    public PlaylistCell(PlaylistCellController playlistCellController) {
        this.playlistCellController = playlistCellController;
    }

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
