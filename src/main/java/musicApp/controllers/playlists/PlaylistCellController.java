package musicApp.controllers.playlists;

import javafx.scene.image.Image;
import musicApp.controllers.ViewController;
import musicApp.models.Library;
import musicApp.services.LanguageService;
import musicApp.views.playlists.PlaylistCell;
import musicApp.views.playlists.PlaylistCellView;

/**
 * The type Playlist cell controller.
 */
public class PlaylistCellController extends ViewController<PlaylistCellView> implements PlaylistCellView.PlaylistCellViewListener, PlaylistCell.PlaylistCellListener {

    private Library library;

    /**
     * Instantiates a new Playlist cell controller.
     *
     */
    public PlaylistCellController() {
        super(new PlaylistCellView());
        view.setListener(this);
        initView("/fxml/PlaylistCell.fxml");
    }

    public boolean isShowingLibrary() {
        return library != null;
    }

    /**
     * Update the song in the view.
     *
     * @param newLibrary the new selected library
     */
    public void update(Library newLibrary) {
        if (!newLibrary.equals(library)) {
            library = newLibrary;
        }
        view.update();
    }

    /**
     * Get the library of the cell.
     *
     * @return the library
     */
    public int getLibrarySize() {
        return library.size();
    }

    /**
     * Get the cover image of the library.
     *
     * @return the cover image
     */
    public Image getLibraryCoverImage() {
        return library.getCoverImage();
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
