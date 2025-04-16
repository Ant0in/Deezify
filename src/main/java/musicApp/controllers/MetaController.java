package musicApp.controllers;

import javafx.stage.Stage;
import musicApp.controllers.settings.SettingsController;
import musicApp.models.Library;
import musicApp.models.Settings;
import musicApp.utils.AlertService;
import musicApp.utils.DataProvider;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * The Meta controller.
 */
public class MetaController {

    private final AlertService alertService;
    private final Stage stage;
    private final DataProvider dataProvider;
    private final PlayerController playerController;
    private final SettingsController settingsController;
    private final List<Library> playlists;

    /**
     * Instantiates a new Meta controller.
     *
     * @param stage the stage
     * @throws IOException the io exception
     */
    public MetaController(Stage stage) throws IOException {
        this.stage = stage;
        alertService = new AlertService();
        dataProvider = new DataProvider();
        playlists = dataProvider.loadAllLibraries();
        playerController = new PlayerController(this, dataProvider.readSettings(), getMainLibrary());
        settingsController = new SettingsController(this, dataProvider.readSettings());
    }


    /**
     * Switches the scene to the specified scene.
     *
     * @param scene The scene to switch to.
     */
    public final void switchScene(Scenes scene) {
        switch (scene) {
            case MAINWINDOW -> playerController.show(stage);
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
            dataProvider.writeSettings(newSettings);
            Library mainLibrary = dataProvider.loadMainLibrary(newSettings.getMusicFolder());
            playlists.set(0, mainLibrary);
            playerController.onSettingsChanged(newSettings);
        } catch (Exception e) {
            alertService.showExceptionAlert(e);
        }
    }

    /**
     * Get the playlists.
     *
     * @return the playlists
     */
    public List<Library> getPlaylists() {
        return playlists;
    }

    /**
     * Get the main library.
     *
     * @return the main library
     */
    public Library getMainLibrary() {
        return playlists.getFirst();
    }

    /**
     * Enum for the different scenes in the application.
     * NOTE: settings is not a scene but a pop-up window.
     */
    public enum Scenes {
        MAINWINDOW,
        SETTINGS
    }

    /**
     * Retrieves the current music directory from the settings controller.
     *
     * @return The path to the music directory as defined in the settings.
     */
    public Path getMusicDirectory() {
        return settingsController.getMusicDirectory();
    }
}