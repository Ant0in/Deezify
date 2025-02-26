package MusicApp.ModelsTest;

import MusicApp.Models.Song;
import javafx.util.Duration;
import org.junit.Test;

import java.nio.file.Paths;

public class TestSong {
    @Test
    public void testGetSongName() {
        Song song = new Song("Song1", "Artist1", "Style1", Paths.get("src/main/resources/songs/song1.mp3"), new Duration(1000));
        assert(song.getSongName().equals("Song1"));
    }

    @Test
    public void testGetArtistName() {
        Song song = new Song("Song1", "Artist1", "Style1", Paths.get("src/main/resources/songs/song1.mp3"), new Duration(1000));
        assert(song.getArtistName().equals("Artist1"));
    }

    @Test
    public void testGetStyle() {
        Song song = new Song("Song1", "Artist1", "Style1", Paths.get("src/main/resources/songs/song1.mp3"), new Duration(1000));
        assert(song.getStyle().equals("Style1"));
    }

    @Test
    public void testGetCoverPath() {
        Song song = new Song("Song1", "Artist1", "Style1", Paths.get("src/main/resources/songs/song1.mp3"), new Duration(1000));
        assert(song.getCoverPath().equals(Paths.get("src/main/resources/images/song.png")));
    }

    @Test
    public void testGetDuration() {
        Song song = new Song("Song1", "Artist1", "Style1", Paths.get("src/main/resources/songs/song1.mp3"), new Duration(1000));
        assert(song.getDuration().equals(new Duration(1000)));
    }

    @Test
    public void testSetSongName() {
        Song song = new Song("Song1", "Artist1", "Style1", Paths.get("src/main/resources/songs/song1.mp3"), new Duration(1000));
        song.setSongName("Song2");
        assert(song.getSongName().equals("Song2"));
    }

    @Test
    public void testSetArtistName() {
        Song song = new Song("Song1", "Artist1", "Style1", Paths.get("src/main/resources/songs/song1.mp3"), new Duration(1000));
        song.setArtistName("Artist2");
        assert(song.getArtistName().equals("Artist2"));
    }

    @Test
    public void testSetStyle() {
        Song song = new Song("Song1", "Artist1", "Style1", Paths.get("src/main/resources/songs/song1.mp3"), new Duration(1000));
        song.setStyle("Style2");
        assert(song.getStyle().equals("Style2"));
    }

    @Test
    public void testSetCoverPath() {
        Song song = new Song("Song1", "Artist1", "Style1", Paths.get("src/main/resources/songs/song1.mp3"), new Duration(1000));
        song.setCoverPath(Paths.get("src/main/resources/songs/song2.mp3"));
        assert(song.getCoverPath().equals(Paths.get("src/main/resources/songs/song2.mp3")));
    }

    @Test
    public void testSetDuration() {
        Song song = new Song("Song1", "Artist1", "Style1", Paths.get("src/main/resources/songs/song1.mp3"), new Duration(1000));
        song.setDuration(new Duration(2000));
        assert(song.getDuration().equals(new Duration(2000)));
    }

    @Test
    public void testToString() {
        Song song = new Song("Song1", "Artist1", "Style1", Paths.get("src/main/resources/songs/song1.mp3"), new Duration(1000));
        assert(song.toString().equals("Song1 - Artist1"));
    }

    @Test
    public void testEquals() {
        Song song1 = new Song("Song1", "Artist1", "Style1", Paths.get("src/main/resources/songs/song1.mp3"), new Duration(1000));
        Song song2 = new Song("Song1", "Artist1", "Style1", Paths.get("src/main/resources/songs/song1.mp3"), new Duration(1000));
        assert(song1.equals(song2));
    }
}
