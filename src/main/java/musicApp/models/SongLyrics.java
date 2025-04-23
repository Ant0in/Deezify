package musicApp.models;

import java.util.Collections;
import java.util.List;

/**
 * Data Transfer Object for song lyrics.
 * Contains the actual lyrics content, not just paths.
 */
public class SongLyrics {
    
    private final List<String> textLyrics;
    private final List<KaraokeLine> karaokeLyrics;
    
    /**
     * Default constructor
     */
    public SongLyrics() {
        textLyrics = Collections.emptyList();
        karaokeLyrics = Collections.emptyList();
    }
    
    /**
     * Constructor with lyrics content
     */
    public SongLyrics(List<String> _textLyrics, List<KaraokeLine> _karaokeLyrics) {
        textLyrics = _textLyrics != null ? _textLyrics : Collections.emptyList();
        karaokeLyrics = _karaokeLyrics != null ? _karaokeLyrics : Collections.emptyList();
    }
    
    /**
     * Get the plain text lyrics
     */
    public List<String> getLyrics() {
        return textLyrics;
    }
    
    /**
     * Get the karaoke lyrics
     */
    public List<KaraokeLine> getKaraokeLines() {
        return karaokeLyrics;
    }

    /**
     * Check if this entry has text lyrics
     */
    public boolean hasTextLyrics() {
        return textLyrics != null && !textLyrics.isEmpty();
    }
}
