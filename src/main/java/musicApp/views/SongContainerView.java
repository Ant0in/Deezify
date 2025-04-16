package musicApp.views;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import musicApp.controllers.SongContainerController;
import musicApp.models.Library;
import musicApp.models.Song;
import musicApp.views.playlists.PlaylistContextMenuView;

import java.util.List;

/**
 * The  PlayList view.
 */
public abstract class SongContainerView extends BaseView {
    private SongContainerViewListener listener;
    /**
     * The List view.
     */
    @FXML
    protected ListView<Song> listView;

    public interface SongContainerViewListener {
        List<Song> toList();
        void handlePlaySong();
    }

    /**
     * Sets listener.
     *
     * @param listener the listener
     */
    public void setListener(SongContainerViewListener listener) {
        this.listener = listener;
    }

    /**
     * Update list view.
     */
    public void updateListView() {
        listView.getItems().clear();
        listView.getItems().setAll(listener.toList());
    }

    /**
     * Clear selection.
     */
    public void clearSelection() {
        listView.getSelectionModel().clearSelection();
    }

    /**
     * Gets selected song index.
     *
     * @return the selected song index
     */
    public int getSelectedSongIndex() {
        return listView.getSelectionModel().getSelectedIndex();
    }

    /**
     * Enable double click to play.
     */
    public void enableDoubleClickToPlay() {
        listView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                listener.handlePlaySong();
            }
        });
    }

    /**
     * Is selected boolean binding.
     *
     * @return the boolean binding
     */
    public BooleanBinding isSelected() {
        return listView.getSelectionModel().selectedItemProperty().isNotNull();
    }

    /**
     * Refresh ui.
     */
    public void refreshUI() {
    }

}
