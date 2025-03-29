package musicApp.utils;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigPathUtil {


    public static Path getConfigDir() {
        String os = System.getProperty("os.name").toLowerCase();
        String configDir;

        if (os.contains("win")) {
            // for Windows
            // Use APPDATA environment variable if available
            configDir = System.getenv("APPDATA");
            if (configDir == null) {
                configDir = System.getProperty("user.home") + "\\AppData\\Roaming";
            }
            return Paths.get(configDir, "musicApp", "lyrics");
        } else {
            // for MacOS, Linux and other OS
            configDir = System.getProperty("user.home") + "/.config";
            return Paths.get(configDir, "musicApp", "lyrics");
        }
    }
}
