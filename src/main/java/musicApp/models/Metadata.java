package MusicApp.Models;

import javafx.util.Duration;

import java.util.HashMap;
import java.util.Optional;

/**
 * Represents metadata associated with a song.
 * Provides methods to retrieve details such as title, artist, genre, duration,
 * and cover image in Base64 format.
 */
public class Metadata {
    private final HashMap<String, String> _metadata;

    public Metadata(HashMap<String, String> metadata) {
        _metadata = metadata;
    }

    private String getMetadataOrDefault(String key, String defaultValue) {
        String value = _metadata.getOrDefault(key, defaultValue);
        return (value == null || value.isEmpty()) ? defaultValue : value;
    }

    /**
     * Retrieves the title of the song.
     * If the title is not present in the metadata, returns "Unknown Title".
     *
     * @return The title of the song.
     */
    public String getTitle() {
        return getMetadataOrDefault("title", "Unknown Title");
    }

    /**
     * Retrieves the artist of the song.
     * If the artist is not present in the metadata, returns "Unknown Artist".
     *
     * @return The artist of the song.
     */
    public String getArtist() {
        return getMetadataOrDefault("artist", "Unknown Artist");
    }

    /**
     * Retrieves the genre of the song.
     * If the genre is not present in the metadata, returns "Unknown Genre".
     *
     * @return The genre of the song.
     */
    public String getGenre() {
        return getMetadataOrDefault("genre", "Unknown Genre");
    }

    /**
     * Retrieves the duration of the song.
     * If the duration is not available, returns a duration of 0 seconds.
     *
     * @return The duration of the media item as a Duration object.
     */
    public Duration getDuration() {
        return _metadata.get("duration") != null ? Duration.seconds(Double.parseDouble(_metadata.get("duration"))) : Duration.seconds(0);
    }

    /**
     * Retrieves the cover image in Base64 encoding, if present.
     * If the cover is not available, returns an empty Optional.
     *
     * @return An Optional containing the cover image in Base64 format or empty if not available.
     */
    public Optional<String> getCoverBytesBase64() {
        String cover = _metadata.get("cover");
        return Optional.ofNullable(cover);
    }
}
