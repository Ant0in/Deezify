package musicApp.views;

import musicApp.controllers.QueueController;
import musicApp.models.Song;
import musicApp.utils.LanguageManager;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.ListCell;
import javafx.scene.input.*;

/**
 * The Queue view.
 */
@SuppressWarnings("unused")
public class QueueView extends PlayListView<QueueView, QueueController> {

    @FXML
    private Button addSongButton, deleteSongButton, clearQueueButton;

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
        addSongButton.setOnAction(_ -> this.viewController.handleAddSong());
        deleteSongButton.setOnAction(_ -> this.viewController.handleDeleteSong());
        clearQueueButton.setOnAction(_ -> this.viewController.handleClearQueue());
    }

    private void initBindings() {
        bindButtons();
        bindQueueButtonsActivation();
    }

    /**
     * Bind the buttons.
     */
    private void bindButtons(){
        deleteSongButton.visibleProperty().bind(isSelected());
        addSongButton.visibleProperty().bind(viewController.isPlaylistItemSelected());
    }

    private void bindQueueButtonsActivation() {
        addSongButton.disableProperty().bind(viewController.isPlaylistItemSelected().not());
        deleteSongButton.disableProperty().bind(listView.getSelectionModel().selectedItemProperty().isNull());
        clearQueueButton.disableProperty().bind(Bindings.isEmpty(listView.getItems()));

        applyDisableStyleListener(addSongButton);
        applyDisableStyleListener(deleteSongButton);
        applyDisableStyleListener(clearQueueButton);
    }

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


    private void enableQueueDragAndDrop() {
        listView.setCellFactory(_ -> {
            ListCell<Song> cell = new ListCell<>() {
                @Override
                protected void updateItem(Song item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item.getSongName());
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
     * @param event The mouse event.
     * @param cell The list cell.
     */
    private void onDragDetected(MouseEvent event, ListCell<Song> cell) {
        if (!cell.isEmpty()) {
            Dragboard db = cell.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(cell.getItem().getSongName());
            db.setContent(content);
            event.consume();
        }
    }

    /**
     * Handle the drag over event.
     * @param event The drag event.
     * @param cell The list cell.
     */
    private void onDragOver(DragEvent event, ListCell<Song> cell) {
        if (event.getGestureSource() != cell && event.getDragboard().hasString()) {
            event.acceptTransferModes(TransferMode.MOVE);
        }
        event.consume();
    }

    /**
     * Handle the drag dropped event.
     * @param event The drag event.
     * @param cell The list cell.
     */
    private void onDragDropped(DragEvent event, ListCell<Song> cell) {
        Dragboard db = event.getDragboard();
        if (db.hasString()) {
            Song draggedSong = listView.getItems().stream()
                    .filter(song -> song.getSongName().equals(db.getString()))
                    .findFirst()
                    .orElse(null);

            if (draggedSong != null) {
                int draggedIndex = listView.getItems().indexOf(draggedSong);
                int dropIndex = cell.getIndex();

                if (draggedIndex != dropIndex) {
                    viewController.reorderQueue(draggedIndex, dropIndex);
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
            if (newVal != null) viewController.clearPlayListViewSelection();
        });
    }


    /**
     * Initialize the translations of the texts in the view.
     */
    private void initTranslation() {
        addSongButton.setText(LanguageManager.get("button.add"));
        deleteSongButton.setText(LanguageManager.get("button.delete"));
        clearQueueButton.setText(LanguageManager.get("button.clear"));
    }


    /**
     * Refresh ui.
     */
    public void refreshUI(){
        initTranslation();
    }
}
