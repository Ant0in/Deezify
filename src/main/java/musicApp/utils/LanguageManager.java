package musicApp.utils;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

/**
 * LanguageManager
 * Manages application language settings and retrieves localized messages.
 * The class is a singleton and uses the Preferences API to store the selected language.
 */
public class LanguageManager {
    private final Preferences prefs = Preferences.userNodeForPackage(LanguageManager.class);
    private final String DEFAULT_LANGUAGE = "en"; // default language if pc language is not supported
    private final String[] SUPPORTED_LANGUAGES = {"fr", "en", "nl"}; // supported languages
    private final String[] BUNDLE_NAMES = {"lang.messages", "lang.general", "lang.errors", "lang.buttons", "lang.settings", "lang.default_values"};

    private final ResourceBundle[] bundles = new ResourceBundle[BUNDLE_NAMES.length];
    private Locale currentLocale;

    private static LanguageManager instance;


    public LanguageManager() {
        String savedLanguage = prefs.get("language", Locale.getDefault().getLanguage());
        if (!isLanguageSupported(savedLanguage)) {
            savedLanguage = DEFAULT_LANGUAGE;
        }
        setLanguage(savedLanguage);
    }

    public LanguageManager(String language) {
        setLanguage(language);
    }

    /**
     * Set the language of the application.
     *
     * @param languageCode The language code (e.g., "en", "fr", "nl").
     */
    public void setLanguage(String languageCode) {
        if (!isLanguageSupported(languageCode)) {
            languageCode = DEFAULT_LANGUAGE; // set default language if not supported
        }
        currentLocale = Locale.forLanguageTag(languageCode);

        for (int i = 0; i < BUNDLE_NAMES.length; i++) {
            bundles[i] = ResourceBundle.getBundle(BUNDLE_NAMES[i], currentLocale);
        }

        prefs.put("language", languageCode); // save the language in the preferences
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
     * Get the current locale.
     *
     * @return The current locale.
     */
    public Locale getCurrentLocale() {
        return currentLocale;
    }

    /**
     * Check if the language is supported.
     *
     * @param languageCode The language code.
     * @return True if the language is supported, False otherwise.
     */
    private boolean isLanguageSupported(String languageCode) {
        for (String lang : SUPPORTED_LANGUAGES) {
            if (lang.equals(languageCode)) {
                return true;
            }
        }
        return false;
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
}


