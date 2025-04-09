package musicApp.utils;


import musicApp.models.Song;
import musicApp.utils.lyrics.LyricsDataAccess;
import musicApp.utils.lyrics.LyricsManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import static org.junit.Assert.*;

public class TestLyricsManager {

    private LyricsManager lyricsManager;
    private DataProvider dataProvider;
    private LyricsDataAccess lyricsDataAccess;
    private Path lyricsDir;
    private Path lyricsFile;
    private Path jsonFile;
    private final String songFileName = "TestSong.mp3";
    private final String lyricsContent = "Line 1\nLine 2\nLine 3";
    private Song testSong;

    @Before
    public void setUp() throws IOException {
        dataProvider = new DataProvider();
        lyricsDataAccess = new LyricsDataAccess(dataProvider); 
        lyricsManager = new LyricsManager(lyricsDataAccess);
        lyricsDir = lyricsDataAccess.getLyricsDir(); 

        Files.createDirectories(lyricsDir);

        lyricsFile = lyricsDir.resolve("TestSong.txt");
        jsonFile = lyricsDir.resolve("lyrics.json");

        Files.deleteIfExists(lyricsFile);
        Files.deleteIfExists(jsonFile);

        Path dummySongPath = Paths.get("/tmp", songFileName);
        testSong = new Song(dummySongPath);
    }

    @After
    public void tearDown() throws IOException {
        Files.deleteIfExists(lyricsFile);
        Files.deleteIfExists(jsonFile);
    }

    @Test
    public void testSaveAndReadLyrics() {
        lyricsManager.saveLyrics(testSong, lyricsContent);
        List<String> readLyrics = lyricsManager.getLyrics(testSong);

        assertNotNull(readLyrics);
        assertEquals(3, readLyrics.size());
        assertEquals("Line 1", readLyrics.get(0));
        assertEquals("Line 2", readLyrics.get(1));
        assertEquals("Line 3", readLyrics.get(2));
    }

    @Test
    public void testReadLyricsWhenMissing() {
        List<String> lyrics = lyricsManager.getLyrics(testSong);
        assertNotNull(lyrics);
        assertTrue(lyrics.isEmpty());
    }

    @Test
    public void testLyricsDirExists() {
        assertTrue(Files.exists(lyricsDir));
        assertTrue(Files.isDirectory(lyricsDir));
    }

    @Test
    public void testMappingIsCreated() {
        lyricsManager.saveLyrics(testSong, lyricsContent);
        String path = lyricsDataAccess.getLyricsPathTxt(testSong.getFilePath().toString());
        assertEquals("TestSong.txt", path);
    }
}
