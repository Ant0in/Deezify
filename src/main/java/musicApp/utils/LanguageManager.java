package musicApp.utils;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import musicApp.enums.Language;

import java.util.EnumSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.prefs.Preferences;

/**
 * LanguageManager
 * Manages application language settings and retrieves localized messages.
 * The class is a singleton and uses the Preferences API to store the selected language.
 */
public class LanguageManager {
    private static final Set<Language> SUPPORTED_LANGUAGES = EnumSet.of(Language.ENGLISH, Language.FRENCH, Language.DUTCH);
    private static LanguageManager instance;
    private final Preferences prefs = Preferences.userNodeForPackage(LanguageManager.class);
    private final Language DEFAULT_LANGUAGE = Language.ENGLISH; // default language if pc language is not supported
    private final String[] BUNDLE_NAMES = {
            "lang.messages",
            "lang.general",
            "lang.errors",
            "lang.buttons",
            "lang.settings",
            "lang.default_values",
            "lang.create_playlist"
    }; // bundle names
    private final ResourceBundle[] bundles = new ResourceBundle[BUNDLE_NAMES.length];
    private final StringProperty languageProperty = new SimpleStringProperty();


    public LanguageManager() {
        String savedLanguageString = prefs.get("language", Locale.getDefault().getLanguage());
        Language savedLanguage = Language.fromCode(savedLanguageString);
        setLanguage(savedLanguage);
    }

    public LanguageManager(Language language) {
        setLanguage(language);
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
            language = DEFAULT_LANGUAGE; // set default language if not supported
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
        return SUPPORTED_LANGUAGES.contains(language);
    }

    public StringProperty languageProperty() {return languageProperty;}
}


