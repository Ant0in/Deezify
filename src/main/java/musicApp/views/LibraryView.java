package musicApp.views;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import musicApp.controllers.LibraryController;
import musicApp.controllers.songs.SongCellController;
import musicApp.models.Song;
import musicApp.utils.LanguageManager;
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
     * Instantiates a new Main library view.
     */
    public LibraryView() {
    }

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
     * @param _listener the listener
     */
    public void setListener(LibraryViewListener _listener) {
        listener = _listener;
    }

    /**
     * Initializes the view and all its components.
     * This method must be called after FXML loading.
     */
    @Override
    public void init() {
        initSongInput();
        initButtons();
        initPlayListView();
        updateListView();
        initTranslation();
        setupListSelectionListeners();
        enableDoubleClickToPlay();
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
    protected void initTranslation() {
        songInput.promptTextProperty().bind(Bindings.createStringBinding(
                () -> LanguageManager.getInstance().get("search"),
                LanguageManager.getInstance().getLanguageProperty()
        ));
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

    /**
     * Refreshes the UI by reloading the song list.
     */
    @Override
    public void refreshUI() {
        updateListView();
    }

}
