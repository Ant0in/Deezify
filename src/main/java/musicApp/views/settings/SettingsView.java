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
    private Slider balanceSlider, crossfadeSlider;
    @FXML
    private Label balanceLabel, balanceTitle, languageTitle, languageSelect, left, right, musicFolderLabel, crossfadeTitle, crossfadeLabel;
    @FXML
    private Button saveButton, cancelButton, browseButton, equalizerButton;


    
    /**
     * The listener interface for receiving settings view events.
     * Implement this interface to handle user actions in the settings view.
     */
    public interface SettingsViewListener {
        double getBalance();

        double getCrossfadeDuration();

        void handleOpenEqualizer();

        void handleCancel();

        void handleSave(Language language, double balance, Path musicFolder, double crossfadeDuration);

        String getMusicFolderString();
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
        initSlider();
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
     * Initialize the balance slider.
     */
    private void initSlider() {
        balanceSlider.setValue(listener.getBalance());
        balanceLabel.setText(String.format("%.2f",listener.getBalance()));
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

    private String getCrossfadeLabelString(double value) {
        String seconds = LanguageService.getInstance().get("settings.seconds");
        return String.format("%.1f %s", value, seconds);
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
        stage.setTitle(languageService.get("settings.title"));
        saveButton.setText(languageService.get("button.save"));
        cancelButton.setText(languageService.get("button.cancel"));
        browseButton.setText(languageService.get("settings.select_music_folder"));
        equalizerButton.setText(languageService.get("settings.manage_audio_equalizer"));
        crossfadeTitle.setText(languageService.get("settings.crossfade"));
        crossfadeLabel.setText(String.format("%.1f %s",listener.getCrossfadeDuration(), LanguageService.getInstance().get("settings.seconds")));
        updateLanguageComboBox();
    }

    /**
     * Initialize the buttons of the view.
     */
    private void initButtons() {
        saveButton.setOnMouseClicked(_ -> handleSave());
        cancelButton.setOnMouseClicked(_ -> listener.handleCancel());
        browseButton.setOnMouseClicked(_ -> handleBrowseMusicFolder());
        equalizerButton.setOnMouseClicked(_ -> listener.handleOpenEqualizer());
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
        double balance = balanceSlider.getValue();
        double crossfadeDuration = crossfadeSlider.getValue();
        Path musicDirectory = Paths.get(musicFolderLabel.getText());
        listener.handleSave(selectedLanguage, balance, musicDirectory, crossfadeDuration);
    }

    /**
     * Update the view with the current settings.
     */
    public void updateView(double balance, String musicFolderString) {
        initComboBox();
        balanceSlider.setValue(balance);
        musicFolderLabel.setText(musicFolderString);
    }
}
