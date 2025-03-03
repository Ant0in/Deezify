package MusicApp.Controllers;

import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Path;

public class MetaController {

    public enum Scenes {
        MAINWINDOW
    }

    private final Stage stage;
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

    public void setMusicDirectoryPath(Path musicDirectoryPath){
        playerController.loadLibrary(musicDirectoryPath);
    }
}
