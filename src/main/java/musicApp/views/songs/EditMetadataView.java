package musicApp.views.songs;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import musicApp.utils.LanguageManager;
import musicApp.views.View;

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

    @FXML
    StackPane artistStackPane, tagStackPane;
    @FXML
    TextField titleField, artistField, genreField, artistAutoCompletion;
    @FXML
    TextField tagInputField, tagAutoCompletion;
    @FXML
    Label titleLabel, artistLabel, genreLabel;
    @FXML
    Button chooseCoverButton, saveButton, cancelButton;
    @FXML
    ImageView coverImage;
    @FXML
    FlowPane tagFlowPane;

    private final Set<String> currentTags;

    /**
     * Instantiates a new EditMetadataView.
     */
    public EditMetadataView() {
        currentTags = new HashSet<>();
    }

    /**
     * Listener interface for handling events in the EditMetadataView.
     * Implement this interface to provide auto-completion suggestions and actions for cover selection,
     * saving metadata, and canceling the edit operation.
     */
    public interface EditMetadataViewListener {
        Optional<String> getArtistAutoCompletion(String input);

        Optional<String> getTagAutoCompletion(String input);

        void handleChooseCover();

        void handleSaveMetadata(String title, String artist, String genre, Set<String> userTags);

        void handleCancel();
    }

    /**
     * Sets listener.
     *
     * @param _listener the listener
     */
    public void setListener(EditMetadataViewListener _listener) {
        listener = _listener;
    }

    /**
     * Initializes the EditMetadataView.
     * This method initializes the auto-completion fields, translations, button actions, and tag input.
     */
    @Override
    public void init() {
        initAutoCompletionFields();
        initTranslations();
        initButtons();
        initTagInput();
    }

    /**
     * Gets the scene associated with this view.
     *
     * @return the scene
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Initializes the auto-completion fields for artist and tag input.
     */
    private void initAutoCompletionFields() {
        initArtistAutoCompletion();
        initTagAutoCompletion();
    }

    /**
     * Initializes the auto-completion functionality for a given text field.
     *
     * @param input                  The text field to which auto-completion is applied.
     * @param autoCompletion         The text field that displays the suggested completion.
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

        input.setOnKeyReleased(event -> {
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
     * Initializes the auto-completion for the tag input field.
     */
    private void initTagAutoCompletion() {
        initAutoCompletion(tagInputField, tagAutoCompletion, listener::getTagAutoCompletion);
    }

    /**
     * Initializes the translations for the labels and buttons in the view.
     */
    private void initTranslations() {
        titleLabel.setText(LanguageManager.getInstance().get("song.title"));
        artistLabel.setText(LanguageManager.getInstance().get("song.artist"));
        genreLabel.setText(LanguageManager.getInstance().get("song.genre"));
        chooseCoverButton.setText(LanguageManager.getInstance().get("button.choose_file"));
        saveButton.setText(LanguageManager.getInstance().get("button.save"));
        cancelButton.setText(LanguageManager.getInstance().get("button.cancel"));
        tagInputField.setPromptText(LanguageManager.getInstance().get("prompt.add_tag"));
    }

    /**
     * Initializes the buttons in the view.
     */
    private void initButtons() {
        chooseCoverButton.setOnAction(_ -> listener.handleChooseCover());

        saveButton.setOnAction(_ -> listener.handleSaveMetadata(
                titleField.getText(),
                artistField.getText(),
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
            tagButton.setOnAction(e -> {
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
    public void populateFields(String title, String artist, String genre, ArrayList<String> userTags) {
        titleField.setText(title);
        artistField.setText(artist);
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
}
