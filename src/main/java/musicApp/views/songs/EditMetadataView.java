package musicApp.views.songs;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import musicApp.controllers.songs.EditMetadataController;
import musicApp.utils.LanguageManager;
import musicApp.views.View;

import java.util.*;

public class EditMetadataView extends View<EditMetadataView, EditMetadataController> {

    @FXML
    private StackPane artistStackPane;

    @FXML
    private TextField titleField, artistField, genreField, artistAutoCompletion;
    @FXML
    TextField tagInputField;
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
        initArtistAutoCompletion();
        initTranslations();
        initButtons();
        initTagInput();
    }

    public Scene getScene() {
        return scene;
    }
    
    private void initArtistAutoCompletion(){
        artistAutoCompletion.setEditable(false);
        artistAutoCompletion.setMouseTransparent(true);
        artistAutoCompletion.setFocusTraversable(false);

        artistField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.BACK_SPACE || event.getCode() == KeyCode.DELETE) {
                if (artistField.getText() == null || artistField.getText().isEmpty()){
                    artistAutoCompletion.setText("");
                }
            }
            else if ((event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.RIGHT)
                    && artistAutoCompletion.getLength() > 0) {
                event.consume();
                artistField.setText(artistField.getText() + artistAutoCompletion.getText().stripLeading());
                artistAutoCompletion.setText("");
                artistField.positionCaret(artistField.getText().length());
            }
        });

        artistField.setOnKeyReleased(event -> {
            String input = artistField.getText();

            Optional<String> randomArtist = viewController.getArtistAutoCompletion(input);
            if (randomArtist.isEmpty()) {
                artistAutoCompletion.setText("");
                return;
            }
            String completion = randomArtist.get().substring(input.length());
            artistField.positionCaret(artistField.getText().length());
            artistAutoCompletion.setText(" ".repeat(artistField.getText().length()) + completion);
        });
    }

    private void initTranslations() {
        titleLabel.setText(LanguageManager.getInstance().get("song.title"));
        artistLabel.setText(LanguageManager.getInstance().get("song.artist"));
        genreLabel.setText(LanguageManager.getInstance().get("song.genre"));
        chooseCoverButton.setText(LanguageManager.getInstance().get("button.choose_file"));
        saveButton.setText(LanguageManager.getInstance().get("button.save"));
        cancelButton.setText(LanguageManager.getInstance().get("button.cancel"));
        tagInputField.setPromptText(LanguageManager.getInstance().get("prompt.addTag"));
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
