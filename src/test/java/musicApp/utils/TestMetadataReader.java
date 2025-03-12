package musicApp.utils;

import javafx.util.Duration;
import musicApp.exceptions.BadFileTypeException;
import musicApp.exceptions.ID3TagException;
import musicApp.models.Metadata;
import org.junit.Test;

import java.io.File;
import java.nio.file.Paths;

import static org.junit.Assert.*;


public class TestMetadataReader {


    @Test
    public void testReadValidFile() {

        // check if the metadata reader can handle a mp3 file with valid metadata
        File file = Paths.get("src", "test", "resources", "goodTestMP3.mp3").toFile();
        boolean hasExceptionOccured = false;
        MetadataReader reader = new MetadataReader();

        try {
            reader.getMetadata(file);
        } catch (Exception e) {
            hasExceptionOccured = true;
        }

        assert !hasExceptionOccured;

    }

    @Test
    public void testNoTagsMP3() {

        // check if the metadata reader can handle a mp3 file with no ID3v2 tags
        File file = Paths.get("src", "test", "resources", "noID3TagMP3.mp3").toFile();
        MetadataReader reader = new MetadataReader();

        assertThrows(ID3TagException.class, () -> {
            reader.getMetadata(file);
        });

    }

    @Test
    public void testCheckMetadataMP3() {

        // check if the metadata reader can handle a mp3 file with valid metadata
        File file = Paths.get("src", "test", "resources", "goodTestMP3.mp3").toFile();
        Metadata metadata;
        MetadataReader reader = new MetadataReader();

        try {
            metadata = reader.getMetadata(file);
        } catch (ID3TagException | BadFileTypeException e) {
            e.printStackTrace();
            assert false;
            return;
        }

        // few eq checks
        assertEquals("Chinese DOIT sound effect", metadata.getTitle());
        assertEquals("ant0in", metadata.getArtist());
        assertEquals("Electro", metadata.getGenre());
        // cover is not tested smh
        assertEquals(Duration.seconds(Double.parseDouble("2")), metadata.getDuration());

    }

    @Test
    public void testCheckMetadataWAV() {

        // check if the metadata reader can handle a mp3 file with valid metadata
        File file = Paths.get("src", "test", "resources", "goodTestWAV.wav").toFile();
        Metadata metadata;
        MetadataReader reader = new MetadataReader();

        try {
            metadata = reader.getMetadata(file);
        } catch (ID3TagException e) {
            e.printStackTrace();
            assert false;
            return;
        } catch (BadFileTypeException e) {
            e.printStackTrace();
            assert false;
            return;
        }

        // few eq checks
        assertEquals("3seconds", metadata.getTitle());
        assertEquals("Sample", metadata.getArtist());
        assertEquals("Celtic", metadata.getGenre());
        assertNull(metadata.getCover());
        assertEquals(Duration.seconds(Double.parseDouble("3")), metadata.getDuration());
    }

    @Test
    public void testNoTagsWAV() {

        // check if the metadata reader can handle a mp3 file with no ID3v2 tags
        File file = Paths.get("src", "test", "resources", "noTagWAV.wav").toFile();
        MetadataReader reader = new MetadataReader();
        LanguageManager languageManager = LanguageManager.getInstance();

        try {
            System.out.println(reader.getMetadata(file));
            assertEquals(languageManager.get("metadata.title"), reader.getMetadata(file).getTitle());
            assertEquals(languageManager.get("metadata.artist"), reader.getMetadata(file).getArtist());
            assertEquals(languageManager.get("metadata.genre"), reader.getMetadata(file).getGenre());
        } catch (ID3TagException e) {
            e.printStackTrace();
            assert false;
        } catch (BadFileTypeException e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    public void testBadFileType() {

        // check if the metadata reader can handle a file with an invalid extension
        File file = Paths.get("src", "test", "resources", "badFileType.opus").toFile();
        MetadataReader reader = new MetadataReader();

        assertThrows(BadFileTypeException.class, () -> {
            reader.getMetadata(file);
        });
    }

}
