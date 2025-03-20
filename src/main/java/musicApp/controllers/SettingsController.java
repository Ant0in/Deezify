package musicApp.controllers;

import javafx.fxml.FXML;
import musicApp.enums.Language;
import musicApp.utils.LanguageManager;
import musicApp.views.EqualizerView;
import musicApp.views.SettingsView;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.stage.Modality;
import javafx.stage.Stage;
import musicApp.models.Settings;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The type Settings controller.
 */
public class SettingsController extends ViewController<SettingsView, SettingsController> {
    private final Stage settingsStage;
    private final MetaController metaController;
    private final EqualizerController equalizerController;
    private final Settings settings;

    /**
     * Instantiates a new Settings controller.
     *
     * @param metaController the meta controller
     * @throws IOException the io exception
     */
    public SettingsController(MetaController metaController, Settings settings) throws IOException {
        super(new SettingsView());
        this.settings = settings;
        this.metaController = metaController;
        this.equalizerController = new EqualizerController(this, settings.getEqualizerBands());
        initView("/fxml/Settings.fxml");
        this.settingsStage = new Stage();
        this.settingsStage.initModality(Modality.APPLICATION_MODAL);
        this.settingsStage.setResizable(false);
        this.settingsStage.setTitle("Settings");
        this.settingsStage.setScene(this.view.getScene());
    }

    /**
     * Show the settings window.
     */
    public void show() {
        if (settingsStage != null) {
            settingsStage.show();
        }
    }

    /**
     * Close the settings window.
     */
    public void close() {
        if (settingsStage != null) {
            settingsStage.close();
        }
    }

    /**
     * Refresh the language of the settings window.
     */
    public void refreshLanguage() {
        metaController.refreshUI();
    }

    /**
     * Set the music directory path .
     *
     * @param path The path to the music directory.
     */
    private void setMusicDirectoryPath(Path path) {
        settings.setMusicFolder(path);
    }

    /**
     * Update the balance of the application.
     *
     * @param balance The new balance.
     */
    private void setBalance(double balance) {
        settings.setBalance(balance);
    }


    /**
     * Get the balance of the application.
     *
     * @return The balance of the application.
     */
    public double getBalance() {
        return settings.getBalance();
    }

    public void openEqualizer() {
        close();
        equalizerController.show();
    }

    public void updateEqualizerBand(int bandIndex, double value){
        settings.setEqualizerBand(bandIndex, value);
        metaController.updateEqualizerBand(bandIndex, value);
    }

    /**
     * Handle when the save button is pressed
     */
    public void handleSave(Language language, double balance, Path musicDirectory) {
        LanguageManager.getInstance().setLanguage(language);
        refreshLanguage();
        setBalance(balance);
        setMusicDirectoryPath(musicDirectory);
        metaController.notifySettingsChanged(settings);
        updateView();
        close();
    }

    private void updateView(){
        view.updateView(settings);
        refreshLanguage();
    }

    /**
     * Handle when the cancel button is pressed
     */
    public void handleCancel() {
        updateView();
        close();
    }

    public String getMusicDirectory() {
        return settings.getMusicDirectory().toString();
    }
}
