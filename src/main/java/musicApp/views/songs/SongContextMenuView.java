package musicApp.views.songs;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import musicApp.services.LanguageService;
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
     * Listener interface used to delegate actions from the view to the controller logic.
     */
    public interface SongContextMenuViewListener {
        void handleEditMetadata();

        void handleRemoveFromPlaylist();

        void populatePlaylistMenuItems(Menu addToMenu, Menu removeFromMenu);

        boolean isShowingMainLibrary();
    }

    /**
     * Sets listener.
     *
     * @param newListener the listener
     */
    public void setListener(SongContextMenuViewListener newListener) {
        listener = newListener;
    }
    
    @Override
    public void init() {
        initContextMenu();
        refreshTranslation();
    }

    @Override
    protected void refreshTranslation() {
        editMetadataItem.setText(LanguageService.getInstance().get("button.edit_metadata"));
        addToPlaylistMenu.setText(LanguageService.getInstance().get("button.add_to_playlist"));
        removeFromPlaylistMenu.setText(LanguageService.getInstance().get("button.remove_from_playlist"));
    }

    /**
     * initContextMenu initializes the actions for the context menu items.
     */
    private void initContextMenu() {
        editMetadataItem.setOnAction(_ -> listener.handleEditMetadata());

        if (listener.isShowingMainLibrary()) {
            removeFromPlaylistMenu = new Menu();
            updateMenuItems();
            contextMenu.getItems().set(2, removeFromPlaylistMenu);
        } else {
            removeFromPlaylistMenu.setOnAction(_ -> listener.handleRemoveFromPlaylist());
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
     * Show the context menu at the specified coordinates.
     *
     * @param node The node to show the context menu for.
     * @param x    The x coordinate to show the context menu at.
     * @param y    The y coordinate to show the context menu at.
     */
    public void show(Node node, double x, double y) {
        contextMenu.show(node, x, y);
    }
}