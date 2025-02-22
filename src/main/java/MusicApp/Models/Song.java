package MusicApp.Models;

import javafx.util.Duration;

public class Song extends PlayingSong {
    private String songName;
    private String artistName;
    private String style;
    private String coverPath;
    private Duration duration;
    private String imagePath;

    public Song(String songName, String artistName, String style, String coverPath, Duration duration) {
        this.songName = songName;
        this.artistName = artistName;
        this.style = style;
        this.coverPath = coverPath;
        this.duration = duration;
    }

    public String getSongName() { return songName; }
    public String getArtistName() { return artistName; }
    public String getStyle() { return style; }
    public String getCoverPath() { return coverPath; }
    public Duration getDuration() { return duration; }
    public String getImagePath() { return imagePath; }
}

