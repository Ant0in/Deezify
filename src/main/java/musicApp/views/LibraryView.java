package musicApp.views;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import musicApp.controllers.LibraryController;
import musicApp.controllers.songs.SongCellController;
import musicApp.models.Song;
import musicApp.services.LanguageService;
import musicApp.views.songs.SongCell;

import java.util.List;

/**
 * The MainLibrary view.
 */
@SuppressWarnings("unused")
public class LibraryView extends SongContainerView {

    private LibraryViewListener listener;

    @FXML
    private TextField songInput;

    @FXML
    private Button addSongButton;

    /**
     * Listener interface used to delegate actions from the view to the controller logic.
     */
    public interface LibraryViewListener {
        void handleAddSong();

        void clearQueueSelection();

        List<Song> searchLibrary(String query);

        LibraryController getController();
    }

    /**
     * Sets listener.
     *
     * @param newListener the listener
     */
    public void setListener(LibraryViewListener newListener) {
        listener = newListener;
    }


    @Override
    public void init() {
        initSongInput();
        initButtons();
        initPlayListView();
        updateListView();
        setupListSelectionListeners();
        enableDoubleClickToPlay();
        refreshTranslation();
    }

    /**
     * Initialize the song input for the search
     */
    private void initSongInput() {
        songInput.textProperty().addListener((_, _, newVal) -> {
            if (newVal == null || newVal.isEmpty()) {
                updateListView();
            } else {
                listView.getItems().setAll(listener.searchLibrary(newVal));
            }
        });
    }



    /**
     * Initialize the buttons in the view.
     */
    private void initButtons() {
        addSongButton.setOnAction(event -> listener.handleAddSong());
    }

    /**
     * Initialize the translations of the texts in the view.
     */
    @Override
    protected void refreshTranslation() {
        songInput.setPromptText(LanguageService.getInstance().get("search"));
    }


    /**
     * Initialize the playlist view.
     */
    private void initPlayListView() {
        listView.setCellFactory(_ -> new SongCell(new SongCellController(listener.getController())));
        updateListView();
    }

    /**
     * Setup the list selection listeners.
     */
    private void setupListSelectionListeners() {
        listView.getSelectionModel().selectedItemProperty().addListener((_, _, newVal) -> {
            if (newVal != null) {
                listener.clearQueueSelection();
            }
        });
    }

    @Override
    public void refreshUI() {
        updateListView();
    }

}
