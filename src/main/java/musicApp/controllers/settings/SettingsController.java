package musicApp.controllers.settings;

import musicApp.controllers.EditUserProfileController;
import musicApp.controllers.MetaController;
import musicApp.controllers.ViewController;
import musicApp.enums.Language;
import musicApp.models.Settings;
import musicApp.models.UserProfile;
import musicApp.models.dtos.SettingsDTO;
import musicApp.services.LanguageService;
import musicApp.views.settings.SettingsView;

import java.nio.file.Path;

/**
 * The type Settings controller.
 */
public class SettingsController extends ViewController<SettingsView> implements SettingsView.SettingsViewListener {

    private final MetaController metaController;
    private final Settings settings;

    /**
     * Instantiates a new Settings controller.
     *
     * @param _controller the meta controller
     * @param _settings   the settings
     */
    public SettingsController(MetaController _controller, Settings _settings) {
        super(new SettingsView());
        view.setListener(this);
        settings = _settings;
        metaController = _controller;
        initView("/fxml/Settings.fxml");
    }

    /**
     * Show the settings window.
     */
    public void show() {
        view.show();
    }

    /**
     * Close the settings window.
     */
    public void close() {
        view.close();
    }

    /**
     * Refresh the language of the settings window.
     */
    public void refreshLanguage() {
        metaController.refreshUI();
    }

    /**
     * Set the music folder path .
     *
     * @param path The path to the music folder.
     */
    private void setMusicFolderPath(Path path) {
        settings.setMusicFolder(path);
    }

    /**
     * Set the current user profile.
     * @param userProfile
     */
    public void setUserProfile(UserProfile userProfile) {
        settings.setCurrentUserProfile(userProfile);
    }

    /**
     * Handle when the save button is pressed
     *
     * @param language       the language
     * @param musicFolder the music folder
     */
    public void handleSave(Language language, Path musicFolder) {
        LanguageService.getInstance().setLanguage(language);
        refreshLanguage();
        setMusicFolderPath(musicFolder);
        metaController.notifySettingsChanged(settings);    
        updateView();
        close();
    }

    /**
     * Update the view with the current settings.
     */
    private void updateView() {
        view.updateView(settings.getMusicFolderString());
        refreshLanguage();
    }

    /**
     * Handle when the cancel button is pressed
     */
    public void handleCancel() {
        updateView();
        close();
    }

    /**
     * Get the path of the music folder
     *
     * @return The path of the music folder
     */
    public String getMusicFolderString() {
        return settings.getMusicFolderString();
    }

    /**
     * Get the settings DTO.
     * @return
     */
    public SettingsDTO getSettingsDTO() {
        return settings.toDTO();
    }

    /**
     * Get the current user playlist path.
     * @return
     */
    public Path getUserPlaylistPath() {
        return settings.getUserPlaylistPath();
    }

    /**
     * Open the edit user profile window.
     */
    public void editUserProfile(){
        new EditUserProfileController(metaController, settings.getCurrentUserProfile());
        
    }
}
