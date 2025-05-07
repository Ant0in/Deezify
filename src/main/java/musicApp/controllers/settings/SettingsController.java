package musicApp.controllers.settings;

import musicApp.controllers.MetaController;
import musicApp.controllers.ViewController;
import musicApp.enums.Language;
import musicApp.models.Settings;
import musicApp.models.UserProfile;
import musicApp.models.dtos.SettingsDTO;
import musicApp.services.LanguageService;
import musicApp.views.settings.SettingsView;

import java.io.IOException;
import java.nio.file.Path;

/**
 * The type Settings controller.
 */
public class SettingsController extends ViewController<SettingsView> implements SettingsView.SettingsViewListener {

    private final MetaController metaController;
    private final EqualizerController equalizerController;
    private final Settings settings;

    /**
     * Instantiates a new Settings controller.
     *
     * @param _controller the meta controller
     * @param _settings   the settings
     * @throws IOException the io exception
     */
    public SettingsController(MetaController _controller, Settings _settings) throws IOException {
        super(new SettingsView());
        view.setListener(this);
        settings = _settings;
        metaController = _controller;
        equalizerController = new EqualizerController(this, _settings.getEqualizer());
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
     * Updates the values of the equalizer with the values of sliders and changes the settings
     */
    private void updateEqualizer() {
        equalizerController.update();
    }

    /**
     * Get the balance of the application.
     *
     * @return The balance of the application.
     */
    public double getBalance() {
        return settings.getBalance();
    }

    @Override
    public double getCrossfadeDuration() {
        return settings.getCrossfadeDuration();
    }


    private void setCrossfadeDuration(double crossfadeDuration) {
        settings.setCrossfadeDuration(crossfadeDuration);
    }

    /**
     * Update the balance of the application.
     *
     * @param balance The new balance.
     */
    private void setBalance(double balance) {
        settings.setBalance(balance);
    }


    public void setUserProfile(UserProfile userProfile) {
        settings.setCurrentUserProfile(userProfile);
    }

    /**
     * Open equalizer.
     */
    public void handleOpenEqualizer() {
        close();
        equalizerController.show();
    }

    /**
     * Handle when the save button is pressed
     *
     * @param language       the language
     * @param balance        the balance
     * @param musicFolder the music folder
     */
    public void handleSave(Language language, double balance, Path musicFolder, double crossfadeDuration) {
        LanguageService.getInstance().setLanguage(language);
        refreshLanguage();
        setBalance(balance);
        setCrossfadeDuration(crossfadeDuration);
        setMusicFolderPath(musicFolder);
        updateEqualizer();
        metaController.notifySettingsChanged(settings);
        updateView();
        close();
    }

    private void updateView() {
        view.updateView(settings.getBalance(), settings.getMusicFolderString());
        refreshLanguage();
    }

    /**
     * Handle when the cancel button is pressed
     */
    public void handleCancel() {
        equalizerController.handleCancel();
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

    public SettingsDTO getSettingsDTO() {
        return settings.toDTO();
    }

    public Path getUserPlaylistPath() {
        return settings.getUserPlaylistPath();
    }
}
