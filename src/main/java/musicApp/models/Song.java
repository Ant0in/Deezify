package musicApp.models;

import javafx.scene.image.Image;
import javafx.util.Duration;
import musicApp.exceptions.BadFileTypeException;
import musicApp.exceptions.ID3TagException;
import musicApp.utils.MetadataReader;

import java.io.ByteArrayInputStream;
import java.nio.file.Path;
import java.util.Objects;

public class Song {

    private final Path filePath;
    private String title;
    private String artist;
    private String genre;
    private byte[] cover;
    private Duration duration;
    private String lyrics;

    /**
     * Constructor from MetadataReader.
     *
     * @param filePath The path to the music file.
     */
    public Song(Path filePath) {
        this.filePath = filePath;
        MetadataReader metadataReader = new MetadataReader();
        try {
            Metadata metadata = metadataReader.getMetadata(filePath.toFile());
            this.loadMetadata(metadata);
        } catch (ID3TagException | BadFileTypeException e) {
            Metadata defaultMetadata = new Metadata();
            this.loadMetadata(defaultMetadata);
        }
    }

    /**
     * Loads Metadata from MetadataReader.
     *
     * @param metadata The metadata of the song.
     */
    private void loadMetadata(Metadata metadata) {
        this.title = metadata.getTitle();
        this.artist = metadata.getArtist();
        this.genre = metadata.getGenre();
        this.cover = metadata.getCover();
        this.duration = metadata.getDuration();
        this.lyrics = metadata.getLyrics();
    }

    /**
     * Get song file path
     *
     * @return The Path to the song file.
     */
    public Path getFilePath() {
        return filePath;
    }

    /**
     * Get the name of the song.
     *
     * @return The name of the song.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the name of the song.
     *
     * @param title The name of the song.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get the name of the artist.
     *
     * @return The name of the artist.
     */
    public String getArtist() {
        return artist;
    }

    /**
     * Set the name of the artist.
     *
     * @param artist The name of the artist.
     */
    public void setArtist(String artist) {
        this.artist = artist;
    }

    /**
     * Get the genre of the song.
     *
     * @return The genre of the song.
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Set the genre of the song.
     *
     * @param genre The genre of the song.
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * Get the path to the cover image.
     *
     * @return The path to the cover image.
     */
    public byte[] getCover() {
        return cover;
    }

    /**
     * Set the path to the cover image.
     *
     * @param cover the cover in base64.
     */
    public void setCover(byte[] cover) {
        this.cover = cover;
    }

    /**
     * Get the lyrics of the song.
     *
     * @return The lyrics of the song.
     */
    public String getLyrics() {
        return lyrics;
    }
    
    /**
     * Set the lyrics of the song.
     *
     * @return The lyrics of the song.
     */
    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }


    /**
     * Get the cover as a JavaFX Image.
     *
     * @return The cover image.
     */
    public Image getCoverImage() {
        if (this.cover == null) {
            return new Image(Objects.requireNonNull(getClass().getResource("/images/song.png")).toExternalForm());
        }
        try {
            return new Image(new ByteArrayInputStream(cover));
        } catch (Exception e) {
            return new Image(Objects.requireNonNull(getClass().getResource("/images/song.png")).toExternalForm());
        }
    }

    /**
     * Get the duration of the song.
     *
     * @return The duration of the song.
     */
    public Duration getDuration() {
        return duration;
    }

    /**
     * Set the duration of the song.
     *
     * @param duration The duration of the song.
     */
    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    /**
     * Get the string representation of the song.
     *
     * @return The string representation of the song.
     */
    @Override
    public String toString() {
        return title + " - " + artist;
    }

    /**
     * Check if two songs are equal by comparing their file paths.
     *
     * @param obj The object to compare.
     * @return True if the songs are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Song song) {
            return filePath.equals(song.filePath);
        }
        return false;
    }

    /**
     * Check if the song contains the given text.
     *
     * @param text The text to search for.
     * @return True if the song contains the text, false otherwise.
     */
    public Boolean containsText(String text) {
        String lowerText = text.toLowerCase();
        return this.title.toLowerCase().contains(lowerText)
                || this.artist.toLowerCase().contains(lowerText)
                || this.genre.toLowerCase().contains(lowerText);
    }

}

