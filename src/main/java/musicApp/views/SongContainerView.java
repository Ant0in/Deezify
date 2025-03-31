package musicApp.views;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import musicApp.controllers.SongContainerController;
import musicApp.models.Library;
import musicApp.models.Song;

/**
 * The  PlayList view.
 *
 * @param <V> the type parameter
 * @param <C> the type parameter
 */
public abstract class SongContainerView<V extends SongContainerView<V, C, M>, C extends SongContainerController<V, C, M>, M extends Library>
        extends View<V, C> {
    /**
     * The List view.
     */
    @FXML
    protected ListView<Song> listView;


    /**
     * Update list view.
     */
    public void updateListView() {
        listView.getItems().clear();
        listView.getItems().setAll(viewController.toList());
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
                viewController.handlePlaySong();
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
