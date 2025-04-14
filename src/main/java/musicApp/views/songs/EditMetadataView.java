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
import musicApp.controllers.songs.EditMetadataController;
import musicApp.services.LanguageService;
import musicApp.views.View;

import java.util.*;
import java.util.function.Function;

/**
 * The EditMetadataView class is responsible for displaying the metadata editing interface for a song.
 * It allows users to edit the title, artist, genre, and tags of a song, as well as choose a cover image.
 */
public class EditMetadataView extends View<EditMetadataView, EditMetadataController> {

    @FXML
    StackPane artistStackPane, tagStackPane;
    @FXML
    TextField titleField, artistField, albumField, genreField, artistAutoCompletion, albumAutoCompletion;
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

    public EditMetadataView() {
        super();
        currentTags = new HashSet<>();
    }

    @Override
    public void init() {
        initAutoCompletionFields();
        refreshTranslation();
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
        initAutoCompletion(artistField, artistAutoCompletion, viewController::getArtistAutoCompletion);
    }

    /**
     * Initializes the auto-completion for the album field.
     */
    private void initAlbumAutoCompletion() {
        initAutoCompletion(albumField, albumAutoCompletion, viewController::getAlbumAutoCompletion);
    }

    /**
     * Initializes the auto-completion for the tag input field.
     */
    private void initTagAutoCompletion() {
        initAutoCompletion(tagInputField, tagAutoCompletion, viewController::getTagAutoCompletion);
    }

    /**
     * Initializes the translations for the labels and buttons in the view.
     */
    @Override
    protected void refreshTranslation() {
        titleLabel.setText(LanguageService.getInstance().get("song.title"));
        artistLabel.setText(LanguageService.getInstance().get("song.artist"));
        genreLabel.setText(LanguageService.getInstance().get("song.genre"));
        chooseCoverButton.setText(LanguageService.getInstance().get("button.choose_file"));
        saveButton.setText(LanguageService.getInstance().get("button.save"));
        cancelButton.setText(LanguageService.getInstance().get("button.cancel"));
        tagInputField.setPromptText(LanguageService.getInstance().get("prompt.add_tag"));
    }

    /**
     * Initializes the buttons in the view.
     */
    private void initButtons() {
        chooseCoverButton.setOnAction(_ -> viewController.handleChooseCover());

        saveButton.setOnAction(_ -> viewController.handleSaveMetadata(
                titleField.getText(),
                artistField.getText(),
                albumField.getText(),
                genreField.getText(),
                currentTags
        ));

        cancelButton.setOnAction(_ -> viewController.handleCancel());
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
     * Gets the tags associated with the song.
     *
     * @return A set of tags.
     */
    public Set<String> getTags() {
        return currentTags;
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
