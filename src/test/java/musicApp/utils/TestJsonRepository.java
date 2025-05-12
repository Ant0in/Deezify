package musicApp.utils;

import musicApp.models.Equalizer;
import musicApp.models.Library;
import musicApp.models.Settings;
import musicApp.models.UserProfile;
import musicApp.repositories.JsonRepository;
import musicApp.repositories.LyricsRepository.LyricsFilePaths;
import musicApp.exceptions.SettingsFilesException;
import com.google.gson.JsonSyntaxException;
import org.junit.Before;
import org.junit.Test;


import java.nio.file.Path;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.List;
import java.nio.file.Files;


import static org.junit.Assert.*;

public class TestJsonRepository extends JsonRepository {

    @Before
    public void setUp() {
        // set the test data into userProfile.json
        Path userProfilePath = Paths.get("src", "test", "resources", "userProfile.json");
        UserProfile userProfile = new UserProfile("test", Path.of(""), Path.of(""), "English");
        userProfile.setBalance(0.0);
        userProfile.setEqualizer(new Equalizer());
        setUserProfiles(List.of(userProfile), userProfilePath);
        setPlaylistsPath(Paths.get("src", "test", "resources", "playlists.json"));
    }


    public TestJsonRepository() throws SettingsFilesException {
        super();
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
        try {
            Settings settings = getSettings(Paths.get("src", "test", "resources", "settings.json"));
            UserProfile userProfile = new UserProfile("test", Path.of(""), Path.of(""));
        settings.setCurrentUserProfile(userProfile);
        assertEquals(0.0, settings.getBalance(), 0.0);
        assertEquals(10, settings.getEqualizerBands().size());
        assertNotNull(settings.getMusicFolder());} catch (SettingsFilesException e) {
            fail("Failed to read settings: " + e.getMessage());
        }
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
