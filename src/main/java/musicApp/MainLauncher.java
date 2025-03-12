package MusicApp;

/**
 * Launcher class that works around JavaFX module system requirements.
 * This class is a standard Java class that can be launched with java -jar.
 * It sets up the necessary module path and options before launching the actual JavaFX application.
 */
public class MainLauncher {

    public static void main(String[] args) {
        // Set system property to suppress the module warning
        System.setProperty("javafx.verbose", "false");
        // For severe module issues, this property can help in some environments
        System.setProperty("illegal-access", "permit");
        // Launch the actual JavaFX application
        Main.main(args);
    }
}
