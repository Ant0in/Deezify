package MusicApp.Controllers;

import MusicApp.Models.Settings;
import MusicApp.utils.DataProvider;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

public class MetaController {

    public enum Scenes {
        MAINWINDOW
    }

    private final Stage stage;
    private final DataProvider dataProvider = new DataProvider();
    private final PlayerController playerController;
    private final SettingsController settingsController;

    public MetaController(Stage stage) throws IOException {
        this.stage = stage;
        this.playerController = new PlayerController(this);
        this.settingsController = new SettingsController(this);
    }

    public final void switchScene(Scenes scene) {
        switch (scene) {
            case Scenes.MAINWINDOW ->this.playerController.show(this.stage);
        }
    }

    public final void showSettings() {
        this.settingsController.show();
    }

    public final void closeSettings() {
        this.settingsController.close();
    }

    public final void refreshUI() {
        this.playerController.refreshUI();
    }

    public PlayerController getPlayerController() {
        return playerController;
    }

    public Settings getSettings() {
        try {
            return dataProvider.readSettings();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void notifySettingsChanged(Settings newSettings) {
        try {
            dataProvider.writeSettings(newSettings);
            playerController.onSettingsChanged(newSettings);
            settingsController.onSettingsChanged(newSettings);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateBalance(double balance) {
        try {
            Settings settings = dataProvider.readSettings();
            settings.setBalance(balance);
            notifySettingsChanged(settings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMusicDirectoryPath(Path path) {
        try {
            Settings settings = dataProvider.readSettings();
            settings.setMusicFolder(path);
            notifySettingsChanged(settings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
