package musicApp.utils;


import musicApp.exceptions.LyricsOperationException;
import musicApp.models.Song;
import musicApp.repositories.JsonRepository;
import musicApp.repositories.LyricsRepository;
import musicApp.services.LyricsService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import static org.junit.Assert.*;

public class TestLyricsService {

    private LyricsService lyricsManager;
    private JsonRepository jsonRepository;
    private LyricsRepository lyricsDataAccess;
    private Path lyricsDir;
    private Path lyricsFile;
    private Path jsonFile;
    private final String songFileName = "TestSong.mp3";
    private final String lyricsContent = "Line 1\nLine 2\nLine 3";
    private Song testSong;

    @Before
    public void setUp() throws IOException {
        jsonRepository = new JsonRepository();
        lyricsDataAccess = new LyricsRepository(new JsonRepository());
        lyricsManager = new LyricsService();
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
    public void testSaveAndReadLyrics() throws LyricsOperationException, IOException {
        lyricsManager.saveLyrics(testSong, lyricsContent);
        List<String> readLyrics = testSong.getLyrics();

        assertNotNull(readLyrics);
        assertEquals(3, readLyrics.size());
        assertEquals("Line 1", readLyrics.get(0));
        assertEquals("Line 2", readLyrics.get(1));
        assertEquals("Line 3", readLyrics.get(2));
    }

    @Test
    public void testReadLyricsWhenMissing() {
        List<String> lyrics = testSong.getLyrics();
        assertNotNull(lyrics);
        assertTrue(lyrics.isEmpty());
    }

    @Test
    public void testLyricsDirExists() {
        assertTrue(Files.exists(lyricsDir));
        assertTrue(Files.isDirectory(lyricsDir));
    }

    @Test
    public void testMappingIsCreated() throws LyricsOperationException, IOException {
        lyricsManager.saveLyrics(testSong, lyricsContent);
        LyricsRepository.LyricsFilePaths paths = lyricsDataAccess.getLyricsPaths(testSong.getFilePath().toString())
                .orElseThrow(() -> new IOException("Mapping not found"));
        assertEquals("TestSong.txt", paths.getTextPath());
    }
}
