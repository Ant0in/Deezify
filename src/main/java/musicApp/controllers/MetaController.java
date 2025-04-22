package musicApp.controllers;

import javafx.stage.Stage;
import musicApp.controllers.settings.SettingsController;
import musicApp.models.Library;
import musicApp.models.Settings;
import musicApp.services.AlertService;
import musicApp.services.PlaylistService;
import musicApp.services.SettingsService;

import java.io.IOException;
import java.util.List;

/**
 * The Meta controller.
 */
public class MetaController {

    private final AlertService alertService;
    private final Stage stage;
    private final SettingsService settingsService;
    private final PlaylistService playlistService;
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
        settingsService = new SettingsService();
        playlistService = new PlaylistService();
        playlists = playlistService.loadAllLibraries();
        Settings settings = settingsService.readSettings();
        playerController = new PlayerController(this, settings, getMainLibrary());
        settingsController = new SettingsController(this, settings);
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
            settingsService.writeSettings(newSettings);
            Library mainLibrary = playlistService.loadMainLibrary(newSettings.getMusicFolder());
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
}