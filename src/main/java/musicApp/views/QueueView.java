package musicApp.views;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.ListCell;
import javafx.scene.input.*;
import musicApp.controllers.QueueController;
import musicApp.models.Library;
import musicApp.models.Song;
import musicApp.utils.LanguageManager;

import java.util.List;

/**
 * The Queue view.
 */
@SuppressWarnings("unused")
public class QueueView extends SongContainerView {
    private QueueViewListener listener;

    @FXML
    private Button addSongButton, deleteSongButton, clearQueueButton;


    public interface QueueViewListener {
        BooleanBinding isPlaylistItemSelected();
        void reorderQueue(int fromIndex, int toIndex);
        void clearPlayListViewSelection();
        void handleAddSong();
        void handleDeleteSong();
        void handleClearQueue();
    }

    /**
     * Sets listener.
     *
     * @param listener the listener
     */
    public void setListener(QueueViewListener listener) {
        this.listener = listener;
    }

    /**
     * Instantiates a new Queue view.
     */
    public QueueView() {
    }


    @Override
    public void init() {
        initBindings();
        updateListView();
        enableQueueDragAndDrop();
        setupListSelectionListeners();
        enableDoubleClickToPlay();
        initTranslation();
        setButtonActions();
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


    /**
     * Initialize the translations of the texts in the view.
     */
    protected void initTranslation() {
        LanguageManager lm = LanguageManager.getInstance();
        addSongButton.textProperty().bind(Bindings.createStringBinding(
                () -> lm.get("button.add"), lm.getLanguageProperty()
        ));
        deleteSongButton.textProperty().bind(Bindings.createStringBinding(
                () -> lm.get("button.delete"), lm.getLanguageProperty()
        ));
        clearQueueButton.textProperty().bind(Bindings.createStringBinding(
                () -> lm.get("button.clear"), lm.getLanguageProperty()
        ));
    }

}
