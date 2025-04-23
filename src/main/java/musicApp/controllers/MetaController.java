package musicApp.controllers;

import javafx.stage.Stage;
import musicApp.controllers.settings.SettingsController;
import musicApp.models.Settings;
import musicApp.services.AlertService;
import musicApp.services.SettingsService;

import java.io.IOException;

/**
 * The Meta controller.
 */
public class MetaController {
    private final AlertService alertService;
    private final SettingsService settingsService;
    private final PlayerController playerController;
    private final SettingsController settingsController;

    /**
     * Enum for the different scenes in the application.
     * NOTE: settings is not a scene but a pop-up window.
     */
    public enum Scenes {
        MAINWINDOW,
        SETTINGS
    }

    /**
     * Instantiates a new Meta controller.
     *
     * @throws IOException the io exception
     */
    public MetaController(Stage primaryStage) throws IOException {
        alertService = new AlertService();
        settingsService = new SettingsService();
        Settings settings = settingsService.readSettings();
        playerController = new PlayerController(this, primaryStage, settings.toDTO());
        settingsController = new SettingsController(this, settings);
    }


    /**
     * Switches the scene to the specified scene.
     *
     * @param scene The scene to switch to.
     */
    public final void switchScene(Scenes scene) {
        switch (scene) {
            case MAINWINDOW -> playerController.show();
            case SETTINGS -> settingsController.show();
        }
    }

    /**
     * Refreshes the UI.
     */
    public final void refreshUI() {
        playerController.refreshUI();
    }

    /**
     * Notify the controllers that the settings have changed.
     *
     * @param newSettings The new settings.
     */
    public void notifySettingsChanged(Settings newSettings) {
        try {
            settingsService.writeSettings(newSettings);
            playerController.onSettingsChanged(newSettings.toDTO());
        } catch (Exception e) {
            alertService.showExceptionAlert(e);
        }
    }

}