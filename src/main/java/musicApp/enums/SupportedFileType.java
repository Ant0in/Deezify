package musicApp.enums;


import java.util.Arrays;

/**
 * The music files extensions supported by the app.
 */
public enum SupportedFileType {
    MP3(".mp3"),
    WAV(".wav"),
    M3U(".m3u");

    private final String ext;

    SupportedFileType(String ext) {
        this.ext = ext;
    }

    /**
     * Returns the file extension associated with the file type.
     *
     * @return The file extension as a string (e.g., ".mp3", ".wav").
     */
    public String getExt() {
        return ext;
    }

    /**
     * Returns an array of file extensions formatted to be used directly in a
     * file search filter, with each extension prefixed by a wildcard (e.g., "*.mp3", "*.wav").
     *
     * @return An array of file extensions (e.g., "*.mp3", "*.wav").
     */
    public static String[] getExtensionsForFileFilter() {
        return Arrays.stream(SupportedFileType.values())
                .map(fileType -> "*" + fileType.getExt())
                .toArray(String[]::new);
    }

    /**
     * Returns an array containing all supported of file extensions as strings (e.g., ".mp3", ".wav").
     *
     * @return An array of file extensions (e.g., ".mp3", ".wav").
     */
    public static String[] getExtensions() {
        return Arrays.stream(SupportedFileType.values())
                .map(SupportedFileType::getExt)
                .toArray(String[]::new);
    }

    /**
     * Returns the corresponding {@link SupportedFileType} based on the provided file extension string.
     * Returns {@code null} if no matching file type is found.
     *
     * @param extension The file extension as a string (e.g., ".mp3", ".wav").
     * @return The corresponding {@link SupportedFileType}, or null in case of no match
     */
    public static SupportedFileType fromExtension(String extension) {
        // Loop through the enum values and check if any matches the given extension (case insensitive)
        return Arrays.stream(SupportedFileType.values())
                .filter(fileType -> fileType.getExt().equalsIgnoreCase(extension))
                .findFirst()
                .orElse(null);
    }
}
