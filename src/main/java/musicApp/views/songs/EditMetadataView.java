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
import musicApp.utils.LanguageManager;
import musicApp.views.View;

import java.util.*;
import java.util.function.Function;

public class EditMetadataView extends View<EditMetadataView, EditMetadataController> {

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

    private final Set<String> currentTags = new HashSet<>();

    @Override
    public void init() {
        initAutoCompletionFields();
        initTranslations();
        initButtons();
        initTagInput();
    }

    public Scene getScene() {
        return scene;
    }

    private void initAutoCompletionFields(){
        initArtistAutoCompletion();
        initTagAutoCompletion();
    }

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


    private void initArtistAutoCompletion() {
        initAutoCompletion(artistField, artistAutoCompletion, viewController::getArtistAutoCompletion);
    }

    private void initTagAutoCompletion() {
        initAutoCompletion(tagInputField, tagAutoCompletion, viewController::getTagAutoCompletion);
    }

    private void initTranslations() {
        titleLabel.setText(LanguageManager.getInstance().get("song.title"));
        artistLabel.setText(LanguageManager.getInstance().get("song.artist"));
        genreLabel.setText(LanguageManager.getInstance().get("song.genre"));
        chooseCoverButton.setText(LanguageManager.getInstance().get("button.choose_file"));
        saveButton.setText(LanguageManager.getInstance().get("button.save"));
        cancelButton.setText(LanguageManager.getInstance().get("button.cancel"));
        tagInputField.setPromptText(LanguageManager.getInstance().get("prompt.add_tag"));
    }

    private void initButtons() {
        chooseCoverButton.setOnAction(_ -> viewController.handleChooseCover());

        saveButton.setOnAction(_ -> viewController.handleSaveMetadata(
                titleField.getText(),
                artistField.getText(),
                genreField.getText(),
                currentTags
        ));

        cancelButton.setOnAction(_ -> viewController.handleCancel());
    }

    private void initTagInput() {
        tagInputField.setOnAction(_ -> handleAddTag());
    }

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

    public void populateFields(String title, String artist, String genre, ArrayList<String> userTags) {
        titleField.setText(title);
        artistField.setText(artist);
        genreField.setText(genre);
        setTags(new HashSet<>(userTags)); // Convert ArrayList to Set and populate tags
    }

    public void setCoverImage(javafx.scene.image.Image image) {
        coverImage.setImage(image);
    }

    public Set<String> getTags() {
        return currentTags;
    }

    public void setTags(Set<String> tags) {
        currentTags.clear();
        tagFlowPane.getChildren().clear();
        for (String tag : tags) {
            tagInputField.setText(tag);
            handleAddTag();
        }
    }
}
