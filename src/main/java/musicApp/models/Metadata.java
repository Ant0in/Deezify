package musicApp.models;

import javafx.util.Duration;
import musicApp.services.LanguageService;
import org.jaudiotagger.tag.images.Artwork;
import org.jaudiotagger.tag.images.ArtworkFactory;

import java.io.File;
import java.io.IOException;
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
    private Artwork cover;
    private ArrayList<String> userTags;

    /**
     * Constructor to create a new Metadata object with default values.
     */
    public Metadata() {
        LanguageService languageService = LanguageService.getInstance();
        title = languageService.get("metadata.title");
        artist = languageService.get("metadata.artist");
        genre = languageService.get("metadata.genre");
        duration = Duration.ZERO;
        userTags = new ArrayList<>();
    }

    /**
     * Retrieves the title of the song.
     * If the title is not present in the metadata, returns "Unknown Title".
     *
     * @return The title of the song.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the song.
     *
     * @param newTitle The title of the song.
     */
    public void setTitle(String newTitle) {
        if (newTitle != null && !newTitle.isEmpty()) {
            title = newTitle;
        }
    }

    /**
     * Retrieves the artist of the song.
     * If the artist is not present in the metadata, returns "Unknown Artist".
     *
     * @return The artist of the song.
     */
    public String getArtist() {
        return artist;
    }

    /**
     * Sets the artist of the song.
     *
     * @param newArtist The artist of the song.
     */
    public void setArtist(String newArtist) {
        if (newArtist != null && !newArtist.isEmpty()) {
            artist = newArtist;
        }
    }

    /**
     * Retrieves the genre of the song.
     * If the genre is not present in the metadata, returns "Unknown Genre".
     *
     * @return The genre of the song.
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Sets the genre of the song.
     *
     * @param newGenre The genre of the song.
     */
    public void setGenre(String newGenre) {
        if (newGenre != null && !newGenre.isEmpty()) {
            genre = newGenre;
        }
    }

    /**
     * Retrieves the duration of the song.
     * If the duration is not available, returns a duration of 0 seconds.
     *
     * @return The duration of the media item as a Duration object.
     */
    public Duration getDuration() {
        return duration;
    }

    /**
     * Sets the duration of the song.
     *
     * @param newDuration The duration of the song.
     */
    public void setDuration(Duration newDuration) {
        if (newDuration != null && newDuration.greaterThan(Duration.ZERO)) {
            duration = newDuration;
        }
    }

    /**
     * Retrieves the cover image.
     *
     * @return The cover image in byte array format or empty if not available.
     */
    public Artwork getCover() {
        return cover;
    }

    /**
     * Sets the cover image
     *
     * @param newCover The cover image.
     */
    public void setCover(Artwork newCover) {
        cover = newCover;
    }

    /**
     * Sets the cover image from a byte array.
     * 
     * @param bytes The byte array representing the cover image.
     */
    public void setCoverFromBytes(byte[] bytes) {
        Artwork artwork = ArtworkFactory.getNew();
        artwork.setBinaryData(bytes);
        cover = artwork;
    }

    /**
     * Loads the cover image from a file path.
     *
     * @param path The path to the image file.
     * @throws IOException If an error occurs while loading the image.
     */
    public void loadCoverFromPath(String path) throws IOException {
        if (path != null && !path.isEmpty()) {
            Artwork artwork = ArtworkFactory.createArtworkFromFile(new File(path));
            setCover(artwork);
        }
    }

    /**
     * Get the user tags.
     * 
     * @return The user tags associated with the song.
     */
    public ArrayList<String> getUserTags() {
        return userTags;
    }

    /**
     * Sets the user tags.
     * @param newUserTags The new user tags to set.
     */
    public void setUserTags(ArrayList<String> newUserTags) {
        if (newUserTags != null) {
            userTags = newUserTags;
        }
    }

    /**
     * Checks if the metadata contains the specified text in any of its fields.
     *
     * @param text The text to search for.
     * @return True if the text is found in any field, false otherwise.
     */
    public Boolean containsText(String text) {
        String lowerText = text.toLowerCase();
        return getTitle().toLowerCase().contains(lowerText)
                || getArtist().toLowerCase().contains(lowerText)
                || getGenre().toLowerCase().contains(lowerText)
                || getUserTags().stream().anyMatch(tag -> tag.toLowerCase().contains(lowerText));
    }

}
