package musicApp.views.settings;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import musicApp.controllers.settings.SettingsController;
import musicApp.enums.Language;
import musicApp.models.Settings;
import musicApp.utils.FileDialogHelper;
import musicApp.utils.LanguageManager;
import musicApp.views.View;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The view for the settings window.
 */
public class SettingsView extends View {

    private SettingsView.SettingsViewListener listener;

    @FXML
    private ComboBox<String> languageComboBox;
    @FXML
    private Slider balanceSlider;
    @FXML
    private Label balanceLabel, balanceTitle, languageTitle, languageSelect, left, right;
    @FXML
    private Button saveButton, cancelButton;
    @FXML
    private Button browseButton, equalizerButton;
    @FXML
    private Label directoryLabel;

    private String title;

    /**
     * Instantiates a new Settings view.
     */
    public SettingsView() {
    }

    /**
     * The listener interface for receiving settings view events.
     * Implement this interface to handle user actions in the settings view.
     */
    public interface SettingsViewListener {
        void handleSave(Language language, double balance, Path musicDirectory);
        Path getMusicDirectory();
        double getBalance();
        void openEqualizer();
        void handleCancel();
    }

    /**
     * Sets listener.
     *
     * @param _listener the listener
     */
    public void setListener(SettingsView.SettingsViewListener _listener) {
        listener = _listener;
    }


    /**
     * Show the settings view.
     *
     * @param stage The stage to show the view on.
     */
    public void show(Stage stage) {
        stage.setScene(scene);
        stage.setTitle(getTitle());
        stage.show();
    }

    /**
     * Initialize the settings view.
     */
    @Override
    public void init() {
        initComboBox();
        initSlider();
        initTranslations();
        initButtons();
        initDirectoryLabel();
    }

    /**
     * Initialize the directory label to display the current music directory.
     */
    private void initDirectoryLabel() {
        directoryLabel.setText(listener.getMusicDirectory().toString());
    }

    /**
     * Update the language combobox to display the current language.
     */
    private void updateLanguageComboBox() {
        Language currentLang = LanguageManager.getInstance().getCurrentLanguage();
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
     * Initialize the balance slider.
     */
    private void initSlider() {
        balanceSlider.setValue(listener.getBalance());
        balanceLabel.setText(String.format("%.2f",listener.getBalance()));
        balanceSlider.valueProperty().addListener((_, _, newVal)
                -> balanceLabel.setText(String.format("%.2f", newVal.doubleValue())));
    }

    /**
     * Initialize the translations of the view.
     */
    private void initTranslations() {
        LanguageManager languageManager = LanguageManager.getInstance();
        languageTitle.setText(languageManager.get("settings.lang_title"));
        languageSelect.setText(languageManager.get("settings.lang_select"));
        left.setText(languageManager.get("settings.left"));
        right.setText(languageManager.get("settings.right"));
        balanceTitle.setText(languageManager.get("settings.balance_title"));
        title = languageManager.get("settings.title");
        saveButton.setText(languageManager.get("button.save"));
        cancelButton.setText(languageManager.get("button.cancel"));
        browseButton.setText(languageManager.get("settings.select_music_folder"));
        equalizerButton.setText(languageManager.get("settings.manage_audio_equalizer"));
    }

    /**
     * Refresh the language of the view.
     */
    public void refreshLanguage() {
        initTranslations();
        updateLanguageComboBox();
    }

    /**
     * Initialize the buttons of the view.
     */
    private void initButtons() {
        saveButton.setOnMouseClicked(_ -> handleSave());
        cancelButton.setOnMouseClicked(_ -> listener.handleCancel());
        browseButton.setOnMouseClicked(_ -> handleBrowseDirectory());
        equalizerButton.setOnMouseClicked(_ -> listener.openEqualizer());
    }

    /**
     * Handle when the browse button is pressed
     */
    private void handleBrowseDirectory() {
        File selectedDirectory = FileDialogHelper.chooseDirectory(null, "Select Music Folder");
        if (selectedDirectory != null) {
            this.directoryLabel.setText(selectedDirectory.getAbsolutePath());
        }
    }

    /**
     * Get the title of the view.
     *
     * @return The title of the view.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the scene of the view.
     *
     * @return The scene of the view.
     */
    public Scene getScene() {
        return scene;
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
        double balance = balanceSlider.getValue();
        Path musicDirectory = Paths.get(directoryLabel.getText());
        listener.handleSave(selectedLanguage, balance, musicDirectory);
    }

    /**
     * Update the view with the current settings.
     */
    public void updateView(Settings settings) {
        initComboBox();
        initTranslations();
        balanceSlider.setValue(settings.getBalance());
        directoryLabel.setText(settings.getMusicFolder().toString());
    }
}
