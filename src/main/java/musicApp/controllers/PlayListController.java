package musicApp.controllers;

import musicApp.models.Playlist;
import musicApp.views.PlayListView;

public class PlayListController extends SongContainerController<PlayListView,PlayListController,Playlist> {
    /**
     * Instantiates a new Play list controller.
     *
     * @param view             the view
     * @param playerController the player controller
     */
    public PlayListController(PlayListView view, PlayerController playerController) {
        super(view, playerController);
    }
}
