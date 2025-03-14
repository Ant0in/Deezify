package MusicApp.Views;

import MusicApp.Controllers.PlayListController;
import MusicApp.Models.Song;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class PlayListView extends View<PlayListView, PlayListController> {

    @FXML
    private TextField songInput;

    @FXML
    private ListView<Song> playListView;

    public PlayListView() {
    }

    @Override
    public void init() {
        initSongInput();
        initPlayListView();
        updatePlayListView();
        initTranslation();
        setupListSelectionListeners();
        enableDoubleClick();
    }

    public BooleanBinding isSelected() {
        return playListView.getSelectionModel().selectedItemProperty().isNotNull();
    }

    /**
     * Initialize the song input for the search
     */
    private void initSongInput() {
        songInput.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.isEmpty()) {
                playListView.getItems().setAll(viewController.searchLibrary(newVal));
            } else {
                updatePlayListView();
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
     * Update the play list view.
     */
    public void updatePlayListView() {
        playListView.getItems().setAll(viewController.getPlayerController().getLibrary().toList());
    }

    /**
     * Initialize the playlist view.
     */
    private void initPlayListView() {
        playListView.setCellFactory(lv -> new SongCell(viewController.getPlayerController()));
        updatePlayListView();
    }

    /**
     * Setup the list selection listeners.
     */
    private void setupListSelectionListeners() {
        playListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null){
                viewController.getPlayerController().clearQueueSelection();
            }
        });
    }

    public void clearSelection() {
        playListView.getSelectionModel().clearSelection();
    }

    public int getSelectedSongIndex() {
        return playListView.getSelectionModel().getSelectedIndex();
    }

    public void enableDoubleClick() {
        playListView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                viewController.getPlayerController().handlePlaySong();
            }
        });
    }

}
