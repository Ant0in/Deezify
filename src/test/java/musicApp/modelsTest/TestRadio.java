package musicApp.modelsTest;

import javafx.scene.image.Image;
import musicApp.models.Radio;
import org.junit.Test;

import java.nio.file.Paths;

import static org.junit.Assert.*;

public class TestRadio {

    @Test
    public void testRadioCreationAndUrlParsing() {
        Radio radio = new Radio(Paths.get("src", "test", "resources", "sampleRadio.m3u"));        
        assertFalse(radio.isSong());
        String expectedUrl = "http://example.com/stream"; 
        assertEquals(expectedUrl, radio.getFilePathString());
    }

    @Test
    public void testSetWebUrl() {
        Radio radio = new Radio(Paths.get("src", "test", "resources", "sampleRadio.m3u"));
        radio.setWebUrl("http://new-url.com/stream");
        assertEquals("http://new-url.com/stream", radio.getFilePathString());
    }
}
