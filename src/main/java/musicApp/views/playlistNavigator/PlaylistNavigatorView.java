package musicApp.views.playlistNavigator;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import musicApp.controllers.PlaylistCellController;
import musicApp.controllers.PlaylistNavigatorController;
import musicApp.models.Library;
import musicApp.views.View;

import java.util.List;

public class PlaylistNavigatorView extends View<PlaylistNavigatorView, PlaylistNavigatorController> {

    @FXML
    private ListView<Library> listView;

    public PlaylistNavigatorView() {}

    @Override
    public void init() {
        initListView();
    }

    private void initListView() {
        listView.setCellFactory(_ -> new PlaylistCell(new PlaylistCellController(viewController)));
    }

    public void update(List<Library> libraries) {
        listView.getItems().clear();
        listView.getItems().addAll(libraries);
    }

    /**
     * Enable double click to play.
     */
    public void enableClickToSelect() {
        listView.setOnMouseClicked(_ -> {
            viewController.setSelectedLibrary(listView.getSelectionModel().getSelectedItem());
        });
    }

}
