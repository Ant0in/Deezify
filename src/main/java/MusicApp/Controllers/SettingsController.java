package MusicApp.Controllers;

import MusicApp.Views.SettingsView;
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

    public void setBalance(double balance) {
        metaController.getPlayerController().setBalance(balance);
    }

    public double getBalance() {
        return metaController.getPlayerController().getBalance();
    }

    public StringProperty getMusicDirectoryPath(){return musicDirectoryPath;}

    public void setMusicDirectoryPath(String path){
        musicDirectoryPath.set(path);
        metaController.setMusicDirectoryPath(Paths.get(path));
    }
}
