package musicApp.utils.lyrics;

/**
 * Represents one entry in lyric.json
 * Example JSON block:
 * {
 *   "pathSong": "Hello-Adele-185",
 *   "pathLyricsTxt": "Hello-Adele-185.txt"
 * }
 */
public class SongLyricsEntry {

    private String pathSong;
    private String pathLyricsTxt;
    private String pathLyricsKaraoke;

    public SongLyricsEntry() {}

    public SongLyricsEntry(String pathSong, String pathLyricsTxt, String pathLyricsKaraoke) {
        this.pathSong = pathSong;
        this.pathLyricsTxt = pathLyricsTxt;
        this.pathLyricsKaraoke = pathLyricsKaraoke;
    }

    public String getPathSong() {
        return pathSong;
    }

    public void setPathSong(String pathSong) {
        this.pathSong = pathSong;
    }

    // Renommage correct et cohérent
    public String getPathLyricsTxt() {
        return pathLyricsTxt;
    }

    public void setPathLyricsTxt(String pathLyricsTxt) {
        this.pathLyricsTxt = pathLyricsTxt;
    }

    public String getPathLyricsKaraoke() {
        return pathLyricsKaraoke;
    }

    public void setPathLyricsKaraoke(String pathLyricsKaraoke) {
        this.pathLyricsKaraoke = pathLyricsKaraoke;
    }
}
