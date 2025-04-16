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

    public interface SongContextMenuViewListener {
        void handleEditMetadata();
        boolean isShowingMainLibrary();
        void handleRemoveFromPlaylist();
        void populatePlaylistMenuItems(Menu addToMenu, Menu removeFromMenu);
    }

    /**
     * Sets listener.
     *
     * @param listener the listener
     */
    public void setListener(SongContextMenuViewListener listener) {
        this.listener = listener;
    }

    @Override
    public void init() {
        initContextMenu();
        initTranslation();
    }

    private void initTranslation() {
        editMetadataItem.setText(LanguageManager.getInstance().get("button.edit_metadata"));
        addToPlaylistMenu.setText(LanguageManager.getInstance().get("button.add_to_playlist"));
        if (removeFromPlaylistMenu instanceof Menu) {
            ((Menu) removeFromPlaylistMenu).setText(LanguageManager.getInstance().get("button.remove_from_playlist"));
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
            ((MenuItem) removeFromPlaylistMenu).setOnAction(_ -> listener.handleRemoveFromPlaylist());
        }
        contextMenu.setOnShowing(e -> updateMenuItems());
    }


    private void clearPlaylistMenuItems() {
        addToPlaylistMenu.getItems().clear();
        if (removeFromPlaylistMenu instanceof Menu) {
            ((Menu) removeFromPlaylistMenu).getItems().clear();
        }
    }

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