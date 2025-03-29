package musicApp.utils;

import javafx.util.Duration;
import musicApp.exceptions.BadFileTypeException;
import musicApp.exceptions.ID3TagException;
import musicApp.models.Metadata;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;


public class TestMetadataReader {


    @Test
    public void testReadValidFile() {
        // check if the metadata reader can handle a mp3 file with valid metadata
        File file = Paths.get("src", "test", "resources", "goodTestMP3.mp3").toFile();
        boolean hasExceptionOccured = false;
        MetadataUtils reader = new MetadataUtils();

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
        MetadataUtils reader = new MetadataUtils();

        assertThrows(ID3TagException.class, () -> {
            reader.getMetadata(file);
        });

    }

    @Test
    public void testCheckMetadataMP3() {
        // check if the metadata reader can handle a mp3 file with valid metadata
        File file = Paths.get("src", "test", "resources", "goodTestMP3.mp3").toFile();
        Metadata metadata;
        MetadataUtils reader = new MetadataUtils();

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
        MetadataUtils reader = new MetadataUtils();

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
        MetadataUtils reader = new MetadataUtils();
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
        MetadataUtils reader = new MetadataUtils();

        assertThrows(BadFileTypeException.class, () -> {
            reader.getMetadata(file);
        });
    }

    /*
    Tests if the setMetadata correctly sets data fields on mp3 files
     */
    @Test
    public void testWriteFieldMP3() throws IOException, CannotWriteException, FieldDataInvalidException, BadFileTypeException, ID3TagException {

        Path defaultSongPath = Paths.get("src", "test", "resources", "defaultWritableTestMP3.mp3");
        Path songPath = Paths.get("src", "test", "resources", "writableTestMP3.mp3");
        Files.copy(defaultSongPath,songPath, StandardCopyOption.REPLACE_EXISTING);

        Metadata metadata = new Metadata();
        metadata.setTitle("Edited Title");
        metadata.setArtist("Edited Artist");
        metadata.setGenre("Edited Genre");

        MetadataUtils metaUtils = new MetadataUtils();

        metaUtils.setMetadata(metadata, songPath.toFile());

        Metadata metadata2 = metaUtils.getMetadata(songPath.toFile());

        assertEquals(metadata.getTitle(), metadata2.getTitle());
        assertEquals(metadata.getArtist(), metadata2.getArtist());
        assertEquals(metadata.getGenre(), metadata2.getGenre());

        Files.delete(songPath);

    }

    /*
    Tests if the setMetadata correctly sets data fields on wav files
     */
    @Test
    public void testWriteFieldWAV() throws IOException, CannotWriteException, FieldDataInvalidException, BadFileTypeException, ID3TagException {

        Path defaultSongPath = Paths.get("src", "test", "resources", "defaultWritableTestWAV.wav");
        Path songPath = Paths.get("src", "test", "resources", "writableTestWAV.wav");
        Files.copy(defaultSongPath,songPath, StandardCopyOption.REPLACE_EXISTING);

        Metadata metadata = new Metadata();
        metadata.setTitle("Edited Title");
        metadata.setArtist("Edited Artist");
        metadata.setGenre("Edited Genre");

        MetadataUtils metaUtils = new MetadataUtils();

        metaUtils.setMetadata(metadata, songPath.toFile());

        Metadata metadata2 = metaUtils.getMetadata(songPath.toFile());

        assertEquals(metadata.getTitle(), metadata2.getTitle());
        assertEquals(metadata.getArtist(), metadata2.getArtist());
        assertEquals(metadata.getGenre(), metadata2.getGenre());

        Files.delete(songPath);

    }

    /**
     * Tests if UserTags are correctly formatted, written on file, read and parsed on wav file
     * @throws IOException
     * @throws CannotWriteException
     * @throws FieldDataInvalidException
     * @throws BadFileTypeException
     * @throws ID3TagException
     */
    @Test
    public void testWriteUserTagsWAV() throws IOException, CannotWriteException, FieldDataInvalidException, BadFileTypeException, ID3TagException {

        Path defaultSongPath = Paths.get("src", "test", "resources", "defaultWritableTestWAV.wav");
        Path songPath = Paths.get("src", "test", "resources", "writableTestWAV.wav");
        Files.copy(defaultSongPath,songPath, StandardCopyOption.REPLACE_EXISTING);

        Metadata metadata = new Metadata();

        ArrayList<String> userTags = new ArrayList<String>(Arrays.asList("custom1", "custom2"));
        metadata.setUserTags(userTags);

        MetadataUtils metaUtils = new MetadataUtils();

        metaUtils.setMetadata(metadata, songPath.toFile());

        Metadata metadata2 = metaUtils.getMetadata(songPath.toFile());

        assertEquals(metadata.getUserTags(), metadata2.getUserTags());

        Files.delete(songPath);

    }

    /**
     * Tests if UserTags are correctly formatted, written on file, read and parsed on mp3 file
     * @throws IOException
     * @throws CannotWriteException
     * @throws FieldDataInvalidException
     * @throws BadFileTypeException
     * @throws ID3TagException
     */
    @Test
    public void testWriteUserTagsMP3() throws IOException, CannotWriteException, FieldDataInvalidException, BadFileTypeException, ID3TagException {

        Path defaultSongPath = Paths.get("src", "test", "resources", "defaultWritableTestMP3.mp3");
        Path songPath = Paths.get("src", "test", "resources", "writableTestMP3.mp3");
        Files.copy(defaultSongPath,songPath, StandardCopyOption.REPLACE_EXISTING);

        Metadata metadata = new Metadata();

        ArrayList<String> userTags = new ArrayList<String>(Arrays.asList("custom1", "custom2"));
        metadata.setUserTags(userTags);

        MetadataUtils metaUtils = new MetadataUtils();

        metaUtils.setMetadata(metadata, songPath.toFile());

        Metadata metadata2 = metaUtils.getMetadata(songPath.toFile());

        assertEquals(metadata.getUserTags(), metadata2.getUserTags());

        Files.delete(songPath);

    }



}
