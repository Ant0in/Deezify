package MusicApp.Views;

import MusicApp.Controllers.SettingsController;
import MusicApp.utils.LanguageManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;

public class SettingsView {
    private final Scene scene;
    @FXML
    private ComboBox<String> languageComboBox;
    @FXML
    private Slider balanceSlider;
    @FXML
    private Label balanceLabel, balanceTitle, languageTitle, languageSelect, left, right;
    @FXML
    private Button saveButton, cancelButton;

    private String title;
    private final SettingsController settingsController;
    private String originalLanguage;

    public SettingsView(SettingsController settingsController) throws IOException {
        this.settingsController = settingsController;
        URL url = PlayerView.class.getResource("/fxml/settings.fxml");
        FXMLLoader loader = new FXMLLoader(url);
        loader.setController((Object) this);
        Pane root = loader.load();
        this.scene = new Scene(root);
        init();
    }

    public void show(Stage stage) {
        stage.setScene(this.scene);
        stage.setTitle(getTitle());
        stage.show();
    }

    private void init() {
        originalLanguage = LanguageManager.getCurrentLocale().getLanguage();
        initComboBox();
        initSlider();
        initTranslations();
        initButtons();
    }

    private void updateLanguageComboBox(){
        String currentLanguage = LanguageManager.getCurrentLocale().getLanguage();
        if (currentLanguage.equals("en")) {
            languageComboBox.getSelectionModel().select(0);
        } else if (currentLanguage.equals("fr")) {
            languageComboBox.getSelectionModel().select(1);
        } else if (currentLanguage.equals("nl")) {
            languageComboBox.getSelectionModel().select(2);
        }
    }

    private void initComboBox() {
        languageComboBox.getItems().clear();
        languageComboBox.getItems().addAll("English", "Français", "Nederlands");
        updateLanguageComboBox();
        languageComboBox.setOnAction(e -> handleLanguageChange());
    }

    private void handleLanguageChange() {
        String selected = languageComboBox.getSelectionModel().getSelectedItem();
        if (selected.equals("English")) {
            LanguageManager.setLanguage("en");
        } else if (selected.equals("Français")) {
            LanguageManager.setLanguage("fr");
        } else if (selected.equals("Nederlands")) {
            LanguageManager.setLanguage("nl");
        }
        refreshLanguage();
    }

    private void initSlider() {
        balanceSlider.setValue(settingsController.getBalance());
        balanceLabel.setText(String.format("%.2f", settingsController.getBalance()));
        balanceSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            balanceLabel.setText(String.format("%.2f", newVal.doubleValue()));
        });
    }

    private void initTranslations() {
        languageTitle.setText(LanguageManager.get("settings.lang_title"));
        languageSelect.setText(LanguageManager.get("settings.lang_select"));
        left.setText(LanguageManager.get("settings.left"));
        right.setText(LanguageManager.get("settings.right"));
        balanceTitle.setText(LanguageManager.get("settings.balance_title"));
        title = LanguageManager.get("settings.title");
        saveButton.setText(LanguageManager.get("settings.save"));
        cancelButton.setText(LanguageManager.get("settings.cancel"));
    }

    public void refreshLanguage() {
        initTranslations();
        updateLanguageComboBox();
    }

    private void initButtons() {
        saveButton.setOnMouseClicked(event -> handleSave());
        cancelButton.setOnMouseClicked(event -> handleCancel());
    }

    @FXML
    public void handleSave() {
        settingsController.refreshLanguage();
        settingsController.setBalance(balanceSlider.getValue());
        settingsController.close();
    }

    @FXML
    public void handleCancel() {
        LanguageManager.setLanguage(originalLanguage);
        settingsController.refreshLanguage();
        settingsController.close();
    }

    public String getTitle() {
        return title;
    }

    public Scene getScene() {
        return scene;
    }


}
