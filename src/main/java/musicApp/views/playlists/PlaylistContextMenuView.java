package musicApp.views.playlists;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import musicApp.services.LanguageService;
import musicApp.views.View;

public class PlaylistContextMenuView extends View {

    private PlaylistContextMenuViewListener listener;

    @FXML
    private ContextMenu contextMenu;

    @FXML
    private MenuItem addToQueueItem, replaceQueueItem, editItem, deleteItem;

    /**
     * Sets listener.
     *
     * @param newListener the listener
     */
    public void setListener(PlaylistContextMenuViewListener newListener) {
        listener = newListener;
    }

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
        addToQueueItem.setOnAction(_ -> listener.handleAppendToQueue());
        replaceQueueItem.setOnAction(_ -> listener.handleReplaceQueue());
        editItem.setOnAction(_ -> listener.handleEditPlaylist());
        deleteItem.setOnAction(_ -> listener.handleDeletePlaylist());
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

    /**
     * Listener interface for handling context menu actions related to playlists.
     * Implement this interface to define behavior for deleting, editing,
     * appending to, or replacing the queue.
     */
    public interface PlaylistContextMenuViewListener {
        void handleDeletePlaylist();

        void handleEditPlaylist();

        void handleAppendToQueue();

        void handleReplaceQueue();
    }
}
