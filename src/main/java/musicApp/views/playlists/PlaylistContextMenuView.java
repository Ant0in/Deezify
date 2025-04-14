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
        initTranslations();
        initActions();
    }

    /**
     * Initializes the translations for the context menu items.
     */
    private void initTranslations() {
        LanguageService languageService = LanguageService.getInstance();
        addToQueueItem.textProperty().bind(Bindings.createStringBinding(
                () -> languageService.get("context_menu.append_to_queue"),
                languageService.getLanguageProperty()));
        replaceQueueItem.textProperty().bind(Bindings.createStringBinding(
                () -> languageService.get("context_menu.replace_queue"), languageService.getLanguageProperty()));
        editItem.textProperty().bind(Bindings.createStringBinding(
                () -> languageService.get("button.edit"), languageService.getLanguageProperty()));
        deleteItem.textProperty().bind(Bindings.createStringBinding(
                () -> languageService.get("button.delete"), languageService.getLanguageProperty()));
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

    public void show(Node node, double x, double y) {
        contextMenu.show(node, x, y);
    }

    public void refreshTranslations() {
        initTranslations();
    }
}
