package musicApp.views.playlists;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import musicApp.services.LanguageService;
import musicApp.controllers.playlists.PlaylistContextMenuController;
import musicApp.views.View;
import javafx.scene.Node;

public class PlaylistContextMenuView extends View<PlaylistContextMenuView, PlaylistContextMenuController> {
    @FXML
    private ContextMenu contextMenu;

    @FXML
    private MenuItem addToQueueItem, replaceQueueItem, editItem, deleteItem;

    /**
     * Initialize the view.
     */
    @Override
    public void init() {
        refreshTranslation();
        initActions();
    }

    /**
     * Initializes the translations for the context menu items.
     */
    @Override
    protected void refreshTranslation() {
        LanguageService languageService = LanguageService.getInstance();
        addToQueueItem.setText(languageService.get("context_menu.add_to_queue"));
        replaceQueueItem.setText(languageService.get("context_menu.replace_queue"));
        editItem.setText(languageService.get("button.edit"));
        deleteItem.setText(languageService.get("button.delete"));
    }

    /**
     * Initializes the actions for the context menu items.
     */
    private void initActions() {
        addToQueueItem.setOnAction(_ -> viewController.appendToQueue());
        replaceQueueItem.setOnAction(_ -> viewController.replaceQueue());
        editItem.setOnAction(_ -> viewController.editPlaylist());
        deleteItem.setOnAction(_ -> viewController.deletePlaylist());
    }

    /**
     * Shows the context menu at the specified coordinates.
     *
     * @param node The node to show the context menu on.
     * @param x    The x coordinate to show the context menu at.
     * @param y    The y coordinate to show the context menu at.
     */
    public void show(Node node, double x, double y) {
        contextMenu.show(node, x, y);
    }
}
