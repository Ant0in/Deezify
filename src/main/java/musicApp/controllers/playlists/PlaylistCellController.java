package musicApp.controllers.playlists;

import musicApp.controllers.ViewController;
import musicApp.models.Library;
import musicApp.utils.LanguageManager;
import musicApp.views.playlists.PlaylistCellView;

public class PlaylistCellController extends ViewController<PlaylistCellView, PlaylistCellController> {

    private final PlaylistNavigatorController navigatorController;
    private Library library;

    public PlaylistCellController(PlaylistNavigatorController navigatorController) {
        super(new PlaylistCellView());
        this.navigatorController = navigatorController;
        initView("/fxml/PlaylistCell.fxml");
    }

    /**
     * Update the song in the view.
     *
     * @param library the new selected library
     */
    public void update(Library library) {
        if (!library.equals(getLibrary())) {
            this.library = library;
        }
        this.view.update(library);
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
