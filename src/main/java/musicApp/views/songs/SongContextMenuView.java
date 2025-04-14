package musicApp.views.songs;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import musicApp.controllers.songs.SongContextMenuController;
import musicApp.services.LanguageService;
import musicApp.views.View;
import musicApp.exceptions.BadSongException;

/**
 * View for the context menu that appears when right-clicking a song in the library.
 */
public class SongContextMenuView extends View<SongContextMenuView, SongContextMenuController> {

    @FXML
    private ContextMenu contextMenu;
    @FXML
    private Menu addToPlaylistMenu;
    @FXML
    private MenuItem removeFromPlaylistMenu, editMetadataItem; // Can be Menu or MenuItem

    @Override
    public void init() {
        initContextMenu();
        refreshTranslation();
    }

    @Override
    protected void refreshTranslation() {
        editMetadataItem.setText(LanguageService.getInstance().get("button.edit_metadata"));
        addToPlaylistMenu.setText(LanguageService.getInstance().get("button.add_to_playlist"));
        if (removeFromPlaylistMenu instanceof Menu) {
            ((Menu) removeFromPlaylistMenu).setText(LanguageService.getInstance().get("button.remove_from_playlist"));
        } else {
            removeFromPlaylistMenu.setText(LanguageService.getInstance().get("button.remove_from_playlist"));
        }
    }

    /**
     * initContextMenu initializes the actions for the context menu items.
     */
    private void initContextMenu() {
        editMetadataItem.setOnAction(_ -> viewController.handleEditMetadata());

        if (viewController.isShowingMainLibrary()) {
            removeFromPlaylistMenu = new Menu();
            updateMenuItems();
            contextMenu.getItems().remove(2);
            contextMenu.getItems().add(removeFromPlaylistMenu);

        } else {
            ((MenuItem) removeFromPlaylistMenu).setOnAction(_ -> viewController.handleRemoveFromPlaylist());
        }
        contextMenu.setOnShowing(e -> updateMenuItems());

        MenuItem launchDjMode = new MenuItem("DJ Mode");
        launchDjMode.setOnAction(_ -> {
            try {
                viewController.launchDjMode();
            } catch (BadSongException e) {
                e.printStackTrace(); // Handle the exception appropriately
            }
        });
        contextMenu.getItems().add(launchDjMode);

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
        viewController.populatePlaylistMenuItems(
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
}