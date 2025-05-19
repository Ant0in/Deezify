package musicApp.views.playlists;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import musicApp.views.View;

/**
 * The PlaylistCellView class is responsible for displaying a cell of a playlist.
 * It extends the View class and uses FXML to define its layout.
 */
public class PlaylistCellView extends View {

    private PlaylistCellViewListener listener;

    @FXML
    private ImageView imageCover;
    @FXML
    private Label playlistNameLabel, playlistSizeLabel;

    /**
     * Sets listener.
     *
     * @param newListener the listener
     */
    public void setListener(PlaylistCellViewListener newListener) {
        listener = newListener;
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
        if (listener.isShowingLibrary()) {
            playlistNameLabel.setText(listener.getLibraryDisplayName());
            playlistSizeLabel.setText(listener.getLibrarySize() + " songs");
        }
    }

    /**
     * Updates the PlaylistCellView with the given library data.
     * This method is called to refresh the view when the library data changes.
     */
    public void update() {
        playlistNameLabel.setText(listener.getLibraryDisplayName());
        playlistNameLabel.setTooltip(new Tooltip(playlistNameLabel.getText()));
        playlistSizeLabel.setText(listener.getLibrarySize() + " songs");
        imageCover.setImage(listener.getLibraryCoverImage());
    }

    /**
     * Listener interface for handling events or retrieving data for the PlaylistCellView.
     * Implement this interface to provide the necessary library information for the cell view.
     */
    public interface PlaylistCellViewListener {
        int getLibrarySize();

        String getLibraryDisplayName();

        boolean isShowingLibrary();

        Image getLibraryCoverImage();
    }
}
