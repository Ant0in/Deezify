package musicApp.utils;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class TestMusicLoader extends MusicLoader {
    @Test
    public void testNonExistentFolder() {
        Path nonExistentFolder = Paths.get("fdsf");
        assertThrows(IOException.class, () -> {
            getAllSongPaths(nonExistentFolder);
        });
    }

    @Test
    public void testEmptyFolder() throws IOException {
        Path tempFolder = Files.createTempDirectory("testFolder");
        assertTrue(getAllSongPaths(tempFolder).isEmpty());
    }

    @Test
    public void testNonValidFormat() throws IOException {
        Path tempFolder = Files.createTempDirectory("testFolder");
        Files.createTempFile(tempFolder, "tempFile", ".txt");
        assertTrue(getAllSongPaths(tempFolder).isEmpty());
    }

    @Test
    public void testValidFormat() throws IOException {
        Path tempFolder = Files.createTempDirectory("testFolder");
        Path validFormatTempFileWav = Files.createTempFile(tempFolder, "tempFileWav", ".wav");
        Path validFormatTempFileMp3 = Files.createTempFile(tempFolder, "tempFileMp3", ".mp3");
        List<Path> allSongs = getAllSongPaths(tempFolder);
        assertTrue(allSongs.contains(validFormatTempFileMp3));
        assertTrue(allSongs.contains(validFormatTempFileWav));
    }
}
