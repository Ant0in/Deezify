package musicApp.views.playlists;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import musicApp.controllers.playlists.PlaylistCellController;
import musicApp.controllers.playlists.PlaylistNavigatorController;
import musicApp.models.Library;
import musicApp.utils.LanguageManager;
import musicApp.views.View;

import java.util.List;

/**
 * The PlaylistNavigatorView class is responsible for displaying and managing the playlist navigator UI.
 * It allows users to create, edit, and delete playlists, as well as manage their contents.
 */
public class PlaylistNavigatorView extends View {

    private PlaylistNavigatorViewListener listener;
    @FXML
    private ListView<Library> listView;

    @FXML
    private Button createPlaylist;


    public PlaylistNavigatorView() {
    }

    /**
     * Listener interface for handling events in the PlaylistNavigatorView.
     * Implement this interface to define behavior for opening dialogs, setting the selected playlist,
     * displaying a context menu, and providing the controller.
     */
    public interface PlaylistNavigatorViewListener {
        void openCreatePlaylistDialog();

        void setSelectedLibrary(Library library);

        void showContextMenu(double x, double y, Library library);

        PlaylistNavigatorController getController();
    }

    /**
     * Sets listener.
     *
     * @param _listener the listener
     */
    public void setListener(PlaylistNavigatorViewListener _listener) {
        listener = _listener;
    }


    /**
     * Initializes the PlaylistNavigatorView.
     * This method sets up the list view, button actions, and context menu for the view.
     */
    @Override
    public void init() {
        initListView();
        enableClickToSelect();
        setButtonActions();
        initTranslation();
    }

    /**
     * Initializes the list view for displaying playlists.
     * This method sets the cell factory for the list view to use a custom PlaylistCell.
     */
    private void initListView() {
        listView.setCellFactory(_ -> new PlaylistCell(new PlaylistCellController(listener.getController())));
    }

    /**
     * Sets up the button actions for creating a new playlist.
     * This method binds the button action to open a dialog for creating a new playlist.
     */
    private void setButtonActions() {
        createPlaylist.setOnAction(_ -> listener.openCreatePlaylistDialog());
    }

    /**
     * Initializes the translation for the UI elements.
     * This method binds the button text to the appropriate language string.
     */
    private void initTranslation() {
        createPlaylist.textProperty().bind(Bindings.createStringBinding(
                () -> LanguageManager.getInstance().get("button.create_playlist"),
                LanguageManager.getInstance().getLanguageProperty()
        ));
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
                if (getSelectedPlaylist() == null) return;
                listener.setSelectedLibrary(listView.getSelectionModel().getSelectedItem());
            } else if (e.getButton() == MouseButton.SECONDARY) {
                System.out.println("Right click on playlist");
                listener.showContextMenu(e.getScreenX(), e.getScreenY(), getSelectedPlaylist());
            }
        });
    }

    /**
     * Retrieves the currently selected playlist from the list view.
     *
     * @return The selected Library object or {@code null} if none is selected.
     */
    public Library getSelectedPlaylist() {
        return listView.getSelectionModel().getSelectedItem();
    }
}
