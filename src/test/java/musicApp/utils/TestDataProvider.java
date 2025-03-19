package musicApp.utils;

import musicApp.models.Playlist;
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
}
