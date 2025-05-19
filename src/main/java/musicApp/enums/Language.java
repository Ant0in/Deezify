package musicApp.enums;


import java.util.EnumSet;
import java.util.Set;

/**
 * The Language enumeration.
 * Contains the different languages the application can be displayed in.
 */
public enum Language {
    ENGLISH("en", "English"),
    FRENCH("fr", "Français"),
    DUTCH("nl", "Nederlands");

    // Default language
    public static final Language DEFAULT = Language.ENGLISH;

    private final String code;
    private final String displayName;


    Language(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    /**
     * Gets the language from the code.
     *
     * @param code the code
     * @return the language
     */
    public static Language fromCode(String code) {
        for (Language lang : values()) {
            if (lang.getCode().equalsIgnoreCase(code)) {
                return lang;
            }
        }
        return DEFAULT;
    }

    /**
     * Gets the language from the display name.
     *
     * @param displayName the display name
     * @return the language
     */
    public static Language fromDisplayName(String displayName) {
        for (Language lang : values()) {
            if (lang.getDisplayName().equalsIgnoreCase(displayName)) {
                return lang;
            }
        }
        return DEFAULT;
    }

    /**
     * Returns a set of all the supported Languages.
     *
     * @return A set containing all the Languages enum values.
     */
    public static Set<Language> getSupportedLanguages() {
        return EnumSet.allOf(Language.class); // Dynamically gets all enum values
    }

    /**
     * Gets the code of the language (ISO 639-1).
     *
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * Gets the display name of the language (in the language itself).
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }
}
