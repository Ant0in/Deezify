package musicApp.controllers.playlists;

import musicApp.controllers.ViewController;
import musicApp.models.Library;
import musicApp.services.LanguageService;
import musicApp.views.playlists.PlaylistCellView;

/**
 * The type Playlist cell controller.
 */
public class PlaylistCellController extends ViewController<PlaylistCellView, PlaylistCellController> {

    private final PlaylistNavigatorController navigatorController;
    private Library library;

    /**
     * Instantiates a new Playlist cell controller.
     *
     * @param controller the controller
     */
    public PlaylistCellController(PlaylistNavigatorController controller) {
        super(new PlaylistCellView());
        navigatorController = controller;
        initView("/fxml/PlaylistCell.fxml");
    }

    /**
     * Update the song in the view.
     *
     * @param _library the new selected library
     */
    public void update(Library _library) {
        if (!_library.equals(getLibrary())) {
            library = _library;
        }
        view.update(library);
    }

    /**
     * Get the library of the cell.
     *
     * @return the library
     */
    public Library getLibrary() {
        return library;
    }

    /**
     * Is the library selected.
     *
     * @return True if the library is selected, false otherwise.
     */
    public boolean isSelected() {
        return navigatorController.getSelectedLibrary().equals(library);
    }

    /**
     * Get the name of the library.
     *
     * @return the name of the library
     */
    public String getLibraryName() {
        if (library.getName().equals("??favorites??")) {
            return LanguageService.getInstance().get("favorites");
        } else if (library.getName().equals("??library??")) {
            return LanguageService.getInstance().get("library");
        } else {
            return library.getName();
        }
    }
}
