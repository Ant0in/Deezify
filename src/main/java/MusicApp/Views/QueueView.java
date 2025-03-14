package MusicApp.Views;

import MusicApp.Controllers.QueueController;
import MusicApp.Models.Song;
import MusicApp.utils.LanguageManager;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.*;

public class QueueView extends View<QueueView, QueueController> {
    @FXML
    private ListView<Song> queueListView;
    @FXML
    private Button addSongButton, deleteSongButton, clearQueueButton;

    public QueueView() {
    }


    @Override
    public void init() {
        initBindings();
        updateQueueListView();
        enableQueueDragAndDrop();
        setupListSelectionListeners();
        enableDoubleClickToPlay();
        initTranslation();
        setButtonActions();
    }


    private void setButtonActions() {
        addSongButton.setOnAction(event -> this.viewController.handleAddSong());
        deleteSongButton.setOnAction(event -> this.viewController.handleDeleteSong());
        clearQueueButton.setOnAction(event -> this.viewController.handleClearQueue());
    }

    private void initBindings() {
        bindButtons();
        bindQueueButtonsActivation();
    }

    /**
     * Bind the buttons.
     */
    private void bindButtons(){
        deleteSongButton.visibleProperty().bind(queueListView.getSelectionModel().selectedItemProperty().isNotNull());
        addSongButton.visibleProperty().bind(viewController.isPlaylistItemSelected());
    }

    private void bindQueueButtonsActivation() {
        addSongButton.disableProperty().bind(viewController.isPlaylistItemSelected().not());
        deleteSongButton.disableProperty().bind(queueListView.getSelectionModel().selectedItemProperty().isNull());
        clearQueueButton.disableProperty().bind(Bindings.isEmpty(queueListView.getItems()));

        applyDisableStyleListener(addSongButton);
        applyDisableStyleListener(deleteSongButton);
        applyDisableStyleListener(clearQueueButton);
    }

    private void applyDisableStyleListener(Control control) {
        control.disableProperty().addListener((obs, wasDisabled, isDisabled) -> {
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
     * Update the queue list view.
     */
    public void updateQueueListView() {
        queueListView.getItems().setAll(viewController.queueToList());
    }

    private void enableQueueDragAndDrop() {
        queueListView.setCellFactory(lv -> {
            ListCell<Song> cell = new ListCell<Song>() {
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
            Song draggedSong = queueListView.getItems().stream()
                    .filter(song -> song.getSongName().equals(db.getString()))
                    .findFirst()
                    .orElse(null);

            if (draggedSong != null) {
                int draggedIndex = queueListView.getItems().indexOf(draggedSong);
                int dropIndex = cell.getIndex();

                if (draggedIndex != dropIndex) {
                    viewController.reorderQueue(draggedIndex, dropIndex);
                    updateQueueListView();
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
        queueListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) viewController.clearPlayListViewSelection();
        });
    }

    /**
     * Enable double click to play
     */
    public void enableDoubleClickToPlay(){

        queueListView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                viewController.handlePlaySong();
            }
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

    public int getSelectedSongIndex() {
        return queueListView.getSelectionModel().getSelectedIndex();
    }

    public void clearSelection() {
        queueListView.getSelectionModel().clearSelection();
    }

    public void refreshUI(){
        initTranslation();
    }
}
