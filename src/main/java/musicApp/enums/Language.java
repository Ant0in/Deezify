package musicApp.enums;


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

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Language fromCode(String code) {
        for (Language lang : values()) {
            if (lang.getCode().equalsIgnoreCase(code)) {
                return lang;
            }
        }
        return ENGLISH;
    }

    public static Language fromDisplayName(String displayName) {
        for (Language lang : values()) {
            if (lang.getDisplayName().equalsIgnoreCase(displayName)) {
                return lang;
            }
        }
        return ENGLISH;
    }
}
