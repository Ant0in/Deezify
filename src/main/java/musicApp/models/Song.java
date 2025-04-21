package musicApp.models;

import com.google.gson.annotations.Expose;
import javafx.scene.image.Image;
import javafx.util.Duration;
import musicApp.exceptions.BadSongException;
import musicApp.exceptions.LyricsNotFoundException;
import musicApp.services.LyricsService;
import musicApp.services.MetadataService;

import java.io.ByteArrayInputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Song {
    
    @Expose
    private final Path filePath;
    private Metadata metadata;
    private Optional<SongLyrics> lyricsEntry;

    /**
     * Constructor from MetadataReader.
     *
     * @param _filePath The path to the music file.
     */
    public Song(Path _filePath) {
        filePath = _filePath;
        MetadataService metadataReader = new MetadataService();
        try {
            metadata = metadataReader.getMetadata(filePath.toFile());
        } catch (Exception e) {
            metadata = new Metadata(); // Default metadata on error
        }
        refreshLyrics();
    }

    /**
     * Get the path to the song.
     *
     * @return The path to the song.
     */
    public Path getFilePath() {
        return filePath;
    }

    public Boolean isSong() {
        return true;
    }

    /**
     * Get the path to the song as a string.
     * @return The path to the song as a string.
     */
    public String getSource() throws BadSongException {
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
     * Get the album of the song.
     *
     * @return The album of the song.
     */
    public String getAlbum() {
        return metadata.getAlbum();
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
     * Get the cover image of the song.
     * @return The cover image of the song in bytes.
     */
    public byte[] getCover() {
        return metadata.getCover() != null ? metadata.getCover().getBinaryData() : null;
    }

    /**
     * Get the user tags of the song.
     * @return The user tags of the song.
     */
    public ArrayList<String> getUserTags() {
        return metadata.getUserTags();
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
     * Returns the current lyrics entry without loading it.
     * May return null if lyrics haven't been loaded yet.
     */
    public Optional<SongLyrics> getLyricsEntry() {
        return lyricsEntry;
    }

    /**
     * Set the lyrics entry for this song.
     * @param entry The lyrics entry.
     */
    public void setLyricsEntry(SongLyrics entry) {
        lyricsEntry = Optional.of(entry);
    }

    /**
     * Get the plain text lyrics for this song.
     * @return The lyrics of the song.
     */
    public List<String> getLyrics() {
        return lyricsEntry.map(SongLyrics::getLyrics)
                .orElseGet(ArrayList::new);
    }
    
    /**
     * Get the karaoke lines for this song.
     * @return List of KaraokeLine objects or empty list if no karaoke lyrics available.
     */
    public List<KaraokeLine> getKaraokeLines() throws LyricsNotFoundException {
        return lyricsEntry.orElseThrow(() -> new LyricsNotFoundException("No Lyrics found")).getKaraokeLines();
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
     * Search for text in the lyrics.
     * @param text The text to search for.
     * @return True if the text is found in the lyrics, false otherwise.
     */
    private boolean searchLyrics(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        String lowerText = text.toLowerCase();
        return getLyrics().stream()
            .anyMatch(line -> line.toLowerCase().contains(lowerText));
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

    /**
     * Refresh the lyrics for this song.
     * This will load the lyrics from the file system.
     */
    public void refreshLyrics() {
        LyricsService lyricsService = new LyricsService();
        try {
            lyricsEntry = Optional.of(lyricsService.getLyricsEntry(this));
        } catch (Exception e) {
            lyricsEntry = Optional.of(new SongLyrics());
        }
    }

    /**
     * Reload metadata for the song.
     */
    public void reloadMetadata() {
        MetadataService metadataReader = new MetadataService();
        try {
            metadata = metadataReader.getMetadata(filePath.toFile());
        } catch (Exception e) {
            metadata = new Metadata(); // Default metadata on error
        }
    }

}
