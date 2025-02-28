
package MusicApp.ControllersTest;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import org.junit.Test;

import MusicApp.Controllers.MetadataReader;
import MusicApp.Exceptions.ID3TagException;


public class TestMetadataReader {


    @Test
    public void testReadValidFile() {

        // check if the metadata reader can handle a mp3 file with valid metadata
        File file = Paths.get("src", "test", "resources", "goodTestFile.mp3").toFile();
        boolean hasExceptionOccured = false;

        try {
            MetadataReader.getMetadata(file);
        } catch (Exception e) {
            hasExceptionOccured = true;
        }

        assert !hasExceptionOccured;

    }

    @Test
    public void testNoID3v2Tags() {

        // check if the metadata reader can handle a mp3 file with no ID3v2 tags
        File file = Paths.get("src", "test", "resources", "noID3TagFile.mp3").toFile();

        assertThrows(ID3TagException.class, () -> {
            MetadataReader.getMetadata(file);
        });

    }

    @Test
    public void testCheckMetadata() {

        // check if the metadata reader can handle a mp3 file with valid metadata
        File file = Paths.get("src", "test", "resources", "goodTestFile.mp3").toFile();
        HashMap<String, String> metadata;

        try {
            metadata = MetadataReader.getMetadata(file);
        } catch (ID3TagException e) {
            e.printStackTrace();
            assert false;
            return;
        }

        // few eq checks

        assertEquals("china idk", metadata.get("Album"));
        assertEquals("ant0in", metadata.get("Artist"));
        assertEquals("comment 101 idfk", metadata.get("Comment"));
        assertEquals("Electro", metadata.get("Genre"));
        assertEquals("Chinese DOIT sound effect", metadata.get("Title"));
        assertEquals("101", metadata.get("Track"));
        assertEquals("20210912", metadata.get("Year"));

    }

}
