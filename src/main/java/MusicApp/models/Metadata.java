package MusicApp.models;

import javafx.util.Duration;
import MusicApp.utils.LanguageManager;

import java.util.ArrayList;

/**
 * Represents metadata associated with a song.
 * Provides methods to retrieve details such as title, artist, genre, duration,
 * and cover image in Base64 format.
 */
public class Metadata {
    private String title;
    private String artist;
    private String genre;
    private Duration duration;
    private byte[] cover;
    private ArrayList<String> userTags;

    /**
     * Constructor to create a new Metadata object with default values.
     */
    public Metadata() {
        LanguageManager languageManager = LanguageManager.getInstance();
        this.title = languageManager.get("metadata.title");
        this.artist = languageManager.get("metadata.artist");
        this.genre = languageManager.get("metadata.genre");
        this.duration = Duration.ZERO;
        this.cover = null;
        this.userTags = new ArrayList<>();
    }

    /**
     * Retrieves the title of the song.
     * If the title is not present in the metadata, returns "Unknown Title".
     *
     * @return The title of the song.
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Sets the title of the song.
     *
     * @param title The title of the song.
     */
    public void setTitle(String title) {
        if (title != null && !title.isEmpty()) {
            this.title = title;
        }
    }

    /**
     * Retrieves the artist of the song.
     * If the artist is not present in the metadata, returns "Unknown Artist".
     *
     * @return The artist of the song.
     */
    public String getArtist() {
        return this.artist;
    }

    /**
     * Sets the artist of the song.
     *
     * @param artist The artist of the song.
     */
    public void setArtist(String artist) {
        if (artist != null && !artist.isEmpty()) {
            this.artist = artist;
        }
    }

    /**
     * Retrieves the genre of the song.
     * If the genre is not present in the metadata, returns "Unknown Genre".
     *
     * @return The genre of the song.
     */
    public String getGenre() {
        return this.genre;
    }

    /**
     * Sets the genre of the song.
     *
     * @param genre The genre of the song.
     */
    public void setGenre(String genre) {
        if (genre != null && !genre.isEmpty()) {
            this.genre = genre;
        }
    }

    /**
     * Retrieves the duration of the song.
     * If the duration is not available, returns a duration of 0 seconds.
     *
     * @return The duration of the media item as a Duration object.
     */
    public Duration getDuration() {
        return this.duration;
    }

    /**
     * Sets the duration of the song.
     *
     * @param duration The duration of the song.
     */
    public void setDuration(Duration duration) {
        if (duration != null && duration.greaterThan(Duration.ZERO)) {
            this.duration = duration;
        }
    }

    /**
     * Retrieves the cover image in Base64 encoding, if present.
     * If the cover is not available, returns an empty Optional.
     *
     * @return An Optional containing the cover image in Base64 format or empty if not available.
     */
    public byte[] getCover() {
        return this.cover;
    }

    /**
     * Sets the cover image in Base64 encoding.
     *
     * @param cover The cover image in Base64 format.
     */
    public void setCover(byte[] cover) {
        if (cover != null && cover.length > 0) {
            this.cover = cover;
        }
    }

    public ArrayList<String> getUserTags() {
        return this.userTags;
    }

    public void setUserTags(ArrayList<String> userTags) {
        if (userTags != null) {
            this.userTags = userTags;
        }
    }
}
