package musicApp.views;

import musicApp.controllers.PlaylistController;
import musicApp.controllers.SongCellController;
import musicApp.models.Playlist;

public class PlaylistView extends SongContainerView<PlaylistView, PlaylistController, Playlist> {
    public PlaylistView() {
        super();
    }

    @Override
    public void init() {

    }

    /**
     * Initialize the playlist view.
     */
    private void initPlaylistView() {
//        listView.setCellFactory(_ -> new SongCell(new SongCellController(viewController)));
        updateListView();
    }

}
