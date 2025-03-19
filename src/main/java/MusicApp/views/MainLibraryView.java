package MusicApp.views;

import MusicApp.controllers.MainLibraryController;
import MusicApp.controllers.SongController;
import MusicApp.utils.LanguageManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * The MainLibrary view.
 */
@SuppressWarnings("unused")
public class MainLibraryView extends PlayListView<MainLibraryView, MainLibraryController> {

    @FXML
    private TextField songInput;

    /**
     * Instantiates a new Main library view.
     */
    public MainLibraryView() {
    }

    @Override
    public void init() {
        initSongInput();
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
            if (newVal != null && !newVal.isEmpty()) {
                listView.getItems().setAll(viewController.searchLibrary(newVal));
            } else {
                updateListView();
            }
        });
    }

    /**
     * Initialize the translations of the texts in the view.
     */
    public void initTranslation() {
        songInput.setPromptText(LanguageManager.getInstance().get("search"));
    }


    /**
     * Initialize the playlist view.
     */
    private void initPlayListView() {
        listView.setCellFactory(_ -> new SongCell(new SongController(viewController)));
        updateListView();
    }

    /**
     * Setup the list selection listeners.
     */
    private void setupListSelectionListeners() {
        listView.getSelectionModel().selectedItemProperty().addListener((_, _, newVal) -> {
            if (newVal != null){
                viewController.clearQueueSelection();
            }
        });
    }

}
