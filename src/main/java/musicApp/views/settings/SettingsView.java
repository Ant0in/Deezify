package musicApp.views.settings;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import musicApp.enums.Language;
import musicApp.models.Settings;
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
    
    // private final Scene scene;
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
     * @param newListener the listener
     */
    public void setListener(SettingsViewListener newListener) {
        listener = newListener;
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
        initButtons();
        initDirectoryLabel();
        refreshTranslation();
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
    @Override
    protected void refreshTranslation() {
        LanguageService languageService = LanguageService.getInstance();
        languageTitle.setText(languageService.get("settings.lang_title"));
        languageSelect.setText(languageService.get("settings.lang_select"));
        left.setText(languageService.get("settings.left"));
        right.setText(languageService.get("settings.right"));
        balanceTitle.setText(languageService.get("settings.balance_title"));
        title = languageService.get("settings.title");
        saveButton.setText(languageService.get("button.save"));
        cancelButton.setText(languageService.get("button.cancel"));
        browseButton.setText(languageService.get("settings.select_music_folder"));
        equalizerButton.setText(languageService.get("settings.manage_audio_equalizer"));
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
        File selectedDirectory = FileDialogService.chooseDirectory(null, "Select Music Folder");
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
        balanceSlider.setValue(settings.getBalance());
        directoryLabel.setText(settings.getMusicFolder().toString());
    }
}
