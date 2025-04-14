package musicApp.services;

import musicApp.models.Settings;
import musicApp.repositories.JsonRepository;

public class SettingsService {
    private final JsonRepository jsonRepository;

    public SettingsService() {
        jsonRepository = new JsonRepository();
    }

    public void writeSettings(Settings settings) {
        jsonRepository.writeSettings(settings);
    }

    public Settings readSettings() {
        return jsonRepository.readSettings();
    }
}
