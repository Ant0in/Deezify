package musicApp.controllers;

import javafx.stage.Stage;
import musicApp.controllers.settings.SettingsController;
import musicApp.models.Settings;
import musicApp.models.UserProfile;
import musicApp.services.AlertService;
import musicApp.services.SettingsService;
import musicApp.services.UserProfileService;

import java.io.IOException;
import java.nio.file.Path;

/**
 * The Meta controller.
 */
public class MetaController implements EditUserProfileController.EditUserProfileControllerListener {
    private final AlertService alertService;
    private final SettingsService settingsService;
    private PlayerController playerController;
    private SettingsController settingsController;
    private final UserProfilesController userProfilesController;

    /**
     * Enum for the different scenes in the application.
     * NOTE: settings is not a scene but a pop-up window.
     */
    public enum Scenes {
        MAINWINDOW,
        SETTINGS,
        USERSWINDOW
    }

    /**
     * Instantiates a new Meta controller.
     *
     * @throws IOException the io exception
     */
    public MetaController(Stage primaryStage) throws IOException {
        alertService = new AlertService();
        settingsService = new SettingsService();
        UserProfileService userProfileService = new UserProfileService();
        userProfilesController = new UserProfilesController(this,primaryStage, userProfileService.readUsers());

//         Settings settings = settingsService.readSettings();
// //        playerController = new PlayerController(this, primaryStage, settings.toDTO());
//         settingsController = new SettingsController(this, settings);
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
            case USERSWINDOW -> userProfilesController.show();
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
            usersUpdate();
            settingsService.writeSettings(newSettings);
            playerController.onSettingsChanged(newSettings.toDTO());
        } catch (Exception e) {
            alertService.showExceptionAlert(e);
        }        
    }

    public void loadPlayerWithUser(UserProfile userProfile) throws IOException {
        Settings settings = settingsService.readSettings();
        settings.setCurrentUserProfile(userProfile);
        settingsController = new SettingsController(this, settings);
        playerController = new PlayerController(this, new Stage(), settingsController.getSettingsDTO());
    }


    public Path getUserPlaylistPath() {
        return settingsController.getUserPlaylistPath();
    }

    public void usersUpdate() {
        userProfilesController.usersUpdate();
    }

}