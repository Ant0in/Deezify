package musicApp.controllers;

import javafx.stage.Stage;
import musicApp.models.Settings;
import musicApp.utils.DataProvider;

import java.io.IOException;
import java.nio.file.Path;

public class MetaController {

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
     *
     * @param scene The scene to switch to.
     */
    public final void switchScene(Scenes scene) {
        switch (scene) {
            case Scenes.MAINWINDOW -> this.playerController.show(this.stage);
        }
    }

    /**
     * Shows the settings window.
     */
    public final void showSettings() {
        this.settingsController.show();
    }

    /**
     * Closes the settings window.
     */
    public final void closeSettings() {
        this.settingsController.close();
    }

    /**
     * Refreshes the UI.
     */
    public final void refreshUI() {
        this.playerController.refreshUI();
    }

    /**
     * Get the player controller.
     *
     * @return The player controller.
     */
    public PlayerController getPlayerController() {
        return playerController;
    }

    /**
     * Get the current settings of the application.
     *
     * @return The current settings.
     */
    public Settings getSettings() {
        try {
            return dataProvider.readSettings();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Notify the controllers that the settings have changed.
     *
     * @param newSettings The new settings.
     */
    private void notifySettingsChanged(Settings newSettings) {
        try {
            dataProvider.writeSettings(newSettings);
            playerController.onSettingsChanged(newSettings);
            settingsController.onSettingsChanged(newSettings);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Update the balance of the application.
     *
     * @param balance The new balance.
     */
    public void updateBalance(double balance) {
        try {
            Settings settings = dataProvider.readSettings();
            settings.setBalance(balance);
            notifySettingsChanged(settings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set the music directory path and notify the change to the controllers.
     *
     * @param path The path to the music directory.
     */
    public void setMusicDirectoryPath(Path path) {
        try {
            Settings settings = dataProvider.readSettings();
            settings.setMusicFolder(path);
            notifySettingsChanged(settings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Enum for the different scenes in the application.
     * NOTE: settings is not a scene but a pop-up window.
     */
    public enum Scenes {
        MAINWINDOW
    }

}
