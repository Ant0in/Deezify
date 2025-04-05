package musicApp.views;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Button;
import musicApp.controllers.LibraryController;
import musicApp.controllers.songs.SongCellController;
import musicApp.models.Library;
import musicApp.models.Song;
import musicApp.utils.LanguageManager;
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

    private boolean isCompleting = false;


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

        songInput.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.TAB && songInput.getSelection().getLength() > 0) {
                songInput.positionCaret(songInput.getText().length());
                event.consume();
            }
        });

        songInput.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.BACK_SPACE || event.getCode() == KeyCode.DELETE) {
                if (songInput.getText() == null || songInput.getText().isEmpty()){
                    updateListView();                
                    return;
                }
                if (isCompleting){                    
                    songInput.setText(songInput.getText().substring(0, songInput.getText().length() - 1));
                    songInput.positionCaret(songInput.getText().length());
                }
            } 
            if ((event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.RIGHT)
                    && songInput.getSelection().getLength() > 0) {
                songInput.positionCaret(songInput.getText().length());
                event.consume();
                listView.getItems().setAll(viewController.searchLibrary(songInput.getText()));
            }
        });
    
        songInput.setOnKeyReleased(event -> {    
            String input = songInput.getText();
            if (input == null || input.isEmpty()) {
                updateListView();
                return;
            }        
            List<Song> results = viewController.searchLibrary(input);
            listView.getItems().setAll(results);
            List<String> suggestions = new ArrayList<>(viewController.searchStartsWith(input));            
            Collections.shuffle(suggestions);
            for (String suggestion : suggestions) {
                if (suggestion.length() > input.length()) {
                    String completion = suggestion.substring(input.length());
                    isCompleting = true;
                    songInput.setText(input + completion);
                    songInput.selectRange(input.length(), suggestion.length());
                    break;
                }
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
                () -> LanguageManager.getInstance().get("search"),
                LanguageManager.getInstance().languageProperty()
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
