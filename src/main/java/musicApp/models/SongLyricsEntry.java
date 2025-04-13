package musicApp.models;

import java.util.Collections;
import java.util.List;

/**
 * Data Transfer Object for song lyrics.
 * Contains the actual lyrics content, not just paths.
 */
public class SongLyricsEntry {
    
    private List<String> textLyrics;
    private List<KaraokeLine> karaokeLyrics;
    
    /**
     * Default constructor
     */
    public SongLyricsEntry() {
        this.textLyrics = Collections.emptyList();
        this.karaokeLyrics = Collections.emptyList();
    }
    
    /**
     * Constructor with lyrics content
     */
    public SongLyricsEntry(List<String> textLyrics, List<KaraokeLine> karaokeLyrics) {
        this.textLyrics = textLyrics != null ? textLyrics : Collections.emptyList();
        this.karaokeLyrics = karaokeLyrics != null ? karaokeLyrics : Collections.emptyList();
    }
    
    /**
     * Get the plain text lyrics
     */
    public List<String> getLyrics() {
        return textLyrics;
    }
    
    /**
     * Set the plain text lyrics
     */
    public void setLyrics(List<String> lyrics) {
        this.textLyrics = lyrics != null ? lyrics : Collections.emptyList();
    }
    
    /**
     * Get the karaoke lyrics
     */
    public List<KaraokeLine> getKaraokeLines() {
        return karaokeLyrics;
    }
    
    /**
     * Set the karaoke lyrics
     */
    public void setKaraokeLines(List<KaraokeLine> karaokeLines) {
        this.karaokeLyrics = karaokeLines != null ? karaokeLines : Collections.emptyList();
    }
    
    /**
     * Check if this entry has text lyrics
     */
    public boolean hasTextLyrics() {
        return textLyrics != null && !textLyrics.isEmpty();
    }
    
    /**
     * Check if this entry has karaoke lyrics
     */
    public boolean hasKaraokeLyrics() {
        return karaokeLyrics != null && !karaokeLyrics.isEmpty();
    }
}
