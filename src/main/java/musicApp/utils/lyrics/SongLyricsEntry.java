package musicApp.utils.lyrics;

/**
 * Represents one entry in lyric.json
 * Example JSON block:
 * {
 *   "pathSong": "Hello-Adele-185",
 *   "pathLyrics": "Hello-Adele-185.txt"
 * }
 */
public class SongLyricsEntry {
    private String pathSong;
    private String pathLyrics;

    private String txt; 
    private String lrc; 

    public SongLyricsEntry() {
    }

    public SongLyricsEntry(String pathSong, String pathLyrics) {
        this.pathSong = pathSong;
        this.pathLyrics = pathLyrics;
    }

    public String getPathSong() {
        return pathSong;
    }

    public void setPathSong(String pathSong) {
        this.pathSong = pathSong;
    }

    public String getPathLyrics() {
        return pathLyrics;
    }

    public void setPathLyrics(String pathLyrics) {
        this.pathLyrics = pathLyrics;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public String getLrc() {
        return lrc;
    }
    
    public void setLrc(String lrc) {
        this.lrc = lrc;
    }
}
