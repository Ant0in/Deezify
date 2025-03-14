package MusicApp.Views;

import MusicApp.Controllers.MainLibraryController;
import MusicApp.Models.Song;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class MainLibraryView extends PlayListView<MainLibraryView, MainLibraryController> {

    @FXML
    private TextField songInput;

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
        songInput.textProperty().addListener((obs, oldVal, newVal) -> {
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
        songInput.setPromptText(MusicApp.utils.LanguageManager.get("search"));
    }


    /**
     * Initialize the playlist view.
     */
    private void initPlayListView() {
        listView.setCellFactory(lv -> new SongCell(viewController));
        updateListView();
    }

    /**
     * Setup the list selection listeners.
     */
    private void setupListSelectionListeners() {
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null){
                viewController.clearQueueSelection();
            }
        });
    }

}
