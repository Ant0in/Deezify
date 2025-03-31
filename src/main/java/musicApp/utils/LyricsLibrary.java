package musicApp.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the entire structure of the lyric.json file.
 */
public class LyricsLibrary {

    // List of songs in the JSON
    private List<SongLyricsEntry> songs;

    public LyricsLibrary() {
        this.songs = new ArrayList<>();
    }

    public List<SongLyricsEntry> getSongs() {
        return songs;
    }

}
