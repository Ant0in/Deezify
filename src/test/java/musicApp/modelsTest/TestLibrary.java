package musicApp.modelsTest;

import musicApp.models.Library;
import musicApp.models.Song;
import musicApp.repositories.PathRepository;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.*;

public class TestLibrary extends PathRepository {
    private Library loadLibrary() {
        Library library = new Library();
        Path folderPath = Paths.get("src", "test", "resources");
        List<Path> songs;
        try {
            PathRepository loader = new PathRepository();
            songs = loader.getAllSongPaths(folderPath);
        } catch (IOException e) {
            System.err.println("Error while loading library: " + e.getMessage() + " \n Song list initialized empty");
            return null;
        }
        library.clear();
        for (Path songPath : songs) {
            library.add(new Song(songPath));
        }
        return library;
    }


    @Test
    public void testLoad() {
        // Test that the library is loaded with the correct number of songs
        Path songFolder = Paths.get("src", "test", "resources");
        Library library = loadLibrary();
        try {
            assert library != null;
            assertEquals(this.getAllSongPaths(songFolder).size(), library.size());
        } catch (IOException e) {
            // Nothing to do here
        }
    }

    @Test
    public void testSearch() {
        Library library = loadLibrary();
        String[] trueAssert = {"ant0in"};
        for (String s : trueAssert) {
            assert library != null;
            assertTrue(library.search(s).stream().anyMatch(song -> Objects.equals(song.getArtist(), s)));
        }
        String[] falseAssert = {"fakeArtist"};
        for (String s : falseAssert) {
            assertFalse(library.search(s).stream().anyMatch(song -> Objects.equals(song.getArtist(), s)));
        }
    }
}
