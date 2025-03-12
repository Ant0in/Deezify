package musicApp.views;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import musicApp.controllers.SettingsController;
import musicApp.utils.LanguageManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class SettingsView {
    private final Scene scene;
    private final SettingsController settingsController;
    private final LanguageManager languageManager;
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

    public SettingsView(SettingsController settingsController) throws IOException {
        this.settingsController = settingsController;
        this.languageManager = LanguageManager.getInstance();
        URL url = PlayerView.class.getResource("/fxml/settings.fxml");
        FXMLLoader loader = new FXMLLoader(url);
        loader.setController((Object) this);
        Pane root = loader.load();
        this.scene = new Scene(root);
        init();
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
    private void init() {
        originalLanguage = languageManager.getCurrentLocale().getLanguage();
        initComboBox();
        initSlider();
        initTranslations();
        initButtons();
        initBinding();
    }

    /**
     * Update the language combobox to display the current language.
     */
    private void updateLanguageComboBox() {
        String currentLanguage = languageManager.getCurrentLocale().getLanguage();
        if (currentLanguage.equals("en")) {
            languageComboBox.getSelectionModel().select(0);
        } else if (currentLanguage.equals("fr")) {
            languageComboBox.getSelectionModel().select(1);
        } else if (currentLanguage.equals("nl")) {
            languageComboBox.getSelectionModel().select(2);
        }
    }

    /**
     * Initialize the language combobox to display the possible languages
     */
    private void initComboBox() {
        languageComboBox.getItems().clear();
        languageComboBox.getItems().addAll("English", "Français", "Nederlands");
        updateLanguageComboBox();
        languageComboBox.setOnAction(e -> handleLanguageChange());
    }

    /**
     * Handle the language change event.
     */
    private void handleLanguageChange() {
        String selected = languageComboBox.getSelectionModel().getSelectedItem();
        if (selected.equals("English")) {
            languageManager.setLanguage("en");
        } else if (selected.equals("Français")) {
            languageManager.setLanguage("fr");
        } else if (selected.equals("Nederlands")) {
            languageManager.setLanguage("nl");
        }
        refreshLanguage();
    }

    /**
     * Initialize the balance slider.
     */
    private void initSlider() {
        balanceSlider.setValue(settingsController.getBalance());
        balanceLabel.setText(String.format("%.2f", settingsController.getBalance()));
        balanceSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            balanceLabel.setText(String.format("%.2f", newVal.doubleValue()));
        });
    }

    /**
     * Initialize the translations of the view.
     */
    private void initTranslations() {
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
        saveButton.setOnMouseClicked(event -> handleSave());
        cancelButton.setOnMouseClicked(event -> handleCancel());
        browseButton.setOnMouseClicked(event -> handleBrowseDirectory());
    }

    /**
     * Initialize the binding of the view.
     */
    private void initBinding() {
        directoryLabel.textProperty().bind(settingsController.getMusicDirectoryPath());
    }

    /**
     * Handle when the save button is pressed
     */
    @FXML
    public void handleSave() {
        settingsController.refreshLanguage();
        settingsController.setBalance(balanceSlider.getValue());
        settingsController.close();
    }

    /**
     * Handle when the cancel button is pressed
     */
    @FXML
    public void handleCancel() {
        languageManager.setLanguage(originalLanguage);
        settingsController.refreshLanguage();
        settingsController.close();
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
            settingsController.setMusicDirectoryPath(selectedDirectory.getAbsolutePath());
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


}
