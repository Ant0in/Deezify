package MusicApp.Views;

import MusicApp.utils.LanguageManager;
import MusicApp.Models.Song;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.beans.binding.Bindings;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import MusicApp.Controllers.PlayerController;
import javafx.event.ActionEvent;
import javafx.stage.StageStyle;
import javafx.scene.paint.Color;


import java.io.IOException;
import java.net.URL;
import java.util.Objects;


/**
 * PlayerView
 * Class that represents the view of the music player.
 */
public class PlayerView extends View {

    private PlayerController playerController;

    @FXML
    private ListView<Song> playListView, queueListView;
    @FXML
    private Button addSongButton, deleteSongButton, clearQueueButton;
    @FXML
    private TextField songInput;
    @FXML
    private VBox controls;

    @FXML
    private BorderPane labelContainer;



    /* To enable drag */
    private double xOffset = 0;
    private double yOffset = 0;

    public PlayerView() throws IOException {
    }

    public void setPlayerController(PlayerController playerController) {
        this.playerController = playerController;
    }

    /**
     * Initialize the FXML scene.
     * @param fxmlPath The path to the FXML file.
     * @throws IOException If an error occurs while loading the FXML file.
     */
    public void initializeScene(String fxmlPath) throws IOException {
        URL url = PlayerView.class.getResource(fxmlPath);
        FXMLLoader loader = new FXMLLoader(url);
        loader.setController((Object) this);
        Pane root = loader.load();
        this.scene = new Scene(root);
    }

    /**
     * Initialize the view.
     */
    @FXML
    public void initialize(){
        initPanes();
        initBindings();
        initSongInput();
        initPlayListView();
        updatePlayListView();
        updateQueueListView();
        enableQueueDragAndDrop();
        setupListSelectionListeners();
        enableDoubleClickToPlay();
        setupListSelectionListeners();
        initTranslation();
    }

    private void initPanes(){
        initControlPanel();
        initToolBar();
    }

    private void initControlPanel(){
        Pane controlPanel = playerController.getControlPanelRoot();
        controls.getChildren().setAll(controlPanel);
    }

    private void initToolBar(){
        Pane toolBarPane = playerController.getToolBarRoot();
        labelContainer.setTop(toolBarPane);
    }



    /**
     * Initialize the translations of the texts in the view.
     */
    private void initTranslation() {
        addSongButton.setText(LanguageManager.get("button.add"));
        deleteSongButton.setText(LanguageManager.get("button.delete"));
        clearQueueButton.setText(LanguageManager.get("button.clear"));
        songInput.setPromptText(LanguageManager.get("search"));
    }

    /**
     * Initialize the bindings between the view and the controller.
     */
    private void initBindings() {
        bindButtons();
        bindPlayingSongAnchor();
        bindQueueButtonsActivation();
    }

    /**
     * Bind the buttons.
     */
    private void bindButtons(){
        deleteSongButton.visibleProperty().bind(queueListView.getSelectionModel().selectedItemProperty().isNotNull());
        addSongButton.visibleProperty().bind(playListView.getSelectionModel().selectedItemProperty().isNotNull());
    }

    /**
     * Bind the visibility of the playing song anchor (the controls at the bottom).
     */
    private void bindPlayingSongAnchor(){
        controls.setVisible(true);
    }

    private void bindQueueButtonsActivation() {
        addSongButton.disableProperty().bind(playListView.getSelectionModel().selectedItemProperty().isNull());
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
     * Initialize the song input for the search
     */
    private void initSongInput() {
        songInput.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.isEmpty()) {
                playListView.getItems().setAll(playerController.searchLibrary(newVal));
            } else {
                updatePlayListView();
            }
        });
    }

    /**
     * Initialize the playlist view.
     */
    private void initPlayListView() {
        playListView.setCellFactory(lv -> new SongCell(playerController));
        updatePlayListView();
    }

    /**
     * Enable double click to play
     */
    public void enableDoubleClickToPlay(){
        playListView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                handlePlaySong();
            }
        });
        queueListView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                handlePlaySong();
            }
        });
    }

    /**
     * Enable double click to grow (fullscreen)
     */
    public void enableDoubleClickToGrow(Stage stage){
        labelContainer.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                stage.setFullScreen(true);
            }
        });
    }

    /**
     * Setup the window close handler.
     * @param stage The stage to setup the handler for.
     */
    private void setupWindowCloseHandler(Stage stage) {
        stage.setOnCloseRequest(e -> {
            playerController.close();
            Platform.exit();
        });
    }

    /**
     * Enable drag of the window.
     */
    private void enableDrag(Stage stage) {
        labelContainer.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        labelContainer.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    /**
     * Get the title of the application.
     * @return The title of the application.
     */
    public String getTitle() {
        return LanguageManager.get("app.title");
    }

    /**
     * Show the stage.
     * @param stage The stage to show.
     */
    public void show(Stage stage) {
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(this.scene);
        stage.setTitle(getTitle());
        enableDrag(stage);
        enableDoubleClickToGrow(stage);
        setupWindowCloseHandler(stage);
        stage.show();
    }


    /**
     * Setup the list selection listeners.
     */
    private void setupListSelectionListeners() {
        playListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) queueListView.getSelectionModel().clearSelection();
        });

        queueListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) playListView.getSelectionModel().clearSelection();
        });
    }

    /**
     * Update the play list view.
     */
    public void updatePlayListView() {
        playListView.getItems().setAll(playerController.getLibrary().toList());
    }

    /**
     * Update the queue list view.
     */
    public void updateQueueListView() {
        queueListView.getItems().setAll(playerController.getQueue().toList());
    }


    /**
     * Clears the selection in both the queue and playlist ListViews.
     */

    private void clearSelections(){
        queueListView.getSelectionModel().clearSelection();
        playListView.getSelectionModel().clearSelection();
    }

    /**
     * Handle the play song button.
     */
    @FXML
    private void handlePlaySong() {
        int songIndexFromQueue = queueListView.getSelectionModel().getSelectedIndex();
        int songIndexFromLibrary = playListView.getSelectionModel().getSelectedIndex();
        if (songIndexFromQueue!=-1){
            System.out.println("The selected song index : " + songIndexFromQueue);
            playerController.playFromQueue(songIndexFromQueue);
        }else if (songIndexFromLibrary != -1){
            playerController.playFromLibrary(songIndexFromLibrary);
        }else{
            System.out.println("No song selected.");
        }
        updateQueueListView();
        clearSelections();
    }

    /**
     * Handle the add song button.
     */
    @FXML
    private void handleAddSong() {
        int index = playListView.getSelectionModel().getSelectedIndex();
        Song selectedSong = playerController.getFromLibrary(index);
        if (index != -1) {
            playerController.addToQueue(selectedSong);
            updateQueueListView();
        }
    }

    /**
     * Handle the delete song button.
     */
    @FXML
    private void handleDeleteSong() {
        int index = queueListView.getSelectionModel().getSelectedIndex();
        if (index != -1) {
            playerController.removeFromQueue(playerController.getQueue().get(index));
            updateQueueListView();
        }
    }

    /**
     * Handle the clear queue button.
     */
    @FXML
    private void handleClearQueue() {
        playerController.clearQueue();
        updateQueueListView();
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
                    playerController.reorderQueue(draggedIndex, dropIndex);
                    updateQueueListView();
                }

                event.setDropCompleted(true);
            }
        }
        event.consume();
    }


    /**
     * Change the language of the application.
     * @param languageCode The language code. (e.g. "en", "fr", "nl")
     */
    private void switchLanguage(String languageCode) {
        LanguageManager.setLanguage(languageCode);
        refreshUI();
    }

    /**
     * Refresh the UI.
     */
    public void refreshUI() {
        initTranslation();
        Stage stage = (Stage) scene.getWindow();
        stage.setTitle(LanguageManager.get("app.title"));
    }
}
