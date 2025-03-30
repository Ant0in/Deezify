package musicApp.views.playlistNavigator;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import musicApp.controllers.PlaylistCellController;
import musicApp.controllers.PlaylistEditController;
import musicApp.controllers.PlaylistNavigatorController;
import musicApp.models.Library;
import musicApp.utils.LanguageManager;
import musicApp.views.View;

import java.util.List;

public class PlaylistNavigatorView extends View<PlaylistNavigatorView, PlaylistNavigatorController> {

    @FXML
    private ListView<Library> listView;

    @FXML
    private Button createPlaylist;

    private final ContextMenu contextMenu = new ContextMenu();

    public PlaylistNavigatorView() {
    }

    @Override
    public void init() {
        initListView();
        enableClickToSelect();
        setButtonActions();
        initTranslation();
        setupContextMenu();
    }

    private void initListView() {
        listView.setCellFactory(_ -> new PlaylistCell(new PlaylistCellController(viewController)));
    }

    private void setButtonActions() {
        createPlaylist.setOnAction(_ -> openCreatePlaylistDialog());
    }

    private void initTranslation() {
        createPlaylist.setText(LanguageManager.getInstance().get("button.create_playlist"));
    }

    /**
     * Open dialog for creating a new playlist.
     */
    private void openCreatePlaylistDialog() {
        new PlaylistEditController(viewController);
    }

    /**
     * Open dialog for editing an existing playlist.
     *
     * @param playlist The playlist to edit
     */
    private void openEditPlaylistDialog(Library playlist) {
        new PlaylistEditController(viewController, playlist);
    }

    public void update(List<Library> libraries) {
        listView.getItems().clear();
        listView.getItems().addAll(libraries);
    }

    /**
     * Enable click to select playlists.
     */
    public void enableClickToSelect() {
        listView.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                if (listView.getSelectionModel().getSelectedItem() == null) return;
                viewController.setSelectedLibrary(listView.getSelectionModel().getSelectedItem());
            } else if (e.getButton() == MouseButton.SECONDARY) {
                contextMenu.show(listView, e.getScreenX(), e.getScreenY());
            }
        });
    }

    private void setupContextMenu() {
        MenuItem addToQueueItem = new MenuItem(LanguageManager.getInstance().get("context_menu.append_to_queue"));
        MenuItem replaceQueueItem = new MenuItem(LanguageManager.getInstance().get("context_menu.replace_queue"));
        MenuItem editItem = new MenuItem(LanguageManager.getInstance().get("context_menu.edit"));
        MenuItem deleteItem = new MenuItem(LanguageManager.getInstance().get("context_menu.delete"));

        addToQueueItem.setOnAction(_ -> {
            Library selectedPlaylist = listView.getSelectionModel().getSelectedItem();
            if (selectedPlaylist != null) {
                viewController.appendToQueue(selectedPlaylist);
            }
        });

        replaceQueueItem.setOnAction(_ -> {
            Library selectedPlaylist = listView.getSelectionModel().getSelectedItem();
            if (selectedPlaylist != null) {
                viewController.replaceQueue(selectedPlaylist);
            }
        });

        editItem.setOnAction(_ -> {
            Library selectedPlaylist = listView.getSelectionModel().getSelectedItem();
            if (selectedPlaylist != null && viewController.isDeletable(selectedPlaylist)) {
                openEditPlaylistDialog(selectedPlaylist);
            }
        });

        deleteItem.setOnAction(_ -> {
            Library selectedPlaylist = listView.getSelectionModel().getSelectedItem();
            if (selectedPlaylist != null && viewController.isDeletable(selectedPlaylist)) {
                viewController.deletePlaylist(selectedPlaylist);
            }
        });

        contextMenu.getItems().addAll(addToQueueItem, replaceQueueItem, editItem, deleteItem);
    }

    /**
     * Sets the currently selected playlist in the list view
     *
     * @param playlist The playlist to select
     */
    public void setSelectedPlaylist(Library playlist) {
        if (playlist != null) {
            listView.getSelectionModel().select(playlist);
        }
    }
}
