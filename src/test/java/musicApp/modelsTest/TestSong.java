package musicApp.modelsTest;

import javafx.util.Duration;
import musicApp.models.Metadata;
import musicApp.models.Song;
import musicApp.models.Metadata;
import musicApp.utils.LanguageManager;
import org.junit.Test;

import java.lang.reflect.Array;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.*;

public class TestSong {
    @Test
    public void testSong() {
        // Test that the song is created with the correct metadata
        Song song = new Song(Paths.get("src", "test", "resources", "goodTestWAV.wav"));
        assertEquals("3seconds", song.getTitle());
        assertEquals("Sample", song.getArtist());
        assertEquals("Celtic", song.getGenre());
        assertNull(song.getCover());
        assertEquals(javafx.util.Duration.seconds(Double.parseDouble("3")), song.getDuration());

        // Test default metadata when the song has no ID3 tag
        Song song2 = new Song(Paths.get("src", "test", "resources", "noID3TagMP3.mp3"));
        assertEquals(LanguageManager.getInstance().get("metadata.title"), song2.getTitle());
        assertEquals(LanguageManager.getInstance().get("metadata.artist"), song2.getArtist());
        assertEquals(LanguageManager.getInstance().get("metadata.genre"), song2.getGenre());
        assertNull(song2.getCover());
        assertEquals(Duration.ZERO, song2.getDuration());
    }

    @Test
    public void testContainsText() {
        Song song = new Song(Paths.get("src", "test", "resources", "goodTestMP3.mp3"));
        song.setTitle("songName");
        song.setArtist("artistName");
        song.setGenre("genre");
        String[] trueAssert = {"s", "song", "songName", "artist", "artistName", "genre", "ngn", ""};
        for (String s : trueAssert) {
            assertTrue(song.containsText(s));
        }
        String[] falseAssert = {"aaa", "cover", "134"};
        for (String s : falseAssert) {
            assertFalse(song.containsText(s));
        }
    }

    @Test
    public void testDifferent(){
        // Test that song are different if they have the same title, artiste and genre but different file path
        Song song = new Song(Paths.get("src", "test", "resources", "noID3TagMP3.mp3"));
        Song song2 = new Song(Paths.get("src", "test", "resources", "noTagWAV.wav"));
        assertNotEquals(song, song2);
    }
}
