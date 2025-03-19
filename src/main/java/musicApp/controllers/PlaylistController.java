package musicApp.controllers;

import musicApp.models.Playlist;
import musicApp.views.PlaylistView;

public class PlaylistController extends SongContainerController<PlaylistView, PlaylistController,Playlist> {
    /**
     * Instantiates a new Play list controller.
     *
     * @param view             the view
     * @param playerController the player controller
     */
    public PlaylistController(PlaylistView view, PlayerController playerController) {
        super(view, playerController);
        initView("/fxml/PlaylistNavigator.fxml");
    }

}
