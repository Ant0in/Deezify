package musicApp.views.playlists;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import musicApp.controllers.playlists.PlaylistCellController;
import musicApp.controllers.playlists.PlaylistNavigatorController;
import musicApp.models.Library;
import musicApp.services.LanguageService;
import musicApp.views.View;

import java.util.List;

/**
 * The PlaylistNavigatorView class is responsible for displaying and managing the playlist navigator UI.
 * It allows users to create, edit, and delete playlists, as well as manage their contents.
 */
public class PlaylistNavigatorView extends View<PlaylistNavigatorView, PlaylistNavigatorController> {

    @FXML
    private ListView<Library> listView;

    @FXML
    private Button createPlaylist;

    /**
     * Initializes the PlaylistNavigatorView.
     * This method sets up the list view, button actions, and context menu for the view.
     */
    @Override
    public void init() {
        initListView();
        enableClickToSelect();
        setButtonActions();
        refreshTranslation();
    }

    /**
     * Initializes the list view for displaying playlists.
     * This method sets the cell factory for the list view to use a custom PlaylistCell.
     */
    private void initListView() {
        listView.setCellFactory(_ -> new PlaylistCell(new PlaylistCellController(viewController)));
    }

    /**
     * Sets up the button actions for creating a new playlist.
     * This method binds the button action to open a dialog for creating a new playlist.
     */
    private void setButtonActions() {
        createPlaylist.setOnAction(_ -> viewController.openCreatePlaylistDialog());
    }

    /**
     * Initializes the translation for the UI elements.
     * This method binds the button text to the appropriate language string.
     */
    @Override
    protected void refreshTranslation() {
        createPlaylist.setText(LanguageService.getInstance().get("button.create_playlist"));
    }

    /**
     * Updates the list view with the provided list of libraries.
     * This method clears the current items in the list view and adds the new libraries.
     *
     * @param libraries The list of libraries to display in the list view.
     */
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
                viewController.setSelectedLibrary(listView.getSelectionModel().getSelectedItem());
            } else if (e.getButton() == MouseButton.SECONDARY) {
                System.out.println("Right click on playlist");
                viewController.showContextMenu(e.getScreenX(), e.getScreenY(), getSelectedPlaylist());
            }
        });
    }

    public Library getSelectedPlaylist() {
        return listView.getSelectionModel().getSelectedItem();
    }
}
