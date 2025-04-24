package musicApp.views;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.ListCell;
import javafx.scene.input.*;
import musicApp.models.Song;
import musicApp.services.LanguageService;

/**
 * The Queue view.
 */
public class QueueView extends SongContainerView {

    private QueueViewListener listener;

    @FXML
    private Button addSongButton, deleteSongButton, clearQueueButton;

    /**
     * Listener interface for handling user actions from the controller.
     */
    public interface QueueViewListener {
        void handleAddSong();

        void handleDeleteSong();

        void handleClearQueue();

        void clearPlayListViewSelection();

        void reorderQueue(int fromIndex, int toIndex);

        BooleanBinding isPlaylistItemSelected();
    }

    /**
     * Sets listener.
     *
     * @param newListener the listener
     */
    public void setListener(QueueViewListener newListener) {
        listener = newListener;
    }


    @Override
    public void init() {
        initBindings();
        updateListView();
        enableQueueDragAndDrop();
        setupListSelectionListeners();
        enableDoubleClickToPlay();
        setButtonActions();
        refreshTranslation();
    }

    private void setButtonActions() {
        addSongButton.setOnAction(_ -> listener.handleAddSong());
        deleteSongButton.setOnAction(_ -> listener.handleDeleteSong());
        clearQueueButton.setOnAction(_ -> listener.handleClearQueue());
    }

    private void initBindings() {
        bindButtons();
        bindQueueButtonsActivation();
    }

    /**
     * Bind the buttons.
     */
    private void bindButtons() {
        deleteSongButton.visibleProperty().bind(isSelected());
        addSongButton.visibleProperty().bind(listener.isPlaylistItemSelected());
    }

    /**
     * Bind the queue buttons activation.
     */
    private void bindQueueButtonsActivation() {
        addSongButton.disableProperty().bind(listener.isPlaylistItemSelected().not());
        deleteSongButton.disableProperty().bind(listView.getSelectionModel().selectedItemProperty().isNull());
        clearQueueButton.disableProperty().bind(Bindings.isEmpty(listView.getItems()));

        applyDisableStyleListener(addSongButton);
        applyDisableStyleListener(deleteSongButton);
        applyDisableStyleListener(clearQueueButton);
    }

    /**
     * Apply the disable style listener to the button.
     *
     * @param control The control to apply the listener to.
     */
    private void applyDisableStyleListener(Control control) {
        control.disableProperty().addListener((_, _, isDisabled) -> {
            if (isDisabled) {
                if (!control.getStyleClass().contains("disabled-btn")) {
                    control.getStyleClass().add("disabled-btn");
                }
            } else {
                control.getStyleClass().remove("disabled-btn");
            }
        });
    }

    /**
     * Enables drag and drop to reorder the queue.
     */
    private void enableQueueDragAndDrop() {
        listView.setCellFactory(_ -> {
            ListCell<Song> cell = new ListCell<>() {
                @Override
                protected void updateItem(Song item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item.getTitle());
                }
            };

            // Start of dragging
            cell.setOnDragDetected(event -> onDragDetected(event, cell));

            // Allows receiving the dragged element
            cell.setOnDragOver(event -> onDragOver(event, cell));

            // When the element is released
            cell.setOnDragDropped(event -> onDragDropped(event, cell));

            return cell;
        });
    }

    /**
     * Handle the drag detected event.
     *
     * @param event The mouse event.
     * @param cell  The list cell.
     */
    private void onDragDetected(MouseEvent event, ListCell<Song> cell) {
        if (!cell.isEmpty()) {
            Dragboard db = cell.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(cell.getItem().getTitle());
            db.setContent(content);
            event.consume();
        }
    }

    /**
     * Handle the drag over event.
     *
     * @param event The drag event.
     * @param cell  The list cell.
     */
    private void onDragOver(DragEvent event, ListCell<Song> cell) {
        if (event.getGestureSource() != cell && event.getDragboard().hasString()) {
            event.acceptTransferModes(TransferMode.MOVE);
        }
        event.consume();
    }

    /**
     * Handle the drag dropped event.
     *
     * @param event The drag event.
     * @param cell  The list cell.
     */
    private void onDragDropped(DragEvent event, ListCell<Song> cell) {
        Dragboard db = event.getDragboard();
        if (db.hasString()) {
            Song draggedSong = listView.getItems().stream()
                    .filter(song -> song.getTitle().equals(db.getString()))
                    .findFirst()
                    .orElse(null);

            if (draggedSong != null) {
                int draggedIndex = listView.getItems().indexOf(draggedSong);
                int dropIndex = cell.getIndex();

                if (draggedIndex != dropIndex) {
                    listener.reorderQueue(draggedIndex, dropIndex);
                    updateListView();
                }

                event.setDropCompleted(true);
            }
        }
        event.consume();
    }

    /**
     * Setup the list selection listeners.
     */
    private void setupListSelectionListeners() {
        listView.getSelectionModel().selectedItemProperty().addListener((_, _, newVal) -> {
            if (newVal != null) listener.clearPlayListViewSelection();
        });
    }

    @Override
    /**
     * Initialize the translations of the texts in the view.
     */
    protected void refreshTranslation() {
        LanguageService ls = LanguageService.getInstance();
        addSongButton.setText(ls.get("button.add"));
        deleteSongButton.setText(ls.get("button.delete"));
        clearQueueButton.setText(ls.get("button.clear"));
    }

}
