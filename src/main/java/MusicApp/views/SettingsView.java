package MusicApp.views;

import MusicApp.controllers.SettingsController;
import MusicApp.utils.LanguageManager;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import java.io.File;

/**
 * The Settings view.
 */
@SuppressWarnings("unused")
public class SettingsView extends View<SettingsView, SettingsController> {
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
    private Button browseButton;
    @FXML
    private Label directoryLabel;

    private String title;
    private String originalLanguage;

    /**
     * Instantiates a new Settings view.
     */
    public SettingsView() {
    }

    /**
     * Show the settings view.
     *
     * @param stage The stage to show the view on.
     */
    public void show(Stage stage) {
        stage.setScene(this.scene);
        stage.setTitle(getTitle());
        stage.show();
    }

    /**
     * Initialize the settings view.
     */
    @Override
    public void init() {
        originalLanguage = LanguageManager.getInstance().getCurrentLocale().getLanguage();
        initComboBox();
        initSlider();
        initTranslations();
        initButtons();
        initBinding();
    }

    /**
     * Update the language combobox to display the current language.
     */
    private void updateLanguageComboBox(){
        String currentLanguage = LanguageManager.getInstance().getCurrentLocale().getLanguage();
        switch (currentLanguage) {
            case "en" -> languageComboBox.getSelectionModel().select(0);
            case "fr" -> languageComboBox.getSelectionModel().select(1);
            case "nl" -> languageComboBox.getSelectionModel().select(2);
        }
    }

    /**
     * Initialize the language combobox to display the possible languages
     */
    private void initComboBox() {
        languageComboBox.getItems().clear();
        languageComboBox.getItems().addAll("English", "Français", "Nederlands");
        updateLanguageComboBox();
        languageComboBox.setOnAction(_ -> handleLanguageChange());
    }

    /**
     * Handle the language change event.
     */
    private void handleLanguageChange() {
        String selected = languageComboBox.getSelectionModel().getSelectedItem();
        switch (selected) {
            case "English" -> LanguageManager.getInstance().setLanguage("en");
            case "Français" -> LanguageManager.getInstance().setLanguage("fr");
            case "Nederlands" -> LanguageManager.getInstance().setLanguage("nl");
        }
        refreshLanguage();
    }

    /**
     * Initialize the balance slider.
     */
    private void initSlider() {
        balanceSlider.setValue(this.viewController.getBalance());
        balanceLabel.setText(String.format("%.2f", this.viewController.getBalance()));
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
        saveButton.setText(languageManager.get("settings.save"));
        cancelButton.setText(languageManager.get("settings.cancel"));
        browseButton.setText(languageManager.get("settings.select_music_folder"));
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
        cancelButton.setOnMouseClicked(_ -> handleCancel());
        browseButton.setOnMouseClicked(_->handleBrowseDirectory());
    }

    /**
     * Initialize the binding of the view.
     */
    private void initBinding(){
        directoryLabel.textProperty().bind(this.viewController.getMusicDirectoryPath());
    }

    /**
     * Handle when the save button is pressed
     */
    @FXML
    public void handleSave() {
        this.viewController.refreshLanguage();
        this.viewController.setBalance(balanceSlider.getValue());
        this.viewController.close();
    }

    /**
     * Handle when the cancel button is pressed
     */
    @FXML
    public void handleCancel() {
        LanguageManager.getInstance().setLanguage(originalLanguage);
        this.viewController.refreshLanguage();
        this.viewController.close();
    }

    /**
     * Handle when the browse button is pressed
     */
    @FXML
    private void handleBrowseDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Music Folder");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        File selectedDirectory = directoryChooser.showDialog(null);
        if (selectedDirectory != null) {
            this.viewController.setMusicDirectoryPath(selectedDirectory.getAbsolutePath());
        }
    }

    /**
     * Get the title of the view.
     * @return The title of the view.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the scene of the view.
     * @return The scene of the view.
     */
    public Scene getScene() {
        return scene;
    }


}
