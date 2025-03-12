package musicApp.utils;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

/**
 * LanguageManager
 * Manages application language settings and retrieves localized messages.
 */
public class LanguageManager {
    private static final Preferences prefs = Preferences.userNodeForPackage(LanguageManager.class);
    private static final String DEFAULT_LANGUAGE = "en"; // default language if pc language is not supported
    private static final String[] SUPPORTED_LANGUAGES = {"fr", "en", "nl"}; // supported languages
    private static final String[] BUNDLE_NAMES = {"lang.messages", "lang.general", "lang.errors", "lang.buttons", "lang.settings", "lang.default_values"};
    private static final ResourceBundle[] bundles = new ResourceBundle[BUNDLE_NAMES.length];
    private static Locale currentLocale;


    static {
        // gets the language saved in the preferences, or the default language
        String savedLanguage = prefs.get("language", Locale.getDefault().getLanguage());

        // if the saved language is not supported, set the default language
        if (!isLanguageSupported(savedLanguage)) {
            savedLanguage = DEFAULT_LANGUAGE;
        }
        setLanguage(savedLanguage);
    }

    public static void setLanguage(String languageCode) {
        if (!isLanguageSupported(languageCode)) {
            languageCode = DEFAULT_LANGUAGE; // set default language if not supported
        }
        currentLocale = Locale.forLanguageTag(languageCode);

        for (int i = 0; i < BUNDLE_NAMES.length; i++) {
            bundles[i] = ResourceBundle.getBundle(BUNDLE_NAMES[i], currentLocale);
        }

        prefs.put("language", languageCode); // save the language in the preferences
    }

    public static String get(String key) {
        for (ResourceBundle messages : bundles) {
            if (messages.containsKey(key)) return messages.getString(key);
        }
        return "???" + key + "???";
    }

    public static Locale getCurrentLocale() {
        return currentLocale;
    }

    private static boolean isLanguageSupported(String languageCode) {
        for (String lang : SUPPORTED_LANGUAGES) {
            if (lang.equals(languageCode)) {
                return true;
            }
        }
        return false;
    }
}


