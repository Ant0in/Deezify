package musicApp.views.playlists;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import musicApp.controllers.playlists.PlaylistEditController;
import musicApp.models.Library;
import musicApp.utils.LanguageManager;
import musicApp.views.View;

import java.io.File;
import java.nio.file.Path;

/**
 * The Playlist edit view.
 * This is a popup window that allows the user to create or edit a playlist.
 */
public class PlaylistEditView extends View<PlaylistEditView, PlaylistEditController> {

    private final Stage stage = new Stage();
    private final boolean isCreation;
    private TextField nameField;
    private Label imagePathLabel;
    private Button selectImageButton;
    private Button actionButton;
    private Button cancelButton;
    private Scene scene;

    public PlaylistEditView(boolean isCreation) {
        this.isCreation = isCreation;
    }

    @Override
    public void init() {
        setupStage();
        setupControls();
        createLayout();
    }

    private void setupStage() {
        String titleKey = isCreation ? "create_playlist.title" : "edit_playlist.title";
        stage.setTitle(LanguageManager.getInstance().get(titleKey));
    }

    private void setupControls() {
        nameField = new TextField();
        imagePathLabel = new Label(LanguageManager.getInstance().get("playlist.no_image_selected"));

        selectImageButton = new Button(LanguageManager.getInstance().get("playlist.select_image"));
        selectImageButton.setOnAction(_ -> handleChooseImage());

        String actionButtonText = isCreation
                ? LanguageManager.getInstance().get("button.create")
                : LanguageManager.getInstance().get("button.save");

        actionButton = new Button(actionButtonText);
        actionButton.setOnAction(e -> viewController.handleSave(nameField.getText(),
                (Path) imagePathLabel.getUserData()));

        cancelButton = new Button(LanguageManager.getInstance().get("button.cancel"));
        cancelButton.setOnAction(e -> close());
    }

    private void createLayout() {
        VBox popupLayout = new VBox(10,
                new Label(LanguageManager.getInstance().get("create_playlist.name")), nameField,
                new Label(LanguageManager.getInstance().get("playlist.image")),
                selectImageButton, imagePathLabel,
                actionButton, cancelButton);

        popupLayout.setStyle("-fx-padding: 10;");
        scene = new Scene(popupLayout, 400, 300);

        stage.setScene(scene);
    }

    public void show() {
        stage.show();
    }

    public void close() {
        stage.close();
    }

    private void handleChooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(LanguageManager.getInstance().get("playlist.select_image"));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            imagePathLabel.setText(selectedFile.getAbsolutePath());
            imagePathLabel.setUserData(selectedFile.toPath());
        }
    }

    public void populateFields(Library playlist) {
        if (playlist != null) {
            nameField.setText(playlist.getName());

            try {
                Path imagePath = playlist.getImagePath();
                if (imagePath != null) {
                    imagePathLabel.setText(imagePath.toString());
                    imagePathLabel.setUserData(imagePath);
                }
            } catch (Exception e) {
                System.err.println("Error getting image path: " + e.getMessage());
            }
        }
    }
}
