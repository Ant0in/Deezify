package musicApp.views;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import musicApp.controllers.MainLibraryController;
import musicApp.controllers.SongCellController;
import musicApp.models.Library;
import musicApp.utils.LanguageManager;

/**
 * The MainLibrary view.
 */
@SuppressWarnings("unused")
public class MainLibraryView extends SongContainerView<MainLibraryView, MainLibraryController, Library> {

    @FXML
    private TextField songInput;

    @FXML
    private Button addSongButton;

    /**
     * Instantiates a new Main library view.
     */
    public MainLibraryView() {
    }

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
            if (newVal != null && !newVal.isEmpty()) {
                listView.getItems().setAll(viewController.searchLibrary(newVal));
            } else {
                updateListView();
            }
        });
    }

    /**
     * Initialize the buttons in the view.
     */
    private void initButtons() {
        addSongButton.setOnAction(event -> viewController.handleAddSong());
    }

    /**
     * Initialize the translations of the texts in the view.
     */
    @Override
    protected void initTranslation() {
        songInput.setPromptText(LanguageManager.getInstance().get("search"));
    }


    /**
     * Initialize the playlist view.
     */
    private void initPlayListView() {
        listView.setCellFactory(_ -> new SongCell(new SongCellController(viewController)));
        updateListView();
    }

    /**
     * Setup the list selection listeners.
     */
    private void setupListSelectionListeners() {
        listView.getSelectionModel().selectedItemProperty().addListener((_, _, newVal) -> {
            if (newVal != null) {
                viewController.clearQueueSelection();
            }
        });
    }

    @Override
    public void refreshUI() {
        updateListView();
    }

}
