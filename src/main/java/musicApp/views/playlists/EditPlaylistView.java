package musicApp.views.playlists;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import musicApp.services.AlertService;
import musicApp.services.LanguageService;
import musicApp.views.View;

import java.io.File;
import java.nio.file.Path;

/**
 * The Playlist edit view.
 * This is a popup window that allows the user to create or edit a playlist.
 */
public class EditPlaylistView extends View {
    
    private EditPlaylistViewListener listener;
    private Stage stage;

    @FXML
    private Label nameLabel, coverLabel;
    @FXML
    private TextField nameField;
    @FXML
    private Button chooseCoverButton, actionButton, cancelButton;
    @FXML
    private ImageView coverImage;
    @FXML
    private VBox popupLayout;

    /**
     * Listener interface for handling events in the EditPlaylistView.
     * Implement this interface to manage user actions such as saving a playlist,
     * checking if the view is in creation mode, retrieving the stage, and closing the view.
     */
    public interface EditPlaylistViewListener {
        void handleSave(String playlistName, Path imagePath);

        void handleClose();

        boolean isCreation();
    }

    /**
     * Sets listener.
     *
     * @param newListener the listener
     */
    public void setListener(EditPlaylistViewListener newListener) {
        listener = newListener;
    }

    /**
     * Initializes the PlaylistEditView.
     * This method is called to set up the view when it is created.
     */
    @Override
    public void init() {
        initControls();
        refreshTranslation();
        initStage();
    }

    /**
     * Initializes the stage for the EditPlaylistView.
     */
    private void initStage() {
        stage = new Stage();
        if (listener.isCreation()) {
            stage.setTitle(LanguageService.getInstance().get("create_playlist.title"));
        } else {
            stage.setTitle(LanguageService.getInstance().get("edit_playlist.title"));
        }
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Refreshes the translation for the UI elements.
     */
    @Override
    protected void refreshTranslation() {
        nameLabel.setText(LanguageService.getInstance().get("create_playlist.name"));
        coverLabel.setText(LanguageService.getInstance().get("create_playlist.image_path"));
        chooseCoverButton.setText(LanguageService.getInstance().get("playlist.select_image"));
        String actionButtonText = listener.isCreation()
                ? LanguageService.getInstance().get("button.create")
                : LanguageService.getInstance().get("button.save");
        actionButton.setText(actionButtonText);
        cancelButton.setText(LanguageService.getInstance().get("button.cancel"));
    }

    /**
     * Handle closing the view.
     */
    public void close() {
        stage.close();
    }

    /**
     * Sets up the controls for the PlaylistEditView.
     * This method creates the text field, buttons, and labels used in the view.
     */
    private void initControls() {
        chooseCoverButton.setOnAction(_ -> handleChooseImage());

        actionButton.setOnAction(_ -> listener.handleSave(nameField.getText(),
                (Path) coverLabel.getUserData()));

        cancelButton.setOnAction(_ -> listener.handleClose());
    }

    /**
     * Handles the action when the user selects an image for the playlist.
     * This method opens a file chooser dialog to select an image file.
     */
    private void handleChooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(LanguageService.getInstance().get("playlist.select_image"));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            coverLabel.setText(selectedFile.getAbsolutePath());
            coverLabel.setUserData(selectedFile.toPath());
            try {
                coverImage.setImage(new javafx.scene.image.Image(selectedFile.toURI().toString()));
            } catch (Exception e) {
                AlertService as = new AlertService();
                as.showExceptionAlert(e);
            }
        }
    }
}
