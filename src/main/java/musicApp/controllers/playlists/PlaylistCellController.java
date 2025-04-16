package musicApp.controllers.playlists;

import musicApp.controllers.ViewController;
import musicApp.models.Library;
import musicApp.utils.LanguageManager;
import musicApp.views.playlists.PlaylistCell;
import musicApp.views.playlists.PlaylistCellView;

/**
 * The type Playlist cell controller.
 */
public class PlaylistCellController extends ViewController<PlaylistCellView> implements PlaylistCellView.PlaylistCellViewListener, PlaylistCell.PlaylistCellListener {

    private final PlaylistNavigatorController navigatorController;
    private Library library;

    /**
     * Instantiates a new Playlist cell controller.
     *
     * @param controller the controller
     */
    public PlaylistCellController(PlaylistNavigatorController controller) {
        super(new PlaylistCellView());
        view.setListener(this);
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
     * Is the library deletable.
     * For now, only the main Library and the favorites ar not deletable.
     *
     * @return True if the library is deletable, false otherwise.
     */
    public boolean isDeletable() {
        return navigatorController.isDeletable(library);
    }

    /**
     * Get the name of the library.
     *
     * @return the name of the library
     */
    public String getLibraryName() {
        if (library.getName().equals("??favorites??")) {
            return LanguageManager.getInstance().get("favorites");
        } else if (library.getName().equals("??library??")) {
            return LanguageManager.getInstance().get("library");
        } else {
            return library.getName();
        }
    }
}
