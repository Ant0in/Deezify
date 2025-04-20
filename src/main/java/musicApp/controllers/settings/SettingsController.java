package musicApp.controllers.settings;

import javafx.stage.Modality;
import javafx.stage.Stage;
import musicApp.controllers.MetaController;
import musicApp.controllers.ViewController;
import musicApp.enums.Language;
import musicApp.models.Settings;
import musicApp.services.LanguageService;
import musicApp.views.settings.SettingsView;

import java.io.IOException;
import java.nio.file.Path;

/**
 * The type Settings controller.
 */
public class SettingsController extends ViewController<SettingsView> implements SettingsView.SettingsViewListener {
    private final Stage settingsStage;
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
        settingsStage = new Stage();
        initSettingsStage();
    }

    private void initSettingsStage() {
        settingsStage.initModality(Modality.APPLICATION_MODAL);
        settingsStage.setResizable(false);
        settingsStage.setTitle("Settings");
        settingsStage.setScene(view.getScene());
        settingsStage.setOnCloseRequest(_ -> {
            handleCancel();
        });
    }

    /**
     * Show the settings window.
     */
    public void show() {
        if (settingsStage != null) {
            settingsStage.show();
        }
    }

    /**
     * Close the settings window.
     */
    public void close() {
        if (settingsStage != null) {
            settingsStage.close();
        }
    }

    /**
     * Refresh the language of the settings window.
     */
    public void refreshLanguage() {
        metaController.refreshUI();
    }

    /**
     * Set the music directory path .
     *
     * @param path The path to the music directory.
     */
    private void setMusicDirectoryPath(Path path) {
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

    /**
     * Update the balance of the application.
     *
     * @param balance The new balance.
     */
    private void setBalance(double balance) {
        settings.setBalance(balance);
    }

    /**
     * Open equalizer.
     */
    public void openEqualizer() {
        close();
        equalizerController.show();
    }

    /**
     * Handle when the save button is pressed
     *
     * @param language       the language
     * @param balance        the balance
     * @param musicDirectory the music directory
     */
    public void handleSave(Language language, double balance, Path musicDirectory) {
        LanguageService.getInstance().setLanguage(language);
        refreshLanguage();
        setBalance(balance);
        setMusicDirectoryPath(musicDirectory);
        updateEqualizer();
        metaController.notifySettingsChanged(settings);
        updateView();
        close();
    }

    private void updateView() {
        view.updateView(settings);
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
     * Get the path of the music directory
     *
     * @return The path of the music directory
     */
    public Path getMusicDirectory() {
        return settings.getMusicFolder();
    }

}
