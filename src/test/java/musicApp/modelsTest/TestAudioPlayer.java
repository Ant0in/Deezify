package musicApp.modelsTest;

import musicApp.exceptions.BadM3URadioException;
import musicApp.exceptions.BadSongException;
import musicApp.models.AudioPlayer;
import musicApp.models.Radio;
import musicApp.models.Song;
import org.junit.Test;

import java.nio.file.Paths;

import static org.junit.Assert.*;

public class TestAudioPlayer {
    @Test
    public void testBadM3URadioException() {
        try {
            AudioPlayer audioPlayer = new AudioPlayer(null);
            Radio radio = new Radio(Paths.get("src", "test", "resources", "badRadio.m3u"));
            audioPlayer.loadSong(radio);
            fail("Expected BadM3URadioException to be thrown");
        } catch (BadSongException e) {
            assertTrue(e instanceof BadM3URadioException);
        }
    }
}
