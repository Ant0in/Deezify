package musicApp.models;

import com.google.gson.annotations.Expose;
import javafx.scene.image.Image;
import javafx.util.Duration;
import musicApp.utils.DataProvider;
import musicApp.utils.lyrics.LyricsManager;
import musicApp.utils.lyrics.LyricsDataAccess;
import musicApp.utils.MetadataUtils;
import org.jaudiotagger.tag.images.Artwork;

import java.io.ByteArrayInputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Song {
    
    @Expose
    private final Path filePath;
    private Metadata metadata; 

    /**
     * Constructor from MetadataReader.
     *
     * @param _filePath The path to the music file.
     */
    public Song(Path _filePath) {
        filePath = _filePath;
        MetadataUtils metadataReader = new MetadataUtils();
        try {
            metadata = metadataReader.getMetadata(filePath.toFile());
        } catch (Exception e) {
            metadata = new Metadata(); // Default metadata on error
        }
    }

    /**
     * Returns if the song is a radio or not.
     * @return boolean isSong.
     */
    public Boolean isSong() {
        return true;
    }

    /**
     * Reload metadata for the song.
     */
    public void reloadMetadata() {
        MetadataUtils metadataReader = new MetadataUtils();
        try {
            metadata = metadataReader.getMetadata(filePath.toFile());
        } catch (Exception e) {
            metadata = new Metadata(); // Default metadata on error
        }
    }
    
    /**
     * Get the path to the song.
     *
     * @return The path to the song.
     */
    public Path getFilePath() {
        return filePath;
    }

    /**
     * Get the path to the song as a string.
     * @return The path to the song as a string.
     */
    public String getSource() {
        return filePath.toUri().toString();
    }

    /**
     * Get the title of the song.
     *
     * @return The title of the song.
     */
    public String getTitle() {
        return metadata.getTitle();
    }

    /**
     * Set the title of the song.
     *
     * @param title The title of the song.
     */
    public void setTitle(String title) {
        metadata.setTitle(title);
    }

    /**
     * Get the artist of the song.
     *
     * @return The artist of the song.
     */
    public String getArtist() {
        return metadata.getArtist();
    }

    /**
     * Set the artist of the song.
     *
     * @param artist The artist of the song.
     */
    public void setArtist(String artist) {
        metadata.setArtist(artist);
    }

    /**
     * Get the genre of the song.
     * @return The genre of the song.
     */
    public String getGenre() {
        return metadata.getGenre();
    }

    /**
     * Set the genre of the song.
     *
     * @param genre The genre of the song.
     */
    public void setGenre(String genre) {
        metadata.setGenre(genre);
    }

    /**
     * Get the duration of the song.
     * @return The duration of the song.
     */
    public Duration getDuration() {
        return metadata.getDuration();
    }

    /**
     * Set the duration of the song.
     *
     * @param duration The duration of the song.
     */
    public void setDuration(Duration duration) {
        metadata.setDuration(duration);
    }

    /**
     * Get the cover image of the song.
     * @return The cover image of the song in bytes.
     */
    public byte[] getCover() {
        return metadata.getCover() != null ? metadata.getCover().getBinaryData() : null;
    }

    /**
     * Set the cover of the song.
     *
     * @param artwork The cover of the song.
     */
    public void setCover(Artwork artwork) {
        metadata.setCover(artwork);
    }

    /**
     * Get the user tags of the song.
     * @return The user tags of the song.
     */
    public ArrayList<String> getUserTags() {
        return metadata.getUserTags();
    }

    /**
     * Set the user tags of the song.
     *
     * @param userTags The user tags of the song.
     */
    public void setUserTags(ArrayList<String> userTags) {
        metadata.setUserTags(userTags);
    }

    /**
     * Get the Cover image of the song.
     * @return The cover image of the song.
     */
    public Image getCoverImage() {
        String defaultCover = getClass().getResource("/images/song.png").toExternalForm();
        if (getCover() == null) {
            return new Image(Objects.requireNonNull(defaultCover));
        }
        try {
            return new Image(new ByteArrayInputStream(getCover()));
        } catch (Exception e) {
            return new Image(Objects.requireNonNull(defaultCover));
        }
    }

    /**
     * Get the metadata of the song.
     * @return The metadata of the song.
     */
    public Metadata getMetadata() {
        return metadata;
    }

    /**
     * Get the title and artist of the song.
     * @return The title and artist of the song.
     */
    @Override
    public String toString() {
        return getTitle() + " - " + getArtist();
    }

    /**
     * Checks if the song is equal to another song by comparing their file paths.
     * @param obj The object to compare with.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Song song) {
            return filePath.equals(song.filePath);
        }
        return false;
    }

    /**
     * Get the lyrics of the song.
     * @return The lyrics of the song.
     */
    public List<String> getLyrics() {
        LyricsManager lyricsManager = new LyricsManager(new LyricsDataAccess(new DataProvider()));
        return lyricsManager.getLyrics(this);
    }

    /**
     * Search for a specific text in the lyrics of the song.
     * @param text The text to search for.
     * @return True if the text is found in the lyrics, false otherwise.
     */
    private boolean searchLyrics(String text) {
        if (text.isEmpty()) {
            return false;
        }
        List<String> lyrics = getLyrics();
        return lyrics.stream().anyMatch(line -> line.toLowerCase().contains(text));
    }

    /**
     * Check if the song contains a specific text in its metadata or lyrics.
     * @param text The text to search for.
     * @return True if the text is found in the metadata or lyrics, false otherwise.
     */
    public Boolean containsText(String text) {
        String lowerText = text.toLowerCase();
        if (lowerText.length() > 1 && lowerText.startsWith("\"") && lowerText.endsWith("\"")) {
            lowerText = lowerText.substring(1, lowerText.length() - 1);
            return searchLyrics(lowerText);
        } else {
            return metadata.containsText(lowerText);
        }
    }

}
