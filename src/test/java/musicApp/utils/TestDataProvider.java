package musicApp.utils;

import musicApp.models.Playlist;
import musicApp.models.Settings;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.*;

public class TestDataProvider extends DataProvider {
    @Test
    public void testGetPlaylist() {
        readPlaylists();
        List<Playlist> playlists = getPlaylists(Paths.get("src", "test", "resources", "playlists.json"));
        assertEquals(3, playlists.size());
        assertEquals("Playlist 1", playlists.getFirst().getName());
        assertNull(playlists.getFirst().getImage());
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
        assertNotNull(settings.getMusicDirectory());
    }
}
