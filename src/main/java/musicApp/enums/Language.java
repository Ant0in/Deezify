package musicApp.enums;


/**
 * The Language enumeration.
 * Contains the different languages the application can be displayed in.
 */
public enum Language {
    ENGLISH("en", "English"),
    FRENCH("fr", "Français"),
    DUTCH("nl", "Nederlands");

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
        return ENGLISH;
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
        return ENGLISH;
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
