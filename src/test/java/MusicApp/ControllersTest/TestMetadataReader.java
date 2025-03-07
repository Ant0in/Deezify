
package MusicApp.ControllersTest;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import MusicApp.Exceptions.BadFileTypeException;
import org.junit.Test;

import MusicApp.Controllers.MetadataReader;
import MusicApp.Exceptions.ID3TagException;


public class TestMetadataReader {


    @Test
    public void testReadValidFile() {

        // check if the metadata reader can handle a mp3 file with valid metadata
        File file = Paths.get("src", "test", "resources", "goodTestMP3.mp3").toFile();
        boolean hasExceptionOccured = false;

        try {
            MetadataReader.getMetadata(file);
        } catch (Exception e) {
            hasExceptionOccured = true;
        }

        assert !hasExceptionOccured;

    }

    @Test
    public void testNoTagsMP3() {

        // check if the metadata reader can handle a mp3 file with no ID3v2 tags
        File file = Paths.get("src", "test", "resources", "noID3TagMP3.mp3").toFile();

        assertThrows(ID3TagException.class, () -> {
            MetadataReader.getMetadata(file);
        });

    }

    @Test
    public void testCheckMetadataMP3() {

        // check if the metadata reader can handle a mp3 file with valid metadata
        File file = Paths.get("src", "test", "resources", "goodTestMP3.mp3").toFile();
        HashMap<String, String> metadata;

        try {
            metadata = MetadataReader.getMetadata(file);
        } catch (ID3TagException | BadFileTypeException e) {
            e.printStackTrace();
            assert false;
            return;
        }

        // few eq checks
        assertEquals("Chinese DOIT sound effect", metadata.get("title"));
        assertEquals("ant0in", metadata.get("artist"));
        assertEquals("Electro", metadata.get("genre"));
        // cover is not tested smh
        assertEquals("2", metadata.get("duration"));

    }

    @Test
    public void testCheckMetadataWAV() {

        // check if the metadata reader can handle a mp3 file with valid metadata
        File file = Paths.get("src", "test", "resources", "goodTestWAV.wav").toFile();
        HashMap<String, String> metadata;

        try {
            metadata = MetadataReader.getMetadata(file);
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
        assertEquals("3seconds", metadata.get("title"));
        assertEquals("Sample", metadata.get("artist"));
        assertEquals("Celtic", metadata.get("genre"));
        // cover is not tested smh
        assertEquals("3", metadata.get("duration"));
    }

    @Test
    public void testNoTagsWAV() {

        // check if the metadata reader can handle a mp3 file with no ID3v2 tags
        File file = Paths.get("src", "test", "resources", "noTagWAV.wav").toFile();

        try {
            System.out.println(MetadataReader.getMetadata(file));
            assertEquals("", MetadataReader.getMetadata(file).get("title"));
            assertEquals("", MetadataReader.getMetadata(file).get("artist"));
            assertEquals("", MetadataReader.getMetadata(file).get("genre"));
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

        assertThrows(BadFileTypeException.class, () -> {
            MetadataReader.getMetadata(file);
        });
    }

}
