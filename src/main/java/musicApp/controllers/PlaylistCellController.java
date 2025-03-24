package musicApp.controllers;

import musicApp.models.Library;
import musicApp.models.Song;
import musicApp.utils.LanguageManager;
import musicApp.views.playlistNavigator.PlaylistCellView;

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
            this.view.update(library);
        }
    }

    public Library getLibrary() {
        return library;
    }

    public boolean isSelected() {
        return navigatorController.getSelectedLibrary().equals(library);
    }

    public boolean isDeletable() {
        return navigatorController.isDeletable(library);
    }

    public String getLibraryName() {
        if (library.getName().equals("??favorites??")) {
            return LanguageManager.getInstance().get("favorites");
        }
        else if (library.getName().equals("??library??")) {
            return LanguageManager.getInstance().get("library");
        } else {
            return library.getName();
        }
    }
}
