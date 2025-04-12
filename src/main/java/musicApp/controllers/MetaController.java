package musicApp.controllers;

import javafx.scene.control.Alert;
import javafx.stage.Stage;
import musicApp.controllers.settings.SettingsController;
import musicApp.models.Library;
import musicApp.models.Settings;
import musicApp.models.Song;
import musicApp.utils.AlertService;
import musicApp.utils.DataProvider;
import musicApp.utils.MusicLoader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * The Meta controller.
 */
public class MetaController {

    private final AlertService alertService = new AlertService();
    private final Stage stage;
    private final DataProvider dataProvider = new DataProvider();
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
        this.playlists = dataProvider.readPlaylists();
        Library mainLibrary = loadMainLibraryFromPath(dataProvider.readSettings().getMusicFolder());
        this.playlists.add(0, mainLibrary);
        this.playerController = new PlayerController(this, dataProvider.readSettings(), mainLibrary);
        this.settingsController = new SettingsController(this, dataProvider.readSettings());
    }
    

    /**
     * Switches the scene to the specified scene.
     *
     * @param scene The scene to switch to.
     */
    public final void switchScene(Scenes scene) {
        switch (scene) {
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
            this.playlists.set(0, loadMainLibraryFromPath(newSettings.getMusicFolder()));
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
        return this.playlists.getFirst();
    }

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

    /**
     * Get the music directory from the settings controller.
     * @return
     */
    public Path getMusicDirectory() {
        return this.settingsController.getMusicDirectory();
    }

    /**
     * Charge the main library from the settings.
     *
     * @return the main library
     */
    public Library loadMainLibraryFromPath(Path musicDirectory) {
        try {
            MusicLoader loader = new MusicLoader();
            List<Song> songs = loader.getAllSongs(musicDirectory);
            return new Library(songs, "??library??", null);
        } catch (IOException e) {
            alertService.showExceptionAlert(e);
            return new Library(); // fallback
        }
    }  

}
