package MusicApp.Controllers;

import MusicApp.Utils.DataProvider;
import MusicApp.Models.Settings;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

public class MetaController {

    /**
     * Enum for the different scenes in the application.
     * NOTE: settings is not a scene but a pop-up window.
     */
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

    /**
     * Switches the scene to the specified scene.
     * @param scene The scene to switch to.
     */
    public final void switchScene(Scenes scene) {
        if (Objects.requireNonNull(scene) == Scenes.MAINWINDOW) {
            this.playerController.show(this.stage);
        }
    }

    /**
     * Shows the settings window.
     */
    public final void showSettings() {
        this.settingsController.show();
    }

    /**
     * Refreshes the UI.
     */
    public final void refreshUI() {
        this.playerController.refreshUI();
    }

    /**
     * Get the current settings of the application.
     * @return The current settings.
     */
    public Settings getSettings() {
        try {
            return dataProvider.readSettings();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    /**
     * Notify the controllers that the settings have changed.
     * @param newSettings The new settings.
     */
    private void notifySettingsChanged(Settings newSettings) {
        try {
            dataProvider.writeSettings(newSettings);
            playerController.onSettingsChanged(newSettings);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Update the balance of the application.
     * @param balance The new balance.
     */
    public void updateBalance(double balance) {
        try {
            Settings settings = dataProvider.readSettings();
            settings.setBalance(balance);
            notifySettingsChanged(settings);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Set the music directory path and notify the change to the controllers.
     * @param path The path to the music directory.
     */
    public void setMusicDirectoryPath(Path path) {
        try {
            Settings settings = dataProvider.readSettings();
            settings.setMusicFolder(path);
            notifySettingsChanged(settings);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

}
