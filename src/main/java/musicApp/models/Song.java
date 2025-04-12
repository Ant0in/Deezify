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
     * @param filePath The path to the music file.
     */
    public Song(Path filePath) {
        this.filePath = filePath;
        MetadataUtils metadataReader = new MetadataUtils();
        try {
            this.metadata = metadataReader.getMetadata(filePath.toFile());
        } catch (Exception e) {
            this.metadata = new Metadata(); // Default metadata on error
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
            this.metadata = metadataReader.getMetadata(filePath.toFile());
        } catch (Exception e) {
            this.metadata = new Metadata(); // Default metadata on error
        }
    }

    public Path getFilePath() {
        return filePath;
    }

    public String getSource() {
        return filePath.toUri().toString();
    }

    /**
     * Get the name of the song.
     *
     * @return The name of the song.
     */
    public String getTitle() {
        return metadata.getTitle();
    }

    public void setTitle(String title) {
        metadata.setTitle(title);
    }

    public String getArtist() {
        return metadata.getArtist();
    }

    public void setArtist(String artist) {
        metadata.setArtist(artist);
    }

    public String getGenre() {
        return metadata.getGenre();
    }

    public void setGenre(String genre) {
        metadata.setGenre(genre);
    }

    public Duration getDuration() {
        return metadata.getDuration();
    }

    public void setDuration(Duration duration) {
        metadata.setDuration(duration);
    }

    public byte[] getCover() {
        return metadata.getCover() != null ? metadata.getCover().getBinaryData() : null;
    }

    public void setCover(Artwork artwork) {
        metadata.setCover(artwork);
    }

    public ArrayList<String> getUserTags() {
        return metadata.getUserTags();
    }

    public void setUserTags(ArrayList<String> userTags) {
        metadata.setUserTags(userTags);
    }

    public Image getCoverImage() {
        if (getCover() == null) {
            return new Image(Objects.requireNonNull(getClass().getResource("/images/song.png")).toExternalForm());
        }
        try {
            return new Image(new ByteArrayInputStream(getCover()));
        } catch (Exception e) {
            return new Image(Objects.requireNonNull(getClass().getResource("/images/song.png")).toExternalForm());
        }
    }

    public Metadata getMetadata() {
        return metadata;
    }

    @Override
    public String toString() {
        return getTitle() + " - " + getArtist();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Song song) {
            return filePath.equals(song.filePath);
        }
        return false;
    }

    public List<String> getLyrics() {
        LyricsManager lyricsManager = new LyricsManager(new LyricsDataAccess(new DataProvider()));
        return lyricsManager.getLyrics(this);
    }

    private boolean searchLyrics(String text) {
        if (text.isEmpty()) {
            return false;
        }
        List<String> lyrics = getLyrics();
        return lyrics.stream().anyMatch(line -> line.toLowerCase().contains(text));
    }

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
