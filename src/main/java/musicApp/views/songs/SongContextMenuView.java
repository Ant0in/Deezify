package musicApp.views.songs;

import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import musicApp.controllers.songs.SongContextMenuController;
import musicApp.utils.LanguageManager;
import musicApp.views.View;

/**
 * View for the context menu that appears when right-clicking a song in the library.
 */
public class SongContextMenuView extends View<SongContextMenuView, SongContextMenuController> {

    private ContextMenu contextMenu;
    private Menu addToPlaylistMenu;
    private Object removeFromPlaylistMenu; // Can be Menu or MenuItem

    @Override
    public void init() {
        createContextMenu();
    }

    /**
     * Create the context menu.
     */
    private void createContextMenu() {
        contextMenu = new ContextMenu();

        MenuItem editMetadataItem = new MenuItem(LanguageManager.getInstance().get("button.edit_metadata"));
        editMetadataItem.setOnAction(_ -> viewController.handleEditMetadata());

        addToPlaylistMenu = new Menu(LanguageManager.getInstance().get("button.add_to_playlist"));

        if (viewController.isShowingMainLibrary()) {
            removeFromPlaylistMenu = new Menu(LanguageManager.getInstance().get("button.remove_from_playlist"));
        } else {
            removeFromPlaylistMenu = new MenuItem(LanguageManager.getInstance().get("button.remove_from_playlist"));
            ((MenuItem) removeFromPlaylistMenu).setOnAction(_ -> viewController.handleRemoveFromPlaylist());
        }

        contextMenu.setOnShowing(e -> updateMenuItems());

        contextMenu.getItems().addAll(editMetadataItem, addToPlaylistMenu);

        if (removeFromPlaylistMenu instanceof Menu) {
            contextMenu.getItems().add((Menu) removeFromPlaylistMenu);
        } else {
            contextMenu.getItems().add((MenuItem) removeFromPlaylistMenu);
        }

        MenuItem launchDjMode = new MenuItem("DJ Mode");
        launchDjMode.setOnAction(_ -> viewController.launchDjMode());
        contextMenu.getItems().add(launchDjMode);

    }

    private void updateMenuItems() {
        addToPlaylistMenu.getItems().clear();
        if (removeFromPlaylistMenu instanceof Menu) {
            ((Menu) removeFromPlaylistMenu).getItems().clear();
        }

        viewController.populatePlaylistMenuItems(
                addToPlaylistMenu,
                removeFromPlaylistMenu instanceof Menu ? (Menu) removeFromPlaylistMenu : null
        );

        if (!addToPlaylistMenu.getItems().isEmpty()) {
            addToPlaylistMenu.getItems().add(new SeparatorMenuItem());
        }
    }

    public ContextMenu getContextMenu() {
        return contextMenu;
    }

    public void show(Node node, double x, double y) {
        contextMenu.show(node, x, y);
    }

    public void refreshTranslation() {
        createContextMenu();
    }
}