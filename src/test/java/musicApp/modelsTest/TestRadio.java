package musicApp.modelsTest;

import musicApp.exceptions.BadM3URadioException;
import musicApp.models.Radio;
import org.junit.Test;

import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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

}
