package musicApp.views.playlists;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import musicApp.utils.LanguageManager;
import musicApp.views.View;

public class PlaylistContextMenuView extends View {

    private PlaylistContextMenuListener listener;

    @FXML
    private ContextMenu contextMenu;

    @FXML
    private MenuItem addToQueueItem, replaceQueueItem, editItem, deleteItem;

    /**
     * Listener interface for handling context menu actions related to playlists.
     * Implement this interface to define behavior for deleting, editing,
     * appending to, or replacing the queue.
     */
    public interface PlaylistContextMenuListener {
        void deletePlaylist();

        void editPlaylist();

        void appendToQueue();

        void replaceQueue();
    }

    /**
     * Sets listener.
     *
     * @param _listener the listener
     */
    public void setListener(PlaylistContextMenuListener _listener) {
        listener = _listener;
    }


    /**
     * Initialize the view.
     */
    @Override
    public void init() {
        initTranslations();
        initActions();
    }

    /**
     * Initializes the translations for the context menu items.
     */
    private void initTranslations() {
        LanguageManager languageManager = LanguageManager.getInstance();
        addToQueueItem.textProperty().bind(Bindings.createStringBinding(
                () -> languageManager.get("context_menu.append_to_queue"),
                languageManager.getLanguageProperty()));
        replaceQueueItem.textProperty().bind(Bindings.createStringBinding(
                () -> languageManager.get("context_menu.replace_queue"), languageManager.getLanguageProperty()));
        editItem.textProperty().bind(Bindings.createStringBinding(
                () -> languageManager.get("button.edit"), languageManager.getLanguageProperty()));
        deleteItem.textProperty().bind(Bindings.createStringBinding(
                () -> languageManager.get("button.delete"), languageManager.getLanguageProperty()));
    }

    /**
     * Initializes the actions for the context menu items.
     */
    private void initActions() {
        addToQueueItem.setOnAction(_ -> listener.appendToQueue());
        replaceQueueItem.setOnAction(_ -> listener.replaceQueue());
        editItem.setOnAction(_ -> listener.editPlaylist());
        deleteItem.setOnAction(_ -> listener.deletePlaylist());
    }

    /**
     * Displays the context menu at the specified position relative to the given node.
     *
     * @param node The node relative to which the context menu is shown.
     * @param x    The x-coordinate for the context menu display.
     * @param y    The y-coordinate for the context menu display.
     */
    public void show(Node node, double x, double y) {
        contextMenu.show(node, x, y);
    }

}
