package musicApp.modelsTest;

import javafx.scene.media.AudioSpectrumListener;
import musicApp.controllers.MiniPlayerController;
import musicApp.exceptions.BadM3URadioException;
import musicApp.exceptions.BadSongException;
import musicApp.models.AudioPlayer;
import musicApp.models.Radio;
import org.junit.Test;

import java.nio.file.Paths;

import static org.junit.Assert.*;

public class TestRadio {

    @Test
    public void testRadioCreationAndUrlParsing() {
        Radio radio;
        try {
            radio = new Radio(Paths.get("src", "test", "resources", "sampleRadio.m3u"));
            assertFalse(radio.isSong());
            String expectedUrl = "http://example.com/stream"; 
            assertEquals(expectedUrl, radio.getSource());
        } catch (BadM3URadioException e) {
            throw new RuntimeException("Failed to create Radio object", e);
        }        
        
    }

    @Test
    public void testSetWebUrl() {
        Radio radio;
        try {
            radio = new Radio(Paths.get("src", "test", "resources", "sampleRadio.m3u"));
            radio.setWebUrl("http://new-url.com/stream");
            assertEquals("http://new-url.com/stream", radio.getSource());
        } catch (BadM3URadioException e) {
            throw new RuntimeException("Failed to create Radio object", e);
        }        
    }

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
