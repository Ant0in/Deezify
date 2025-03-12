package MusicApp.Controllers;

import MusicApp.Models.Settings;
import MusicApp.Views.SettingsView;
import MusicApp.utils.DataProvider;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.stage.Stage;
import javafx.stage.Modality;

import java.io.IOException;
import java.nio.file.Paths;

public class SettingsController {
    private Stage settingsStage;
    private SettingsView settingsView;
    private MetaController metaController;
    private StringProperty musicDirectoryPath = new SimpleStringProperty("None");

    public SettingsController(MetaController metaController) throws IOException {
        this.metaController = metaController;
        this.settingsView = new SettingsView(this);
        this.settingsStage = new Stage();
        this.settingsStage.initModality(Modality.APPLICATION_MODAL);
        this.settingsStage.setResizable(false);
        this.settingsStage.setTitle("Settings");
        this.settingsStage.setScene(settingsView.getScene());
        this.musicDirectoryPath.set(metaController.getSettings().getMusicDirectory().toString());
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
     * Get the settings view.
     *
     * @return The settings view.
     */
    public StringProperty getMusicDirectoryPath() {
        return musicDirectoryPath;
    }

    /**
     * Set the music directory path.
     *
     * @param path The path to set.
     */
    public void setMusicDirectoryPath(String path) {
        musicDirectoryPath.set(path);
        metaController.setMusicDirectoryPath(Paths.get(path));
    }

    /**
     * Handle when settings are changed
     */
    public void onSettingsChanged(Settings newSettings) {
    }

    /**
     * Get the balance of the application.
     *
     * @return The balance of the application.
     */
    public double getBalance() {
        return metaController.getSettings().getBalance();
    }

    /**
     * Set the balance of the application.
     *
     * @param balance The balance to set.
     */
    public void setBalance(double balance) {
        metaController.updateBalance(balance);
    }
}
