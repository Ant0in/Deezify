/**
 * Test suite for the MetadataUtils class.
 * These tests cover reading and writing metadata (title, artist, genre, user tags, duration, and cover images)
 * for both MP3 and WAV files. They also test error handling, edge cases, and format-specific behavior.
 */
package musicApp.utils;

import javafx.util.Duration;
import musicApp.exceptions.BadFileTypeException;
import musicApp.exceptions.ID3TagException;
import musicApp.models.Metadata;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class TestMetadataUtils {

    private static final MetadataUtils utils = new MetadataUtils();

    private File resolveTestFile(String filename) {
        return Paths.get("src", "test", "resources", filename).toFile();
    }

    private Path prepareWritableCopy(String originalName, String copyName) throws Exception {
        Path source = Paths.get("src", "test", "resources", originalName);
        Path target = Paths.get("src", "test", "resources", copyName);
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        return target;
    }

    /**
     * Validates that reading metadata from a valid MP3 does not throw exceptions.
     */
    @Test
    public void testReadValidFile() {
        File file = resolveTestFile("goodTestMP3.mp3");
        try {
            utils.getMetadata(file);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Validates that reading an MP3 without tags throws ID3TagException.
     */
    @Test
    public void testNoTagsMP3() {
        File file = resolveTestFile("noID3TagMP3.mp3");
        assertThrows(ID3TagException.class, () -> utils.getMetadata(file));
    }

    /**
     * Confirms that metadata is correctly read from a properly tagged MP3.
     */
    @Test
    public void testCheckMetadataMP3() {
        File file = resolveTestFile("goodTestMP3.mp3");
        Metadata metadata;
        try {
            metadata = utils.getMetadata(file);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
            return;
        }
        assertEquals("Chinese DOIT sound effect", metadata.getTitle());
        assertEquals("ant0in", metadata.getArtist());
        assertEquals("Electro", metadata.getGenre());
        assertEquals(Duration.seconds(2), metadata.getDuration());
    }

    /**
     * Confirms that metadata is correctly read from a tagged WAV file.
     */
    @Test
    public void testCheckMetadataWAV() {
        File file = resolveTestFile("goodTestWAV.wav");
        Metadata metadata;
        try {
            metadata = utils.getMetadata(file);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
            return;
        }
        assertEquals("3seconds", metadata.getTitle());
        assertEquals("Sample", metadata.getArtist());
        assertEquals("Celtic", metadata.getGenre());
        assertNull(metadata.getCover());
        assertEquals(Duration.seconds(3), metadata.getDuration());
    }

    /**
     * Confirms fallback behavior when a WAV file contains no metadata.
     */
    @Test
    public void testNoTagsWAV() throws Exception {
        File file = resolveTestFile("noTagWAV.wav");
        Metadata metadata = utils.getMetadata(file);
        LanguageManager lm = LanguageManager.getInstance();
        assertEquals(lm.get("metadata.title"), metadata.getTitle());
        assertEquals(lm.get("metadata.artist"), metadata.getArtist());
        assertEquals(lm.get("metadata.genre"), metadata.getGenre());
    }

    /**
     * Ensures that unsupported file types throw the expected exception.
     */
    @Test
    public void testBadFileType() {
        File file = resolveTestFile("badFileType.opus");
        assertThrows(BadFileTypeException.class, () -> utils.getMetadata(file));
    }

    /**
     * Tests writing and reading title/artist/genre fields on MP3 files.
     */
    @Test
    public void testWriteFieldMP3() throws Exception {
        Path target = prepareWritableCopy("defaultWritableTestMP3.mp3", "writableTestMP3.mp3");
        Metadata metadata = new Metadata();
        metadata.setTitle("Edited Title");
        metadata.setArtist("Edited Artist");
        metadata.setGenre("Edited Genre");
        utils.setMetadata(metadata, target.toFile());
        Metadata loaded = utils.getMetadata(target.toFile());
        assertEquals(metadata.getTitle(), loaded.getTitle());
        assertEquals(metadata.getArtist(), loaded.getArtist());
        assertEquals(metadata.getGenre(), loaded.getGenre());
        Files.delete(target);
    }

    /**
     * Tests writing and reading title/artist/genre fields on WAV files.
     */
    @Test
    public void testWriteFieldWAV() throws Exception {
        Path target = prepareWritableCopy("defaultWritableTestWAV.wav", "writableTestWAV.wav");
        Metadata metadata = new Metadata();
        metadata.setTitle("Edited Title");
        metadata.setArtist("Edited Artist");
        metadata.setGenre("Edited Genre");
        utils.setMetadata(metadata, target.toFile());
        Metadata loaded = utils.getMetadata(target.toFile());
        assertEquals(metadata.getTitle(), loaded.getTitle());
        assertEquals(metadata.getArtist(), loaded.getArtist());
        assertEquals(metadata.getGenre(), loaded.getGenre());
        Files.delete(target);
    }

    /**
     * Tests writing and reading user tags on WAV files.
     */
    @Test
    public void testWriteUserTagsWAV() throws Exception {
        Path target = prepareWritableCopy("defaultWritableTestWAV.wav", "writableTestWAV.wav");
        Metadata metadata = new Metadata();
        metadata.setUserTags(new ArrayList<>(Arrays.asList("custom1", "custom2")));
        utils.setMetadata(metadata, target.toFile());
        Metadata loaded = utils.getMetadata(target.toFile());
        assertEquals(metadata.getUserTags(), loaded.getUserTags());
        Files.delete(target);
    }

    /**
     * Tests writing and reading user tags on MP3 files.
     */
    @Test
    public void testWriteUserTagsMP3() throws Exception {
        Path target = prepareWritableCopy("defaultWritableTestMP3.mp3", "writableTestMP3.mp3");
        Metadata metadata = new Metadata();
        metadata.setUserTags(new ArrayList<>(Arrays.asList("custom1", "custom2")));
        utils.setMetadata(metadata, target.toFile());
        Metadata loaded = utils.getMetadata(target.toFile());
        assertEquals(metadata.getUserTags(), loaded.getUserTags());
        Files.delete(target);
    }

    /**
     * Confirms that cover images persist after a write-read cycle.
     */
    @Test
    public void testCoverImageRoundtripMP3() throws Exception {
        Path target = prepareWritableCopy("MP3WithCover.mp3", "writableCoverTestMP3.mp3");
        Metadata metadata = utils.getMetadata(target.toFile());
        assertNotNull(metadata.getCover());
        utils.setMetadata(metadata, target.toFile());
        Metadata loaded = utils.getMetadata(target.toFile());
        assertNotNull(loaded.getCover());
        assertArrayEquals(metadata.getCover().getBinaryData(), loaded.getCover().getBinaryData());
        Files.delete(target);
    }

    /**
     * Validates that corrupted files throw a RuntimeException when parsed.
     */
    @Test
    public void testCorruptedMP3Throws() {
        File file = resolveTestFile("corruptedMP3.mp3");
        assertThrows(RuntimeException.class, () -> utils.getMetadata(file));
    }

    /**
     * Tests edge case handling of the tag parser on semicolon-delimited strings.
     */
    @Test
    public void testParseUserTagsEdgeCases() throws Exception {
        Method parseMethod = MetadataUtils.class.getDeclaredMethod("parseUserTags", String.class);
        parseMethod.setAccessible(true);
        assertEquals(Arrays.asList("tag1", "", "tag3", ""), parseMethod.invoke(null, "tag1;;tag3;"));
        assertEquals(List.of(""), parseMethod.invoke(null, new Object[]{null}));
        assertEquals(List.of("  "), parseMethod.invoke(null, "  "));
    }

    /**
     * Verifies that tag formatting and parsing roundtrip cleanly.
     */
    @Test
    public void testFormatUserTagsRoundtrip() throws Exception {
        Method formatMethod = MetadataUtils.class.getDeclaredMethod("formatUserTags", ArrayList.class);
        formatMethod.setAccessible(true);
        Method parseMethod = MetadataUtils.class.getDeclaredMethod("parseUserTags", String.class);
        parseMethod.setAccessible(true);
        ArrayList<String> original = new ArrayList<>(Arrays.asList("tagA", "", "tagB"));
        String formatted = (String) formatMethod.invoke(null, original);
        ArrayList<String> parsed = (ArrayList<String>) parseMethod.invoke(null, formatted);
        assertEquals(original, parsed);
    }
}
