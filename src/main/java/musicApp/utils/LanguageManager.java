package musicApp.utils;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import musicApp.enums.Language;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

/**
 * LanguageManager
 * Manages application language settings and retrieves localized messages.
 * The class is a singleton and uses the Preferences API to store the selected language.
 */
public class LanguageManager {

    private static LanguageManager instance;

    private final Preferences prefs;
    private final String[] BUNDLE_NAMES;
    private final ResourceBundle[] bundles;
    private final StringProperty languageProperty;

    public LanguageManager() {
        // Initialize non-static variables in the constructor
       prefs = Preferences.userNodeForPackage(LanguageManager.class);
       BUNDLE_NAMES = new String[]{
                "lang.messages",
                "lang.general",
                "lang.buttons",
                "lang.settings",
                "lang.default_values",
                "lang.create_playlist"
        };
       bundles = new ResourceBundle[BUNDLE_NAMES.length];
       languageProperty = new SimpleStringProperty();

        // Initialize language settings based on saved preferences
        String savedLanguageString = prefs.get("language", Locale.getDefault().getLanguage());
        Language savedLanguage = Language.fromCode(savedLanguageString);
        setLanguage(savedLanguage);
    }

    /**
     * Get the instance of the LanguageManager.
     *
     * @return The instance of the LanguageManager.
     */
    public static LanguageManager getInstance() {
        if (instance == null) {
            instance = new LanguageManager();
        }
        return instance;
    }

    /**
     * Set the language of the application.
     *
     * @param language The language
     */
    public void setLanguage(Language language) {
        if (!isLanguageSupported(language)) {
            language = Language.DEFAULT; // set default language if not supported
        }
        Locale currentLocale = Locale.forLanguageTag(language.getCode());

        for (int i = 0; i < BUNDLE_NAMES.length; i++) {
            bundles[i] = ResourceBundle.getBundle(BUNDLE_NAMES[i], currentLocale);
        }

        prefs.put("language", language.getCode()); // save the language in the preferences
        languageProperty.set(language.getCode());
    }

    /**
     * Get the localized message for the given key.
     *
     * @param key The key of the message.
     * @return The localized message.
     */
    public String get(String key) {
        for (ResourceBundle messages : bundles) {
            if (messages.containsKey(key)) return messages.getString(key);
        }
        return "???" + key + "???";
    }

    /**
     * Get the current language.
     *
     * @return The current language.
     */
    public Language getCurrentLanguage() {
        return Language.fromCode(languageProperty.get());
    }

    /**
     * Check if the language is supported.
     *
     * @param language The language.
     * @return True if the language is supported, False otherwise.
     */
    private boolean isLanguageSupported(Language language) {
        return Language.getSupportedLanguages().contains(language);
    }

    public StringProperty getLanguageProperty() {return languageProperty;}
}


