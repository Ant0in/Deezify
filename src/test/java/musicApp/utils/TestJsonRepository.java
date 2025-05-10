package musicApp.utils;

import musicApp.exceptions.SettingsFilesException;
import musicApp.models.Library;
import musicApp.models.Settings;
import musicApp.repositories.JsonRepository;
import org.junit.Test;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.*;

public class TestJsonRepository extends JsonRepository {

    public TestJsonRepository() throws SettingsFilesException {
        super();
    }

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
        try {
            Settings settings = getSettings(Paths.get("src", "test", "resources", "settings.json"));
            assertEquals(0.0, settings.getBalance(), 0.0);
            assertEquals(10, settings.getEqualizerBands().size());
            assertNotNull(settings.getMusicFolder());
        } catch (SettingsFilesException e) {
            fail("Failed to read settings: " + e.getMessage());
        }
    }
}
