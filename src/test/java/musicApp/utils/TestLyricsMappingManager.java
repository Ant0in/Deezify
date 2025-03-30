package musicApp.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import static org.junit.Assert.*;

public class TestLyricsMappingManager {

    private final String songKey = "TestSong-TestArtist-120";
    private final String lyricsContent = "Line 1\nLine 2\nLine 3";
    private final Path lyricsDir = LyricsMappingManager.getLyricsDir();
    private final Path lyricsFile = lyricsDir.resolve(songKey + ".txt");
    private final Path jsonFile = lyricsDir.resolve("lyrics.json");

    @Before
    public void setUp() throws IOException {
        Files.createDirectories(lyricsDir);
        Files.deleteIfExists(lyricsFile);
        Files.deleteIfExists(jsonFile);
    }

    @After
    public void tearDown() throws IOException {
        Files.deleteIfExists(lyricsFile);
        Files.deleteIfExists(jsonFile);
    }

    @Test
    public void testGetLyricsWhenMissing() {
        List<String> lyrics = LyricsMappingManager.getSongLyrics("FakeSong-Unknown-0");
        assertNotNull(lyrics);  
        assertTrue(lyrics.isEmpty());
    }

    @Test
    public void testSaveAndReadLyrics() throws IOException {
        LyricsMappingManager.updateLyricsMapping(songKey, songKey + ".txt");
        Files.writeString(lyricsFile, lyricsContent);

        List<String> readLyrics = LyricsMappingManager.getSongLyrics(songKey);

        assertNotNull(readLyrics);
        assertEquals(3, readLyrics.size());
        assertEquals("Line 1", readLyrics.get(0));
        assertEquals("Line 3", readLyrics.get(2));
    }

    @Test
    public void testLyricsDirCreated() {
        assertTrue(Files.exists(lyricsDir));
        assertTrue(Files.isDirectory(lyricsDir));
    }

    @Test
    public void testNoJsonFileHandledGracefully() {
        assertFalse(Files.exists(jsonFile));
        String result = LyricsMappingManager.getSongLyricsPath(songKey);
        assertNull(result);
    }
}
