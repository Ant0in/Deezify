package musicApp.views;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import musicApp.controllers.LibraryController;
import musicApp.controllers.songs.SongCellController;
import musicApp.models.Library;
import musicApp.services.LanguageService;
import musicApp.views.songs.SongCell;

/**
 * The MainLibrary view.
 */
@SuppressWarnings("unused")
public class LibraryView extends SongContainerView<LibraryView, LibraryController, Library> {

    @FXML
    private TextField songInput;

    @FXML
    private Button addSongButton;

    /**
     * Instantiates a new Main library view.
     */
    public LibraryView() {
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
            if (newVal == null || newVal.isEmpty()) {
                updateListView();
            } else {
                listView.getItems().setAll(viewController.searchLibrary(newVal));
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
    protected void initTranslation() {
        songInput.promptTextProperty().bind(Bindings.createStringBinding(
                () -> LanguageService.getInstance().get("search"),
                LanguageService.getInstance().getLanguageProperty()
        ));
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
