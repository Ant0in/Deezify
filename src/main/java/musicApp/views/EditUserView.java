package musicApp.views;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import musicApp.services.AlertService;
import musicApp.services.FileDialogService;
import musicApp.services.LanguageService;

import java.io.File;
import java.nio.file.Path;

/**
 * The Playlist edit view.
 * This is a popup window that allows the user to create or edit a playlist.
 */
public class EditUserView extends View {
    
    private EditUserViewListener listener;
    private Stage stage;

    @FXML
    private Label nameLabel, userImageLabel, musicFolderLabel, chosenMusicFolderLabel;
    @FXML
    private TextField nameField;
    @FXML
    private Button chooseUserImageButton,chooseMusicFolderButton, actionButton, cancelButton;
    @FXML
    private ImageView userImage;
    @FXML
    private VBox popupLayout;

    public void show() {
        if (stage != null) {
            stage.show();
        } else {
            initStage();
        }
    }

    /**
     * Listener interface for handling events in the EditPlaylistView.
     * Implement this interface to manage user actions such as saving a playlist,
     * checking if the view is in creation mode, retrieving the stage, and closing the view.
     */
    public interface EditUserViewListener {
        void handleSave(String userName, Path imagePath, Path musicPath);

        void handleClose();

        boolean isCreation();
    }

    /**
     * Sets listener.
     *
     * @param newListener the listener
     */
    public void setListener(EditUserViewListener newListener) {
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

    private void initStage() {
        stage = new Stage();
        if (listener.isCreation()) {
            stage.setTitle(LanguageService.getInstance().get("create_playlist.title"));
        } else {
            stage.setTitle(LanguageService.getInstance().get("edit_playlist.title"));
        }
        stage.setScene(scene);
    }

    public void populateFields(String userName, String imagePath, String musicPath) {
        nameField.setText(userName);
        userImage.setImage(new Image(imagePath));
        chosenMusicFolderLabel.setText(musicPath);
    }

    @Override
    protected void refreshTranslation() {
        userImageLabel.setText(LanguageService.getInstance().get("create_playlist.image_path"));
        musicFolderLabel.setText(LanguageService.getInstance().get("create_playlist.music_folder"));
        chooseUserImageButton.setText(LanguageService.getInstance().get("playlist.select_image"));
//        chooseMusicFolderButton.setText(LanguageService.getInstance().get(""));
        chooseMusicFolderButton.setText("Select folder");
        String actionButtonText = listener.isCreation()
                ? LanguageService.getInstance().get("button.create")
                : LanguageService.getInstance().get("button.save");
        actionButton.setText(actionButtonText);
        cancelButton.setText(LanguageService.getInstance().get("button.cancel"));
    }

    public void close() {
        stage.close();
    }

    /**
     * Sets up the controls for the PlaylistEditView.
     * This method creates the text field, buttons, and labels used in the view.
     */
    private void initControls() {
        chooseUserImageButton.setOnAction(_ -> handleChooseImage());
        chooseMusicFolderButton.setOnAction(_ -> handleBrowseMusicFolder());

        actionButton.setOnAction(_ -> listener.handleSave(nameField.getText(),
                (Path) userImageLabel.getUserData(), Path.of(chosenMusicFolderLabel.getText())));

        cancelButton.setOnAction(_ -> listener.handleClose());
    }

    /**
     * Handle when the browse button is pressed
     */
    private void handleBrowseMusicFolder() {
        File selectedDirectory = FileDialogService.chooseDirectory(null,
                LanguageService.getInstance().get("settings.select_music_folder"));
        if (selectedDirectory != null) {
            musicFolderLabel.setText(selectedDirectory.getAbsolutePath());
            chosenMusicFolderLabel.setText(selectedDirectory.getAbsolutePath());
            chosenMusicFolderLabel.setUserData(selectedDirectory.toPath());
        }
    }

    /**
     * Handles the action when the user selects an image for the playlist.
     * This method opens a file chooser dialog to select an image file.
     */
    private void handleChooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(LanguageService.getInstance().get(""));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            userImageLabel.setText(selectedFile.getAbsolutePath());
            userImageLabel.setUserData(selectedFile.toPath());
            try {
                userImage.setImage(new javafx.scene.image.Image(selectedFile.toURI().toString()));
            } catch (Exception e) {
                AlertService as = new AlertService();
                as.showExceptionAlert(e);
            }
        }
    }
}
