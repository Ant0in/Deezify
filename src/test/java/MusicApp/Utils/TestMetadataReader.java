
package MusicApp.Utils;

import java.io.File;
import java.nio.file.Paths;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import MusicApp.Exceptions.BadFileTypeException;
import MusicApp.Models.Metadata;
import javafx.util.Duration;
import org.junit.Test;

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

        assertThrows(ID3TagException.class, () -> MetadataReader.getMetadata(file));

    }

    @Test
    public void testCheckMetadataMP3() {

        // check if the metadata reader can handle a mp3 file with valid metadata
        File file = Paths.get("src", "test", "resources", "goodTestMP3.mp3").toFile();
        Metadata metadata;

        try {
            metadata = MetadataReader.getMetadata(file);
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

        try {
            metadata = MetadataReader.getMetadata(file);
        } catch (ID3TagException | BadFileTypeException e) {
            e.printStackTrace();
            assert false;
            return;
        }

        // few eq checks
        assertEquals("3seconds", metadata.getTitle());
        assertEquals("Sample", metadata.getArtist());
        assertEquals("Celtic", metadata.getGenre());
        // cover is not tested smh
        assertEquals(Duration.seconds(Double.parseDouble("3")), metadata.getDuration());
    }

    @Test
    public void testNoTagsWAV() {

        // check if the metadata reader can handle a mp3 file with no ID3v2 tags
        File file = Paths.get("src", "test", "resources", "noTagWAV.wav").toFile();

        try {
            System.out.println(MetadataReader.getMetadata(file));
            assertEquals("Unknown Title", MetadataReader.getMetadata(file).getTitle());
            assertEquals("Unknown Artist", MetadataReader.getMetadata(file).getArtist());
            assertEquals("Unknown Genre", MetadataReader.getMetadata(file).getGenre());
        } catch (ID3TagException | BadFileTypeException e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    public void testBadFileType() {

        // check if the metadata reader can handle a file with an invalid extension
        File file = Paths.get("src", "test", "resources", "badFileType.opus").toFile();

        assertThrows(BadFileTypeException.class, () -> MetadataReader.getMetadata(file));
    }

}
