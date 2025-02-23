package MusicApp.Models;

import javafx.util.Duration;

public class Song {
    private String songName;
    private String artistName;
    private String style;
    private String coverPath;
    private Duration duration;
    private String imagePath;

    /**
     * Constructor
     * @param songName The name of the song.
     * @param artistName The name of the artist.
     * @param style The style of the song.
     * @param coverPath The path to the cover image.
     * @param duration The duration of the song.
     */
    public Song(String songName, String artistName, String style, String coverPath, Duration duration) {
        this.songName = songName;
        this.artistName = artistName;
        this.style = style;
        this.coverPath = coverPath;
        this.duration = duration;
    }

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
    public String getCoverPath() { return coverPath; }

    /**
     * Get the duration of the song.
     * @return The duration of the song.
     */
    public Duration getDuration() { return duration; }

    /**
     * Get the path to the image.
     * @return The path to the image.
     */
    public String getImagePath() { return imagePath; }
}

