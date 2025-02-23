package MusicApp.Models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.util.Duration;

/**
 * PlayingSong
 * Class that represents a song that is currently playing.
 */
public class PlayingSong extends Song {
    private final DoubleProperty progress = new SimpleDoubleProperty(0.0);

    /**
     * Constructor
     * @param songName The name of the song.
     * @param artistName The name of the artist.
     * @param style The style of the song.
     * @param coverPath The path to the cover image.
     * @param duration The duration of the song.
     */
    public PlayingSong(String songName, String artistName, String style, String coverPath, Duration duration) {
        super(songName, artistName, style, coverPath, duration);
        this.progress.set(0.0);
    }

    /**
     * Get the progress of the song.
     * @return The progress of the song.
     */
    public double getProgress() { return progress.get(); }

    /**
     * Get the progress property.
     * @return The progress property.
     */
    public DoubleProperty progressProperty() { return progress; }

    /**
     * Set the progress of the song.
     * @param value The progress of the song.
     */
    public void setProgress(double value) { progress.set(value); }
}