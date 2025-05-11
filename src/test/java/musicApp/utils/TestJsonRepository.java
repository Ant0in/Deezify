package musicApp.utils;

import musicApp.models.Equalizer;
import musicApp.models.Library;
import musicApp.models.Settings;
import musicApp.models.UserProfile;
import musicApp.repositories.JsonRepository;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.*;

public class TestJsonRepository extends JsonRepository {

    @Before
    public void setUp() {
        // set the test data into userProfile.json
        Path userProfilePath = Paths.get("src", "test", "resources", "userProfile.json");
        UserProfile userProfile = new UserProfile("test", Path.of(""), Path.of(""));
        userProfile.setBalance(0.0);
        userProfile.setEqualizer(new Equalizer());
        setUserProfiles(List.of(userProfile), userProfilePath);
        setPlaylistsPath(Paths.get("src", "test", "resources", "playlists.json"));
    }

    @Test
    public void testReadPlaylist() {
//        readPlaylists();
        List<Library> playlists = readPlaylists();
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
        UserProfile userProfile = new UserProfile("test", Path.of(""), Path.of(""));
        settings.setCurrentUserProfile(userProfile);
        assertEquals(0.0, settings.getBalance(), 0.0);
        assertEquals(10, settings.getEqualizerBands().size());
        assertNotNull(settings.getMusicFolder());
    }

    @Test
    public void testGetUserProfile() {
        List<UserProfile> userProfiles = getUserProfiles(Paths.get("src", "test", "resources", "userProfile.json"));
        assertEquals(1, userProfiles.size());
        assertEquals("test", userProfiles.getFirst().getUsername());
        assertEquals(0.0, userProfiles.getFirst().getBalance(), 0.0);
        assertEquals(10, userProfiles.getFirst().getEqualizerBands().size());
        assertNotNull(userProfiles.getFirst().getUserMusicPath());

    }
}
