package musicApp.controllers;

import javafx.stage.Stage;
import musicApp.controllers.settings.SettingsController;
import musicApp.enums.Language;
import musicApp.exceptions.SettingsFilesException;
import musicApp.models.Settings;
import musicApp.models.UserProfile;
import musicApp.services.AlertService;
import musicApp.services.SettingsService;
import musicApp.services.UserProfileService;

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
     */
    public MetaController(Stage primaryStage) {
        alertService = new AlertService();
        try {
            settingsService = new SettingsService();
            UserProfileService userProfileService = new UserProfileService();
            userProfileSelectionController = new UserProfileSelectionController(this,primaryStage, userProfileService.readUserProfiles());
        } catch (SettingsFilesException e) {
            alertService.showFatalErrorAlert("Error loading settings", e);
            throw new RuntimeException(e);
        }
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
     * @param userProfile The user profile to load.
     */
    public void loadPlayerWithUser(UserProfile userProfile) {
        try {
            Settings settings = settingsService.readSettings();
            settings.setCurrentUserProfile(userProfile);
            settingsController = new SettingsController(this, settings);
            playerController = new PlayerController(this, new Stage(), settingsController.getSettingsDTO());
        } catch (SettingsFilesException e) {
            alertService.showFatalErrorAlert("Error loading settings", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the user playlist path.
     * @return The user playlist path.
     */
    public Path getUserPlaylistPath() {
        return settingsController.getUserPlaylistPath();
    }

    /**
     * Get the user library path.
     * @return The user library path.
     */
    public Path getUserMusicFolder() {
        return settingsController.getUserMusicFolder();
    }

    public Path getMusicFolder() {
        return settingsController.getMusicFolder();
    }


    /**
     * Update the users list in the user profile selection controller.
     */
    public void usersUpdate() {
        userProfileSelectionController.usersUpdate();
    }

    /**
     * Get the app's default language.
     *
     * @return the default language.
     */
    public Language getDefaultLanguage() {
        try {
            return settingsService.readSettings().getCurrentLanguage();
        } catch (SettingsFilesException e) {
            alertService.showFatalErrorAlert("Error loading settings", e);
            throw new RuntimeException(e);
        }
    }
}