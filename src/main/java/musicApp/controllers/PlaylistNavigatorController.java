package musicApp.controllers;

import musicApp.models.Library;
import musicApp.utils.DataProvider;
import musicApp.views.playlistNavigator.PlaylistNavigatorView;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PlaylistNavigatorController extends ViewController<PlaylistNavigatorView, PlaylistNavigatorController>{

    private List<Library> playlists = new ArrayList<>();
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
        playlists.addFirst(playerController.getLibrary());
        view.update(playlists);
    }

    public Library getSelectedLibrary() {
        return selectedLibrary;
    }

    public void setSelectedLibrary(Library library) {
        this.selectedLibrary = library;
        playerController.updateShownPlaylist(library);
    }

    public void handleSelectLibrary(Library library) {
        setSelectedLibrary(library);
    }

    public void createPlaylist(String name, Path imagePath) {
        Library playlist = new Library(new ArrayList<>(), name, imagePath);
        playlists.add(playlist);
        DataProvider dataProvider = new DataProvider();
        dataProvider.writePlaylists(playlists.subList(1, playlists.size()));
        loadPlaylists();
    }
}
