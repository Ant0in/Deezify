package musicApp.views.playlists;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import musicApp.models.Library;
import musicApp.views.BaseView;

/**
 * The PlaylistCellView class is responsible for displaying a cell of a playlist.
 * It extends the View class and uses FXML to define its layout.
 */
public class PlaylistCellView extends BaseView {
    private PlaylistCellViewListener listener;
    
    @FXML
    private ImageView imageCover;
    @FXML
    private Label playlistNameLabel, playlistSizeLabel;


    public interface PlaylistCellViewListener {
        Library getLibrary();
        String getLibraryName();
    }

    /**
     * Sets listener.
     *
     * @param listener the listener
     */
    public void setListener(PlaylistCellViewListener listener) {
        this.listener = listener;
    }
    
    /**
     * Initializes the PlaylistCellView.
     * This method is called to set up the view when it is created.
     */
    @Override
    public void init() {
        initComponents();
    }

    /**
     * Initializes the components of the PlaylistCellView.
     * This method sets the playlist name and size labels based on the library data.
     */
    private void initComponents() {
        if (listener.getLibrary() == null) return;
        playlistNameLabel.setText(listener.getLibraryName());
        playlistSizeLabel.setText(listener.getLibrary().size() + " songs");
    }

    /**
     * Updates the PlaylistCellView with the given library data.
     * This method is called to refresh the view when the library data changes.
     *
     * @param library The library data to update the view with.
     */
    public void update(Library library) {
        playlistNameLabel.setText(listener.getLibraryName());
        playlistSizeLabel.setText(library.size() + " songs");
        imageCover.setImage(library.getCoverImage());
    }
}
