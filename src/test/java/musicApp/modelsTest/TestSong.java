package musicApp.modelsTest;

import musicApp.models.Metadata;
import musicApp.models.Song;
import musicApp.models.Metadata;
import org.junit.Test;

import java.nio.file.Paths;
import java.time.Duration;

import static org.junit.Assert.*;

public class TestSong {
    @Test
    public void testContainsText() {
        Metadata metadata = new Metadata();
        metadata.setTitle("songName");
        metadata.setArtist("artistName");
        metadata.setGenre("genre");
        Song song = new Song(metadata, Paths.get("src", "test", "resources", "goodTestMP3.mp3"));
        assertTrue(song.containsText("s"));
        assertTrue(song.containsText("song"));
        assertTrue(song.containsText("songName"));
        assertTrue(song.containsText("artist"));
        assertTrue(song.containsText("artistName"));
        assertTrue(song.containsText("genre"));
        assertTrue(song.containsText("ngn"));
    }
}
