package musicApp.models;

import javafx.scene.image.Image;
import javafx.util.Duration;

import java.io.ByteArrayInputStream;
import java.nio.file.Path;

public class Song {

    private final Path filePath;
    private String songName;
    private String artistName;
    private String genre;
    private byte[] cover;
    private Duration duration;

    /**
     * Constructor from MetadataReader.
     *
     * @param metadata The metadata of the song.
     * @param filePath The path to the music file.
     */
    public Song(Metadata metadata, Path filePath) {
        this.songName = metadata.getTitle();
        this.artistName = metadata.getArtist();
        this.genre = metadata.getGenre();
        this.cover = metadata.getCover();
        this.filePath = filePath;
        this.duration = metadata.getDuration();
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
    public String getSongName() {
        return songName;
    }

    /**
     * Set the name of the song.
     *
     * @param songName The name of the song.
     */
    public void setSongName(String songName) {
        this.songName = songName;
    }

    /**
     * Get the name of the artist.
     *
     * @return The name of the artist.
     */
    public String getArtistName() {
        return artistName;
    }

    /**
     * Set the name of the artist.
     *
     * @param artistName The name of the artist.
     */
    public void setArtistName(String artistName) {
        this.artistName = artistName;
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
     * Get the cover as a JavaFX Image.
     *
     * @return The cover image.
     */
    public Image getCoverImage() {
        if (this.cover == null) {
            return new Image(getClass().getResource("/images/song.png").toExternalForm());
        }

        try {
            return new Image(new ByteArrayInputStream(cover));
        } catch (Exception e) {
            return new Image(getClass().getResource("/images/song.png").toExternalForm());
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
        return songName + " - " + artistName;
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
        return this.songName.toLowerCase().contains(text.toLowerCase())
                || this.artistName.toLowerCase().contains(text.toLowerCase())
                || this.genre.toLowerCase().contains(text.toLowerCase());
    }

}

