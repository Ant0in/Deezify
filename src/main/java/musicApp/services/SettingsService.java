package musicApp.services;

import musicApp.exceptions.SettingsFilesException;
import musicApp.models.Settings;
import musicApp.repositories.JsonRepository;

public class SettingsService {
    private final JsonRepository jsonRepository;

    public SettingsService() throws SettingsFilesException {
        jsonRepository = new JsonRepository();
    }

    public void writeSettings(Settings settings) {
        jsonRepository.writeSettings(settings);
    }

    public Settings readSettings() throws SettingsFilesException {
        return jsonRepository.readSettings();
    }
}
