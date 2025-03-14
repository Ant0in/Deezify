package MusicApp.Views;

import MusicApp.Controllers.PlayListController;
import MusicApp.Models.Song;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public abstract class PlayListView<V extends PlayListView<V, C>, C extends PlayListController<V, C>>
        extends View<V, C> {
    @FXML
    protected ListView<Song> listView;

    public void updateListView(){
        listView.getItems().setAll(viewController.toList());
    }

    public void clearSelection() {
        listView.getSelectionModel().clearSelection();
    }

    public int getSelectedSongIndex() {
        return listView.getSelectionModel().getSelectedIndex();
    }

    public void enableDoubleClickToPlay() {
        listView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                viewController.handlePlaySong();
            }
        });
    }

    public BooleanBinding isSelected() {
        return listView.getSelectionModel().selectedItemProperty().isNotNull();
    }
}
