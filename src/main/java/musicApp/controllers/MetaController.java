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
    private final UserProfileSelectionController userProfileSelectionController;

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
        userProfileSelectionController = new UserProfileSelectionController(this,primaryStage, userProfileService.readUserProfiles());
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
            case USERSWINDOW -> userProfileSelectionController.show();
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

    /**
     * Load the player with the specified user profile.
     * @param userProfile
     */
    public void loadPlayerWithUser(UserProfile userProfile) {
        Settings settings = settingsService.readSettings();
        settings.setCurrentUserProfile(userProfile);
        settingsController = new SettingsController(this, settings);
        playerController = new PlayerController(this, new Stage(), settingsController.getSettingsDTO());
    }

    /**
     * Get the user playlist path.
     * @return
     */
    public Path getUserPlaylistPath() {
        return settingsController.getUserPlaylistPath();
    }

    /**
     * Update the users list in the user profile selection controller.
     */
    public void usersUpdate() {
        userProfileSelectionController.usersUpdate();
    }
}