package musicApp.views;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import musicApp.controllers.EditMetadataController;
import musicApp.utils.LanguageManager;

public class EditMetadataView extends View<EditMetadataView, EditMetadataController> {

    @FXML
    TextField titleField, artistField, genreField;
    @FXML
    Label titleLabel, artistLabel, genreLabel;
    @FXML
    Button chooseCoverButton, saveButton, cancelButton;
    @FXML
    ImageView coverImage;

    @Override
    public void init() {
        initTranslations();
        initButtons();
    }

    public Scene getScene() {
        return scene;
    }

    private void initTranslations() {
        titleLabel.setText(LanguageManager.getInstance().get("song.title"));
        artistLabel.setText(LanguageManager.getInstance().get("song.artist"));
        genreLabel.setText(LanguageManager.getInstance().get("song.genre"));
        chooseCoverButton.setText(LanguageManager.getInstance().get("button.choose_file"));
        saveButton.setText(LanguageManager.getInstance().get("settings.save"));
        cancelButton.setText(LanguageManager.getInstance().get("settings.cancel"));
    }

    private void initButtons() {
        chooseCoverButton.setOnAction(_ -> {
            viewController.handleChooseCover();
        });

        saveButton.setOnAction(_ -> {
            viewController.handleSaveMetadata(
                titleField.getText(),
                artistField.getText(),
                genreField.getText(),
                null
            );
        });

        cancelButton.setOnAction(_ -> {
            viewController.handleCancel();
        });
    }


}
