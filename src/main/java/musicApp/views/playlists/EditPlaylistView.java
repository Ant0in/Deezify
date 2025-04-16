package musicApp.views.playlists;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import musicApp.models.Library;
import musicApp.utils.AlertService;
import musicApp.utils.LanguageManager;
import musicApp.views.View;

import java.io.File;
import java.nio.file.Path;

/**
 * The Playlist edit view.
 * This is a popup window that allows the user to create or edit a playlist.
 */
public class EditPlaylistView extends View {

    private EditPlaylistViewListener listener ;

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
        void handleSave(String name, Path imagePath);
        void close();
        boolean isCreation();
        Stage getStage();
    }

    /**
     * Sets listener.
     *
     * @param _listener the listener
     */
    public void setListener(EditPlaylistViewListener _listener) {
        listener = _listener;
    }

    /**
     * Initializes the PlaylistEditView.
     * This method is called to set up the view when it is created.
     */
    @Override
    public void init() {
        initControls();
        initTranslations();
    }

    /**
     * Initializes the translations of the view.
     * Sets the text for the labels and buttons based on the current language.
     */
    private void initTranslations() {
        nameLabel.setText(LanguageManager.getInstance().get("create_playlist.name"));
        coverLabel.setText(LanguageManager.getInstance().get("create_playlist.image_path"));
        chooseCoverButton.setText(LanguageManager.getInstance().get("playlist.select_image"));
        String actionButtonText = listener.isCreation()
                ? LanguageManager.getInstance().get("button.create")
                : LanguageManager.getInstance().get("button.save");
        actionButton.setText(actionButtonText);
        cancelButton.setText(LanguageManager.getInstance().get("button.cancel"));
    }

    /**
     * Sets up the controls for the PlaylistEditView.
     * This method creates the text field, buttons, and labels used in the view.
     */
    private void initControls() {
        chooseCoverButton.setOnAction(_ -> handleChooseImage());

        actionButton.setOnAction(e -> listener.handleSave(nameField.getText(),
                (Path) coverLabel.getUserData()));

        cancelButton.setOnAction(e -> listener.close());
    }

    /**
     * Handles the action when the user selects an image for the playlist.
     * This method opens a file chooser dialog to select an image file.
     */
    private void handleChooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(LanguageManager.getInstance().get("playlist.select_image"));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(listener.getStage());
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

    /**
     * Populates the fields of the PlaylistEditView with the given playlist data.
     * This method is used to pre-fill the fields when editing an existing playlist.
     *
     * @param playlist The playlist to populate the fields with.
     */
    public void populateFields(Library playlist) {
        if (playlist != null) {
            nameField.setText(playlist.getName());

            try {
                Path imagePath = playlist.getImagePath();
                if (imagePath != null) {
                    coverLabel.setText(imagePath.toString());
                    coverLabel.setUserData(imagePath);
                }
            } catch (Exception e) {
                System.err.println("Error getting image path: " + e.getMessage());
            }
        }
    }

    /**
     * Returns the scene associated with this view.
     *
     * @return The Scene object.
     */
    public Scene getScene() {
        return scene;
    }
}
