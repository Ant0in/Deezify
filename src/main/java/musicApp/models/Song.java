package musicApp.models;

import java.io.ByteArrayInputStream;
import java.nio.file.Path;
import java.util.Base64;

import javafx.scene.image.Image;
import javafx.util.Duration;

public class Song {

    private String songName;
    private String artistName;
    private String style;
    private String cover;
    private final Path filePath;
    private Duration duration;

    /**
     * Constructor
     * @param songName The name of the song.
     * @param artistName The name of the artist.
     * @param style The style of the song.
     * @param cover Base64 representation of the image.
     * @param filePath The path to the music file.
     * @param duration The duration of the song.
     */
    public Song(String songName, String artistName, String style, String cover, Path filePath, Duration duration) {
        this.songName = songName;
        this.artistName = artistName;
        this.style = style;
        this.cover = cover;
        this.filePath = filePath;
        this.duration = duration;
    }

    /**
     * Constructor using the metadata map from MetadataReader.
     * @param metadata The metadata of the song.
     * @param filePath The path to the music file.
     */
    public Song(Metadata metadata, Path filePath) {
        
        this.songName = metadata.getTitle();
        this.artistName = metadata.getArtist();
        this.style = metadata.getGenre();
        this.cover = metadata.getCoverBytesBase64().orElse(null);
        this.filePath = filePath;
        this.duration = metadata.getDuration();

    }

    /**
     * Get song file path
     * @return The Path to the song file.
     */
    public Path getFilePath() { return filePath; }
    /**
     * Get the name of the song.
     * @return The name of the song.
     */
    public String getSongName() { return songName; }

    /**
     * Get the name of the artist.
     * @return The name of the artist.
     */
    public String getArtistName() { return artistName; }

    /**
     * Get the style of the song.
     * @return The style of the song.
     */
    public String getStyle() { return style; }

    /**
     * Get the path to the cover image.
     * @return The path to the cover image.
     */
    public String getCover() { return cover; }

    /**
     * Get the cover as a JavaFX Image.
     * @return The cover image.
     */
    public Image getCoverImage() {
        if (this.cover == null) {
            return new Image(getClass().getResource("/images/song.png").toExternalForm());
        }

        try {
            byte [] imageData = Base64.getDecoder().decode(cover);
            return new Image(new ByteArrayInputStream(imageData));
        } catch (Exception e) {
            return new Image(getClass().getResource("/images/song.png").toExternalForm());
        }
    }

    /**
     * Get the duration of the song.
     * @return The duration of the song.
     */
    public Duration getDuration() { return duration; }

    /**
     * Set the name of the song.
     * @param songName The name of the song.
     */
    public void setSongName(String songName) { this.songName = songName; }

    /**
     * Set the name of the artist.
     * @param artistName The name of the artist.
     */
    public void setArtistName(String artistName) { this.artistName = artistName; }

    /**
     * Set the style of the song.
     * @param style The style of the song.
     */
    public void setStyle(String style) { this.style = style; }

    /**
     * Set the path to the cover image.
     * @param cover the cover in base64.
     */
    public void setCover(String cover) { this.cover = cover; }

    /**
     * Set the duration of the song.
     * @param duration The duration of the song.
     */
    public void setDuration(Duration duration) { this.duration = duration; }

    /**
     * Get the string representation of the song.
     * @return The string representation of the song.
     */
    @Override
    public String toString() {
        return songName + " - " + artistName;
    }

    /**
     * Check if two songs are equal by comparing their file paths.
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


    public boolean containsText(String lowerQuery){
        return songName.toLowerCase().contains(lowerQuery) ||
                artistName.toLowerCase().contains(lowerQuery) ||
                style.toLowerCase().contains(lowerQuery);
    }

}

