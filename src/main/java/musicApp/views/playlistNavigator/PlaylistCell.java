package musicApp.views.playlistNavigator;

import javafx.scene.control.ListCell;
import musicApp.controllers.PlaylistCellController;
import musicApp.controllers.PlaylistController;
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
        } else {
            if (!library.equals(playlistCellController.getLibrary())) {
                setGraphic(playlistCellController.getRoot());
            }
            playlistCellController.update(library);
        }
    }
}
