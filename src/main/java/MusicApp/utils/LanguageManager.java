package app.localization;

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
    private static Locale currentLocale;
    private static ResourceBundle messages;

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
        messages = ResourceBundle.getBundle("lang.messages", currentLocale);
        prefs.put("language", languageCode); // save the language in the preferences
    }

    public static String get(String key) {
        return messages.getString(key);
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


