package musicApp.utils;

import musicApp.models.Library;
import musicApp.models.Settings;
import musicApp.repositories.JsonRepository;
import musicApp.repositories.LyricsRepository.LyricsFilePaths;
import org.junit.Test;
import com.google.gson.JsonSyntaxException;


import java.net.URISyntaxException;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.List;
import java.nio.file.Files;


import static org.junit.Assert.*;

public class TestJsonRepository extends JsonRepository {
    @Test
    public void testGetPlaylist() throws URISyntaxException {
//        readPlaylists();
        List<Library> playlists = getPlaylists(Paths.get("src", "test", "resources", "playlists.json"));
        assertEquals(4, playlists.size());
        assertEquals("??favorites??", playlists.getFirst().getName());
        assertNull(playlists.getFirst().getImagePath());
        assertEquals(0, playlists.getFirst().size());
    }

    @Test
    public void testBadPlaylistsName() {
        assertThrows(IllegalArgumentException.class, () -> getPlaylists(Paths.get("src", "test", "resources", "badPlaylistsName.json")));
    }

    @Test
    public void testBadPlaylistsSongs() {
        assertThrows(IllegalArgumentException.class, () -> getPlaylists(Paths.get("src", "test", "resources", "badPlaylistsSongs.json")));
    }

    @Test
    public void testGetSettings() {
        Settings settings = getSettings(Paths.get("src", "test", "resources", "settings.json"));
        assertEquals(0.0, settings.getBalance(), 0.0);
        assertEquals(10, settings.getEqualizerBands().size());
        assertNotNull(settings.getMusicFolder());
    }

    @Test
    public void testMissingLyricsFile() {
        List<LyricsFilePaths> list = readLyricsLibrary(Paths.get("src", "test", "resources", "does_not_exist.json"));
        assertTrue("Missing file should return empty list", list.isEmpty());
    }

 
    @Test
    public void testInvalidLyricsJson() throws IOException {
        Path invalid = Paths.get("src", "test", "resources", "lyrics_invalid.json");
        Files.writeString(invalid, "{ not valid json ");
        assertThrows(JsonSyntaxException.class, () -> readLyricsLibrary(invalid)
        );
    }

    @Test
    public void testValidLyricsJson() {
        List<LyricsFilePaths> list = readLyricsLibrary(Paths.get("src", "test", "resources", "lyrics_valid.json"));
        assertEquals("Should parse 2 entries", 2, list.size());

        LyricsFilePaths first = list.get(0);
        assertEquals("song1.mp3", first.getSongPath());
        assertEquals("song1.txt", first.getTextPath());
        assertEquals("song1.lrc", first.getKaraokePath());
    }

    @Test
    public void testWriteAndReadLyricsLibrary() throws IOException {
        Path roundtrip = Paths.get("src", "test", "resources", "lyrics_roundtrip.json");
        List<LyricsFilePaths> original = List.of(
            new LyricsFilePaths("s1.mp3", "s1.txt", "s1.lrc"),
            new LyricsFilePaths("s2.mp3", null, "s2.lrc")
        );

        writeLyricsLibrary(roundtrip, original);
        List<LyricsFilePaths> result = readLyricsLibrary(roundtrip);

        assertEquals(2, result.size());
        assertEquals("s1.mp3", result.get(0).getSongPath());
        assertEquals("s1.txt", result.get(0).getTextPath());
        assertEquals("s1.lrc", result.get(0).getKaraokePath());
        assertEquals("s2.mp3", result.get(1).getSongPath());
        assertNull  (result.get(1).getTextPath());
        assertEquals("s2.lrc", result.get(1).getKaraokePath());
    }
}
