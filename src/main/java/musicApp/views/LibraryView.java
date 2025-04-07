package musicApp.views;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
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
    private TextField autoCompletion;

    @FXML
    private StackPane stackPane;

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
        File fontFile = new File("src/main/resources/fonts/JetBrainsMonoNLNerdFont-Regular.ttf");
        if (fontFile.exists()) {
            double fontSize = 14.0;
            Font font = Font.loadFont(fontFile.toURI().toString(), fontSize);
            if (font != null) {
                songInput.setFont(font);
                autoCompletion.setFont(font);
            }
        }

        autoCompletion.setEditable(false);
        autoCompletion.setMouseTransparent(true);
        autoCompletion.setFocusTraversable(false);

        songInput.setOnKeyPressed(event -> {
            // Refresh the list view if the input in the search bar is empty
            if (event.getCode() == KeyCode.BACK_SPACE || event.getCode() == KeyCode.DELETE) {
                if (songInput.getText() == null || songInput.getText().isEmpty()){
                    autoCompletion.setText("");
                    updateListView();                
                }
            } 
            else if ((event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.RIGHT)
                    && autoCompletion.getLength() > 0) {
                event.consume();
                songInput.setText(songInput.getText() + autoCompletion.getText().stripLeading());
                autoCompletion.setText("");
                songInput.positionCaret(songInput.getText().length());
                listView.getItems().setAll(viewController.searchLibrary(songInput.getText()));
            }
        });
    
        songInput.setOnKeyReleased(event -> {    
            String input = songInput.getText();
            if (input == null || input.isEmpty()) {
                updateListView();
                autoCompletion.setText("");
                return;
            }        
            List<Song> results = viewController.searchLibrary(input);
            listView.getItems().setAll(results);
            List<String> suggestions = viewController.searchStartsWith(input);
            if (suggestions.isEmpty()) {
                autoCompletion.setText("");
                return;
            }

            List<Integer> indices = new ArrayList<>();
            for (int i = 0; i < suggestions.size(); i++) {
                indices.add(i);
            }
            Collections.shuffle(indices);

            for (int index : indices) {
                if (suggestions.get(index).length() > input.length()) {
                    String completion = suggestions.get(index).substring(input.length());
                    songInput.setText(input);
                    songInput.positionCaret(songInput.getText().length());
                    autoCompletion.setText(" ".repeat(songInput.getText().length()) + completion);
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
