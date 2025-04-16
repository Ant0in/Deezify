package musicApp.views.songs;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import musicApp.utils.LanguageManager;
import musicApp.views.View;

/**
 * View for the context menu that appears when right-clicking a song in the library.
 */
public class SongContextMenuView extends View {

    private SongContextMenuViewListener listener;

    @FXML
    private ContextMenu contextMenu;
    @FXML
    private Menu addToPlaylistMenu;
    @FXML
    private MenuItem removeFromPlaylistMenu, editMetadataItem; // Can be Menu or MenuItem

    /**
     * Interface for the controller/logic layer to respond to user actions triggered
     * through the context menu.
     */
    public interface SongContextMenuViewListener {
        void handleEditMetadata();

        boolean isShowingMainLibrary();

        void handleRemoveFromPlaylist();

        void populatePlaylistMenuItems(Menu addToMenu, Menu removeFromMenu);
    }

    /**
     * Sets listener.
     *
     * @param _listener the listener
     */
    public void setListener(SongContextMenuViewListener _listener) {
        listener = _listener;
    }

    /**
     * Initialize the view and set up all relevant components.
     */
    @Override
    public void init() {
        initContextMenu();
        initTranslation();
    }

    /**
     * Set the translated text for all context menu items based on the current language.
     */
    private void initTranslation() {
        editMetadataItem.setText(LanguageManager.getInstance().get("button.edit_metadata"));
        addToPlaylistMenu.setText(LanguageManager.getInstance().get("button.add_to_playlist"));
        if (removeFromPlaylistMenu instanceof Menu) {
            removeFromPlaylistMenu.setText(LanguageManager.getInstance().get("button.remove_from_playlist"));
        } else {
            removeFromPlaylistMenu.setText(LanguageManager.getInstance().get("button.remove_from_playlist"));
        }
    }

    /**
     * initContextMenu initializes the actions for the context menu items.
     */
    private void initContextMenu() {
        editMetadataItem.setOnAction(_ -> listener.handleEditMetadata());

        if (listener.isShowingMainLibrary()) {
            removeFromPlaylistMenu = new Menu();
            updateMenuItems();
            contextMenu.getItems().remove(2);
            contextMenu.getItems().add(removeFromPlaylistMenu);

        } else {
            removeFromPlaylistMenu.setOnAction(_ -> listener.handleRemoveFromPlaylist());
        }
        contextMenu.setOnShowing(e -> updateMenuItems());
    }

    /**
     * Clears the dynamic playlist items from the submenus.
     */
    private void clearPlaylistMenuItems() {
        addToPlaylistMenu.getItems().clear();
        if (removeFromPlaylistMenu instanceof Menu) {
            ((Menu) removeFromPlaylistMenu).getItems().clear();
        }
    }

    /**
     * Checks if the "Add to Playlist" menu currently has no items.
     *
     * @return true if empty, false otherwise
     */
    private boolean addToPlaylistMenuIsEmpty() {
        return addToPlaylistMenu.getItems().isEmpty();
    }

    /**
     * Update the menu items in the context menu.
     * This method is called when the context menu is shown.
     */
    private void updateMenuItems() {
        clearPlaylistMenuItems();
        listener.populatePlaylistMenuItems(
                addToPlaylistMenu,
                removeFromPlaylistMenu instanceof Menu ? (Menu) removeFromPlaylistMenu : null
        );

        if (addToPlaylistMenuIsEmpty()) {
            addToPlaylistMenu.getItems().add(new SeparatorMenuItem());
        }
    }

    /**
     * Get the context menu.
     */
    public ContextMenu getContextMenu() {
        return contextMenu;
    }

    /**
     * Show the context menu at the specified coordinates.
     *
     * @param node The node to show the context menu for.
     * @param x    The x coordinate to show the context menu at.
     * @param y    The y coordinate to show the context menu at.
     */
    public void show(Node node, double x, double y) {
        contextMenu.show(node, x, y);
    }

    /**
     * Refresh the translation of the context menu.
     */
    public void refreshTranslation() {
        initTranslation();
    }
}