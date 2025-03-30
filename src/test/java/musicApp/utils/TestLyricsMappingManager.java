package musicApp.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import static org.junit.Assert.*;

public class TestLyricsMappingManager {

    private final String songKey = "/tmp/TestSong.mp3";
    private final String lyricsContent = "Line 1\nLine 2\nLine 3";
    private LyricsMappingManager manager;
    private Path lyricsFile;
    private Path jsonFile;
    private Path lyricsDir;

    @Before
    public void setUp() throws IOException {
        manager = new LyricsMappingManager();
        lyricsDir = manager.getLyricsDir();
        lyricsFile = lyricsDir.resolve("TestSong.txt");
        jsonFile = lyricsDir.resolve("lyrics.json");

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
        List<String> lyrics = manager.getSongLyrics("/tmp/FakeSong.mp3");
        assertNotNull(lyrics);  
        assertTrue(lyrics.isEmpty());
    }

    @Test
    public void testSaveAndReadLyrics() throws IOException {
        Files.writeString(lyricsFile, lyricsContent);
        manager.updateLyricsMapping(songKey, "TestSong.txt");

        List<String> readLyrics = manager.getSongLyrics(songKey);

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
        String result = manager.getSongLyricsPath(songKey);
        assertNull(result);
    }
}
