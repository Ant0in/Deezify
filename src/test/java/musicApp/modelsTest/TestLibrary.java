package musicApp.modelsTest;

import musicApp.models.Library;
import musicApp.utils.MusicLoader;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static org.junit.Assert.*;

public class TestLibrary extends MusicLoader {
    @Test
    public void testLoad() {
        // Test that the library is loaded with the correct number of songs
        Library library = new Library();
        Path songFolder = Paths.get("src", "test", "resources");
        library.load(songFolder);
        try {
            assertEquals(this.getAllSongPaths(songFolder).size(), library.size());
        } catch (IOException e) {
            // Nothing to do here
        }
    }

    @Test
    public void testSearch() {
        Library library = new Library();
        library.load(Paths.get("src", "test", "resources"));
        String[] trueAssert = {"ant0in"};
        for (String s : trueAssert) {
            assertTrue(library.search(s).stream().anyMatch(song -> Objects.equals(song.getArtist(), s)));
        }
        String[] falseAssert = {"fakeArtist"};
        for (String s : falseAssert) {
            assertFalse(library.search(s).stream().anyMatch(song -> Objects.equals(song.getArtist(), s)));
        }
    }
}
