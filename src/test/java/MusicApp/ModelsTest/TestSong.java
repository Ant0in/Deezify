package MusicApp.ModelsTest;

import MusicApp.Models.Song;
import javafx.util.Duration;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

public class TestSong {
    @Test
    public void testGetSongName() {
        Song song = new Song("Song1", "Artist1", "Style1", "Cover1", Paths.get("file1.mp3"), Duration.millis(1000));
        assert (song.getSongName().equals("Song1"));
    }

    @Test
    public void testGetArtistName() {
        Song song = new Song("Song1", "Artist1", "Style1", "Cover1", Paths.get("file1.mp3"), Duration.millis(1000));
        assert (song.getArtistName().equals("Artist1"));
    }

    @Test
    public void testGetStyle() {
        Song song = new Song("Song1", "Artist1", "Style1", "Cover1", Paths.get("file1.mp3"), Duration.millis(1000));
        assert (song.getStyle().equals("Style1"));
    }

    @Test
    public void testGetCoverPath() {
        Song song = new Song("Song1", "Artist1", "Style1", "Cover1", Paths.get("file1.mp3"), Duration.millis(1000));
        assert (song.getCoverPath().equals("Cover1"));
    }

    @Test
    public void testGetDuration() {
        Song song = new Song("Song1", "Artist1", "Style1", "Cover1", Paths.get("file1.mp3"), Duration.millis(1000));
        assert (song.getDuration().equals(Duration.millis(1000)));
    }

    @Test
    public void testSetSongName() {
        Song song = new Song("Song1", "Artist1", "Style1", "Cover1", Paths.get("file1.mp3"), Duration.millis(1000));
        song.setSongName("Song2");
        assert (song.getSongName().equals("Song2"));
    }

    @Test
    public void testSetArtistName() {
        Song song = new Song("Song1", "Artist1", "Style1", "Cover1", Paths.get("file1.mp3"), Duration.millis(1000));
        song.setArtistName("Artist2");
        assert (song.getArtistName().equals("Artist2"));
    }

    @Test
    public void testSetStyle() {
        Song song = new Song("Song1", "Artist1", "Style1", "Cover1", Paths.get("file1.mp3"), Duration.millis(1000));
        song.setStyle("Style2");
        assert (song.getStyle().equals("Style2"));
    }

    @Test
    public void testSetCoverPath() {
        Song song = new Song("Song1", "Artist1", "Style1", "Cover1", Paths.get("file1.mp3"), Duration.millis(1000));
        song.setCoverPath("Cover2");
        assert (song.getCoverPath().equals("Cover2"));
    }

    @Test
    public void testSetDuration() {
        Song song = new Song("Song1", "Artist1", "Style1", "Cover1", Paths.get("file1.mp3"), Duration.millis(1000));
        song.setDuration(Duration.millis(2000));
        assert (song.getDuration().equals(Duration.millis(2000)));
    }

    @Test
    public void testToString() {
        Song song = new Song("Song1", "Artist1", "Style1", "Cover1", Paths.get("file1.mp3"), Duration.millis(1000));
        assert (song.toString().equals("Song1 - Artist1"));
    }

    @Test
    public void testEquals() {
        Song song1 = new Song("Song1", "Artist1", "Style1", "Cover1", Paths.get("file1.mp3"), Duration.millis(1000));
        Song song2 = new Song("Song1", "Artist1", "Style1", "Cover1", Paths.get("file1.mp3"), Duration.millis(1000));
        assert (song1.equals(song2));
    }
}

