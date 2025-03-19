package musicApp.controllers;

import musicApp.models.Library;
import musicApp.models.Playlist;
import musicApp.views.playlistNavigator.PlaylistNavigatorView;

import java.util.ArrayList;
import java.util.List;

public class PlaylistNavigatorController extends ViewController<PlaylistNavigatorView, PlaylistNavigatorController>{

    private List<Playlist> playlists = new ArrayList<>();
    private Library selectedLibrary;
    private PlayerController playerController;

    /**
     * Instantiates a new View controller.
     *
     * @param playerController the player controller
     */
    public PlaylistNavigatorController(PlayerController playerController) {
        super(new PlaylistNavigatorView());
        this.playerController = playerController;
        initView("/fxml/PlaylistNavigator.fxml");
        loadPlaylists();
    }

    public void loadPlaylists() {
        playlists = playerController.getPlaylists();
        view.update((List<Library>)(List<?>)playlists);
    }

    public Library getSelectedLibrary() {
        return selectedLibrary;
    }

    public void setSelectedLibrary(Library library) {
        this.selectedLibrary = library;
    }

    public void handleSelectLibrary(Library library) {
        setSelectedLibrary(library);
    }
}
