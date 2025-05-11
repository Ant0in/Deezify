package musicApp.views.songs;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import musicApp.services.LanguageService;
import musicApp.views.View;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * The EditMetadataView class is responsible for displaying the metadata editing interface for a song.
 * It allows users to edit the title, artist, genre, and tags of a song, as well as choose a cover image.
 */
public class EditMetadataView extends View {

    private EditMetadataViewListener listener;
    private Stage stage;
    
    @FXML
    private TextField titleField, artistField, albumField, genreField, tagInputField,
        artistAutoCompletion, albumAutoCompletion, tagAutoCompletion;
    @FXML
    private Label titleLabel, artistLabel, genreLabel;
    @FXML
    private Button chooseCoverButton, saveButton, cancelButton;
    @FXML
    private ImageView coverImage;
    @FXML
    private FlowPane tagFlowPane;

    private final Set<String> currentTags;

    public EditMetadataView() {
        super();
        currentTags = new HashSet<>();
    }

    /**
     * Listener interface for handling events in the EditMetadataView.
     * Implement this interface to provide auto-completion suggestions and actions for cover selection,
     * saving metadata, and canceling the edit operation.
     */
    public interface EditMetadataViewListener {
        void handleCancel();

        void handleCoverChanged(File coverImageFile);

        void handleSaveMetadata(String title, String artist, String album, String genre, Set<String> userTags);

        Optional<String> getArtistAutoCompletion(String input);

        Optional<String> getTagAutoCompletion(String input);

        Optional<String> getAlbumAutoCompletion(String input);
    }

    /**
     * Sets listener.
     *
     * @param newListener the listener
     */
    public void setListener(EditMetadataViewListener newListener) {
        listener = newListener;
    }

    @Override
    public void init() {
        initAutoCompletionFields();
        refreshTranslation();
        initButtons();
        initTagInput();
        initStage();
    }

    private void initStage(){
        stage = new Stage();
        stage.setTitle(LanguageService.getInstance().get("button.edit_metadata"));
        stage.setScene(scene);
        stage.show();
    }

    public void close(){
        stage.close();
    }

    /**
     * Initializes the auto-completion fields for artist and tag input.
     */
    private void initAutoCompletionFields(){
        initArtistAutoCompletion();
        initAlbumAutoCompletion();
        initTagAutoCompletion();
    }

    /**
     * Initializes the auto-completion functionality for a given text field.
     *
     * @param input              The text field to which auto-completion is applied.
     * @param autoCompletion     The text field that displays the suggested completion.
     * @param getSuggestedCompletion A function that provides the suggested completion based on the current input.
     */
    private void initAutoCompletion(TextField input, TextField autoCompletion, Function<String, Optional<String>> getSuggestedCompletion) {
        autoCompletion.setEditable(false);
        autoCompletion.setMouseTransparent(true);
        autoCompletion.setFocusTraversable(false);

        input.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.BACK_SPACE || event.getCode() == KeyCode.DELETE) {
                if (input.getText() == null || input.getText().isEmpty()) {
                    autoCompletion.setText("");
                }
            } else if ((event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.RIGHT)
                    && autoCompletion.getLength() > 0) {
                event.consume();
                input.setText(input.getText() + autoCompletion.getText().stripLeading());
                autoCompletion.setText("");
                input.positionCaret(input.getText().length());
            }
        });

        input.setOnKeyReleased(_ -> {
            String currentText = input.getText();
            Optional<String> suggestion = getSuggestedCompletion.apply(currentText);

            if (suggestion.isEmpty()) {
                autoCompletion.setText("");
                return;
            }

            String completion = suggestion.get().substring(currentText.length());
            input.positionCaret(input.getText().length());
            autoCompletion.setText(" ".repeat(currentText.length()) + completion);
        });
    }

    /**
     * Initializes the auto-completion for the artist field.
     */
    private void initArtistAutoCompletion() {
        initAutoCompletion(artistField, artistAutoCompletion, listener::getArtistAutoCompletion);
    }

    /**
     * Initializes the auto-completion for the album field.
     */
    private void initAlbumAutoCompletion() {
        initAutoCompletion(albumField, albumAutoCompletion, listener::getAlbumAutoCompletion);
    }

    /**
     * Initializes the auto-completion for the tag input field.
     */
    private void initTagAutoCompletion() {
        initAutoCompletion(tagInputField, tagAutoCompletion, listener::getTagAutoCompletion);
    }

    /**
     * Initializes the translations for the labels and buttons in the view.
     */
    @Override
    protected void refreshTranslation() {
        LanguageService languageService = LanguageService.getInstance();
        titleLabel.setText(languageService.get("song.title"));
        artistLabel.setText(languageService.get("song.artist"));
        genreLabel.setText(languageService.get("song.genre"));
        chooseCoverButton.setText(languageService.get("button.choose_file"));
        saveButton.setText(languageService.get("button.save"));
        cancelButton.setText(languageService.get("button.cancel"));
        tagInputField.setPromptText(languageService.get("prompt.add_tag"));
    }

    /**
     * Initializes the buttons in the view.
     */
    private void initButtons() {
        chooseCoverButton.setOnAction(_ -> handleChooseCover());

        saveButton.setOnAction(_ -> listener.handleSaveMetadata(
                titleField.getText(),
                artistField.getText(),
                albumField.getText(),
                genreField.getText(),
                currentTags
        ));

        cancelButton.setOnAction(_ -> listener.handleCancel());
    }

    /**
     * Initializes the tag input field to allow adding tags.
     */
    private void initTagInput() {
        tagInputField.setOnAction(_ -> handleAddTag());
    }

    /**
     * Handles the addition of a tag when the user presses Enter in the tag input field.
     */
    private void handleAddTag() {
        String tag = tagInputField.getText().trim();
        if (!tag.isEmpty() && currentTags.add(tag)) {
            Button tagButton = new Button(tag);
            tagButton.setStyle("-fx-background-color: #274472; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5;");
            tagButton.setOnAction(_ -> {
                tagFlowPane.getChildren().remove(tagButton);
                currentTags.remove(tag);
            });
            tagFlowPane.getChildren().add(tagButton);
            tagInputField.clear();
        }
    }

    /**
     * Populates the fields with the provided metadata.
     *
     * @param title    The title of the song.
     * @param artist   The artist of the song.
     * @param genre    The genre of the song.
     * @param userTags The user tags associated with the song.
     */
    public void populateFields(String title, String artist, String album, String genre, ArrayList<String> userTags) {
        titleField.setText(title);
        artistField.setText(artist);
        albumField.setText(album);
        genreField.setText(genre);
        setTags(new HashSet<>(userTags)); // Convert ArrayList to Set and populate tags
    }

    /**
     * Sets the cover image for the song.
     *
     * @param image The image to be set as the cover.
     */
    public void setCoverImage(Image image) {
        coverImage.setImage(image);
    }

    /**
     * Sets the tags for the song.
     *
     * @param tags A set of tags to be set.
     */
    public void setTags(Set<String> tags) {
        currentTags.clear();
        tagFlowPane.getChildren().clear();
        for (String tag : tags) {
            tagInputField.setText(tag);
            handleAddTag();
        }
    }

    /**
     * Prompts the user with a FileChooser dialog to select an image file.
     *
     * @return the selected File if the user chose one, or {@code null} otherwise
     */
    private File promptUserForCoverFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Cover Image/Video");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image or Video File", "*.png", "*.jpg", "*.jpeg", "*.mp4")
        );

        return fileChooser.showOpenDialog(stage);
    }

    /**
     * Handles the user action of choosing a cover image.
     * Opens a file chooser, and if a valid image is selected, loads and displays it in the view.
     */
    private void handleChooseCover() {
        File file = promptUserForCoverFile();
        listener.handleCoverChanged(file);
    }
}
