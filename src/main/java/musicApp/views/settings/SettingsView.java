package musicApp.views.settings;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.Modality;
import javafx.stage.Stage;
import musicApp.enums.Language;
import musicApp.services.FileDialogService;
import musicApp.services.LanguageService;
import musicApp.views.View;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The view for the settings window.
 */
public class SettingsView extends View {
    
    private SettingsViewListener listener;
    private Stage stage;

    @FXML
    private ComboBox<String> languageComboBox;    
    @FXML
    private Label languageTitle, languageSelect, musicFolderLabel;
    @FXML
    private Button saveButton, cancelButton, browseButton, editUserProfileButton;


    
    /**
     * The listener interface for receiving settings view events.
     * Implement this interface to handle user actions in the settings view.
     */
    public interface SettingsViewListener {

        void handleCancel();

        void handleSave(Language language, Path musicFolder);

        String getMusicFolderString();

        void editUserProfile();
    }

    /**
     * Sets listener.
     *
     * @param newListener the listener
     */
    public void setListener(SettingsViewListener newListener) {
        listener = newListener;
    }

    /**
     * Initialize the settings view.
     */
    @Override
    public void init() {
        initComboBox();
        initButtons();
        initMusicFolderLabel();
        initStage();
        refreshTranslation();
    }
    
    private void initStage() {
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setOnCloseRequest(_ -> listener.handleCancel());
    }

    public void show(){
        stage.show();
    }

    public void close(){
        stage.close();
    }

    /**
     * Initialize the directory label to display the current music directory.
     */
    private void initMusicFolderLabel() {
        musicFolderLabel.setText(listener.getMusicFolderString());
    }

    /**
     * Update the language combobox to display the current language.
     */
    private void updateLanguageComboBox() {
        Language currentLang = LanguageService.getInstance().getCurrentLanguage();
        languageComboBox.getSelectionModel().select(currentLang.getDisplayName());
    }

    /**
     * Initialize the language combobox to display the possible languages
     */
    private void initComboBox() {
        languageComboBox.getItems().clear();
        for (Language lang : Language.values()) {
            languageComboBox.getItems().add(lang.getDisplayName());
        }
        updateLanguageComboBox();
    }

    /**
     * Initialize the translations of the view.
     */
    @Override
    protected void refreshTranslation() {
        LanguageService languageService = LanguageService.getInstance();
        languageTitle.setText(languageService.get("settings.lang_title"));
        languageSelect.setText(languageService.get("settings.lang_select"));
        stage.setTitle(languageService.get("settings.title"));
        saveButton.setText(languageService.get("button.save"));
        cancelButton.setText(languageService.get("button.cancel"));
        browseButton.setText(languageService.get("settings.select_music_folder"));
        updateLanguageComboBox();
    }

    /**
     * Initialize the buttons of the view.
     */
    private void initButtons() {
        saveButton.setOnMouseClicked(_ -> handleSave());
        cancelButton.setOnMouseClicked(_ -> listener.handleCancel());
        browseButton.setOnMouseClicked(_ -> handleBrowseMusicFolder());
        editUserProfileButton.setOnMouseClicked(_ -> listener.editUserProfile());
    }

    /**
     * Handle when the browse button is pressed
     */
    private void handleBrowseMusicFolder() {
        LanguageService languageService = LanguageService.getInstance();
        File selectedDirectory = FileDialogService.chooseDirectory(null,
                languageService.get("settings.select_music_folder"));
        if (selectedDirectory != null) {
            this.musicFolderLabel.setText(selectedDirectory.getAbsolutePath());
        }
    }

    /**
     * Get the selected language from the combobox.
     *
     * @return The selected language.
     */
    private Language getSelectedLanguage() {
        String selectedDisplayName = languageComboBox.getSelectionModel().getSelectedItem();
        return Language.fromDisplayName(selectedDisplayName);
    }

    /**
     * Handle when the save button is pressed
     */
    private void handleSave() {
        Language selectedLanguage = getSelectedLanguage();
        Path musicDirectory = Paths.get(musicFolderLabel.getText());
        listener.handleSave(selectedLanguage, musicDirectory);
    }

    /**
     * Update the view with the current settings.
     */
    public void updateView(double balance, String musicFolderString) {
        initComboBox();
        musicFolderLabel.setText(musicFolderString);
    }
}
