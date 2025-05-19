package musicApp.views;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import musicApp.enums.Language;
import musicApp.services.AlertService;
import musicApp.services.FileDialogService;
import musicApp.services.LanguageService;

import java.io.File;
import java.nio.file.Path;
import java.util.Objects;

/**
 * The Playlist edit view.
 * This is a popup window that allows the user to create or edit a playlist.
 */
public class EditUserProfileView extends View {

    private EditUserProfileViewListener listener;
    private Stage stage;

    @FXML
    private Label nameLabel, userImageLabel, musicFolderLabel, chosenMusicFolderLabel, balanceLabel, balanceTitle, left, right, crossfadeTitle, crossfadeLabel;
    @FXML
    private TextField nameField;
    @FXML
    private Button chooseUserImageButton, chooseMusicFolderButton, actionButton, cancelButton, equalizerButton;
    @FXML
    private ImageView userImage;
    @FXML
    private VBox popupLayout;
    @FXML
    private Slider balanceSlider, crossfadeSlider;
    @FXML
    private ComboBox<String> languageChoice;

    public void show() {
        if (stage != null) {
            stage.show();
        } else {
            initStage();
        }
    }

    /**
     * Sets listener.
     *
     * @param newListener the listener
     */
    public void setListener(EditUserProfileViewListener newListener) {
        listener = newListener;
    }

    /**
     * Initializes the PlaylistEditView.
     * This method is called to set up the view when it is created.
     */
    @Override
    public void init() {
        initButtons();
        initSlider();
        initLanguageChoice();
        refreshTranslation();
        initStage();
    }

    /**
     * Initialize the stage for the EditUserProfileView.
     */
    private void initStage() {
        stage = new Stage();
        if (listener.isCreation()) {
            stage.setTitle(LanguageService.getInstance().get("create_user.title"));
        } else {
            stage.setTitle(LanguageService.getInstance().get("edit_user.title"));
        }
        stage.setMinWidth(600);
        stage.setMinHeight(700);
        stage.setScene(scene);
    }

    /**
     * Initialize the balance slider.
     */
    private void initSlider() {
        balanceSlider.setValue(listener.getBalance());
        balanceLabel.setText(String.format("%.2f", listener.getBalance()));
        balanceSlider.valueProperty().addListener((_, _, newVal)
                -> balanceLabel.setText(String.format("%.2f", newVal.doubleValue())));

        crossfadeSlider.setValue(listener.getCrossfadeDuration());
        crossfadeLabel.setText(getCrossfadeLabelString(listener.getCrossfadeDuration()));
        crossfadeSlider.valueProperty().addListener((_, _, newVal)
                -> crossfadeLabel.setText(getCrossfadeLabelString(newVal.doubleValue())));
        crossfadeSlider.setMajorTickUnit(1);
        crossfadeSlider.setMinorTickCount(1);
        crossfadeSlider.setSnapToTicks(true);
        crossfadeSlider.setShowTickLabels(true);
        crossfadeSlider.setShowTickMarks(true);
    }

    /**
     * Populate the fields with the provided user name, image path, and music path.
     *
     * @param userName  The user name to set in the text field.
     * @param imagePath The path to the user image.
     * @param musicPath The path to the music folder.
     */
    public void populateFields(String userName, String imagePath, String musicPath) {
        nameField.setText(userName);
        userImage.setImage(new Image((imagePath == null || imagePath.isBlank()) ?
                Objects.requireNonNull(getClass().getResource("/images/default_account.png")).toExternalForm() :
                Path.of(imagePath).toUri().toString()));
        userImageLabel.setText(imagePath);
        chosenMusicFolderLabel.setText(musicPath);
    }

    private void initLanguageChoice() {
        languageChoice.setItems(FXCollections.observableArrayList(
                "English",
                "Français",
                "Nederlands"
        ));
        try {
            languageChoice.setValue(listener.getLanguage().getDisplayName());
        } catch (Exception e) {
            languageChoice.setValue(Language.DEFAULT.getDisplayName());
        }
    }

    /**
     * Refresh the translation for the UI elements.
     * This method updates the text of various labels and buttons based on the current language settings.
     */
    @Override
    protected void refreshTranslation() {
        chooseUserImageButton.setText(LanguageService.getInstance().get("user.select_picture"));
        chooseMusicFolderButton.setText(LanguageService.getInstance().get("user.select_music_folder"));
        String actionButtonText = listener.isCreation()
                ? LanguageService.getInstance().get("button.create")
                : LanguageService.getInstance().get("button.save");
        actionButton.setText(actionButtonText);
        cancelButton.setText(LanguageService.getInstance().get("button.cancel"));
        equalizerButton.setText(LanguageService.getInstance().get("settings.manage_audio_equalizer"));
    }

    /**
     * Close the stage.
     * This method is called to close the EditUserProfileView when the user is done editing.
     */
    public void close() {
        stage.close();
    }

    /**
     * Sets up the controls for the PlaylistEditView.
     * This method creates the text field, buttons, and labels used in the view.
     */
    private void initButtons() {
        chooseUserImageButton.setOnAction(_ -> handleChooseImage());
        chooseMusicFolderButton.setOnAction(_ -> handleBrowseMusicFolder());

        actionButton.setOnAction(_ -> listener.handleSave(nameField.getText(), balanceSlider.getValue(), crossfadeSlider.getValue(),
                !(userImageLabel.getText() == null) ? Path.of(userImageLabel.getText()) : null
                , !(chosenMusicFolderLabel.getText() == null) ? Path.of(chosenMusicFolderLabel.getText()) : null, languageChoice.getValue()));

        cancelButton.setOnAction(_ -> listener.handleCancel());
        equalizerButton.setOnAction(_ -> listener.handleOpenEqualizer());
    }

    /**
     * Handle when the browse button is pressed
     */
    private void handleBrowseMusicFolder() {
        File selectedDirectory = FileDialogService.chooseDirectory(null,
                LanguageService.getInstance().get("settings.select_music_folder"));
        if (selectedDirectory != null) {
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

    /**
     * Get the crossfade label string.
     * This method formats the crossfade duration to a string with one decimal place and appends the unit "seconds".
     *
     * @param crossfadeDuration The crossfade duration in seconds.
     * @return The formatted crossfade label string.
     */
    public String getCrossfadeLabelString(double crossfadeDuration) {
        String seconds = LanguageService.getInstance().get("settings.seconds");
        return String.format("%.1f %s", crossfadeDuration, seconds);

    }

    /**
     * Listener interface for handling events in the EditPlaylistView.
     * Implement this interface to manage user actions such as saving a playlist,
     * checking if the view is in creation mode, retrieving the stage, and closing the view.
     */
    public interface EditUserProfileViewListener {
        void handleSave(String userName, double balance, double crossfade, Path imagePath, Path musicPath, String Language);

        void handleCancel();

        boolean isCreation();

        double getBalance();

        double getCrossfadeDuration();

        Language getLanguage();

        void handleOpenEqualizer();
    }
}
