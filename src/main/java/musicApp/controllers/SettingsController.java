package musicApp.controllers;

import musicApp.views.SettingsView;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.stage.Stage;
import javafx.stage.Modality;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * The type Settings controller.
 */
public class SettingsController extends ViewController<SettingsView, SettingsController> {
    private final Stage settingsStage;
    private final MetaController metaController;
    private final StringProperty musicDirectoryPath = new SimpleStringProperty("None");

    /**
     * Instantiates a new Settings controller.
     *
     * @param metaController the meta controller
     * @throws IOException the io exception
     */
    public SettingsController(MetaController metaController) throws IOException {
        super(new SettingsView());
        this.metaController = metaController;
        initView("/fxml/Settings.fxml");
        this.settingsStage = new Stage();
        this.settingsStage.initModality(Modality.APPLICATION_MODAL);
        this.settingsStage.setResizable(false);
        this.settingsStage.setTitle("Settings");
        this.settingsStage.setScene(this.view.getScene());
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
    public StringProperty getMusicDirectoryPath(){return musicDirectoryPath;}

    /**
     * Set the music directory path.
     *
     * @param path The path to set.
     */
    public void setMusicDirectoryPath(String path){
        musicDirectoryPath.set(path);
        metaController.setMusicDirectoryPath(Paths.get(path));
    }

    /**
     * Set the balance of the application.
     *
     * @param balance The balance to set.
     */
    public void setBalance(double balance) {
        metaController.updateBalance(balance);
    }

    /**
     * Get the balance of the application.
     *
     * @return The balance of the application.
     */
    public double getBalance() {
        return metaController.getSettings().getBalance();
    }
}
