package MusicApp.Models;

import javafx.util.Duration;

public class Song {
    private String songName;
    private String artistName;
    private String style;
    private String coverPath;
    private Duration duration;

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
     * Set the name of the song.
     * @param songName The name of the song.
     */
    public void setSongName(String songName) { this.songName = songName; }

    /**
     * Set the name of the artist.
     * @param artistName The name of the artist.
     */
    public void setArtistName(String artistName) { this.artistName = artistName; }

    /**
     * Set the style of the song.
     * @param style The style of the song.
     */
    public void setStyle(String style) { this.style = style; }

    /**
     * Set the path to the cover image.
     * @param coverPath The path to the cover image.
     */
    public void setCoverPath(String coverPath) { this.coverPath = coverPath; }

    /**
     * Set the duration of the song.
     * @param duration The duration of the song.
     */
    public void setDuration(Duration duration) { this.duration = duration; }

    /**
     * Get the string representation of the song.
     * @return The string representation of the song.
     */
    @Override
    public String toString() {
        return songName + " - " + artistName;
    }

    /**
     * Check if two songs are equal.
     * @param obj The object to compare.
     * @return True if the songs are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Song) {
            Song song = (Song) obj;
            return songName.equals(song.getSongName()) && artistName.equals(song.getArtistName());
        }
        return false;
    }


}

