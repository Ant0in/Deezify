package musicApp.controllers;

import javafx.stage.Stage;
import musicApp.models.Settings;
import musicApp.utils.DataProvider;

import java.io.IOException;
import java.nio.file.Path;

/**
 * The Meta controller.
 */
public class MetaController {

    /**
     * Enum for the different scenes in the application.
     * NOTE: settings is not a scene but a pop-up window.
     */
    public enum Scenes {
        /**
         * Mainwindow scenes.
         */
        MAINWINDOW,
        SETTINGS
    }

    private final Stage stage;
    private final DataProvider dataProvider = new DataProvider();
    private final PlayerController playerController;
    private final SettingsController settingsController;

    /**
     * Instantiates a new Meta controller.
     *
     * @param stage the stage
     * @throws IOException the io exception
     */
    public MetaController(Stage stage) throws IOException {
        this.stage = stage;
        this.playerController = new PlayerController(this,dataProvider.readSettings());
        this.settingsController = new SettingsController(this, dataProvider.readSettings());
    }

    /**
     * Switches the scene to the specified scene.
     *
     * @param scene The scene to switch to.
     */
    public final void switchScene(Scenes scene) {
        switch (scene){
            case MAINWINDOW -> this.playerController.show(this.stage);
            case SETTINGS -> this.settingsController.show();
        }
    }

    /**
     * Refreshes the UI.
     */
    public final void refreshUI() {
        this.playerController.refreshUI();
    }


    /**
     * Notify the controllers that the settings have changed.
     *
     * @param newSettings The new settings.
     */
    public void notifySettingsChanged(Settings newSettings) {
        try {
            dataProvider.writeSettings(newSettings);
            playerController.onSettingsChanged(newSettings);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Get the music directory from the settings controller.
     * @return
     */
    public Path getMusicDirectory() {
        return this.settingsController.getMusicDirectory();
    }
}
