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

    public void show() {
        if (settingsStage != null) {
            settingsStage.show();
        }
    }

    public void close() {
        if (settingsStage != null) {
            settingsStage.close();
        }
    }

    public void refreshLanguage() {
        metaController.refreshUI();
    }

    public StringProperty getMusicDirectoryPath(){return musicDirectoryPath;}

    public void setMusicDirectoryPath(String path){
        musicDirectoryPath.set(path);
        metaController.setMusicDirectoryPath(Paths.get(path));
    }

    public void setBalance(double balance) {
        metaController.updateBalance(balance);
    }

    public void onSettingsChanged(Settings newSettings) {}

    public double getBalance() {
        return metaController.getSettings().getBalance();
    }
}
