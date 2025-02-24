package MusicApp.ControllersTest;

import MusicApp.Controllers.PlayerController;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import java.util.List;

public class TestPlayerController extends ApplicationTest {

    private PlayerController playerController;
    private List<String> library;

    @Before
    public void setUp() {
        playerController = new PlayerController();
        library = playerController.getLibrary();
    }

    private boolean hasEnoughSongs(int count) {
        return library.size() >= count;
    }

    @Test
    public void testPlayFromLibrary() {
        if (!hasEnoughSongs(1)) return;

        int songIndex = 0;
        playerController.playFromLibrary(songIndex);
        assertTrue(playerController.isPlaying().get());
        assertEquals(library.get(songIndex), playerController.getAudioPlayer().currentSongProperty().get());
    }

    @Test
    public void testSkip() {
        if (!hasEnoughSongs(2)) return;

        playerController.playFromLibrary(0);
        playerController.skip();
        assertTrue(playerController.isPlaying().get());
        assertEquals(library.get(1), playerController.getAudioPlayer().currentSongProperty().get());
    }

    @Test
    public void testPrec() {
        if (!hasEnoughSongs(2)) return;

        playerController.playFromLibrary(1);
        playerController.prec();
        assertTrue(playerController.isPlaying().get());
        assertEquals(library.get(0), playerController.getAudioPlayer().currentSongProperty().get());
    }

    @Test
    public void testPauseAndUnpause() {
        if (!hasEnoughSongs(1)) return;

        playerController.playFromLibrary(0);
        playerController.pause();
        assertFalse(playerController.isPlaying().get());
        playerController.unpause();
        assertTrue(playerController.isPlaying().get());
    }

    @Test
    public void testAddToQueue() {
        if (!hasEnoughSongs(2)) return;

        playerController.addToQueue(1);
        assertEquals(1, playerController.getQueue().size());
        assertEquals(library.get(1), playerController.getQueue().get(0));
    }

    @Test
    public void testRemoveFromQueue() {
        if (!hasEnoughSongs(2)) return;

        playerController.addToQueue(1);
        playerController.removeFromQueue(0);
        assertTrue(playerController.getQueue().isEmpty());
    }

    @Test
    public void testClearQueue() {
        if (!hasEnoughSongs(2)) return;

        playerController.addToQueue(1);
        playerController.clearQueue();
        assertTrue(playerController.getQueue().isEmpty());
    }

    @Test
    public void testPlayFromQueue() {
        if (!hasEnoughSongs(2)) return;

        playerController.addToQueue(1);
        playerController.playFromQueue(0);
        assertTrue(playerController.isPlaying().get());
        assertEquals(library.get(1), playerController.getAudioPlayer().currentSongProperty().get());
    }

    @Test
    public void testGetCurrentTime() {
        if (!hasEnoughSongs(1)) return;

        playerController.playFromLibrary(0);
        assertNotNull(playerController.getCurrentTime());
    }

    @Test
    public void testGetTotalDuration() {
        if (!hasEnoughSongs(1)) return;

        playerController.playFromLibrary(0);
        assertNotNull(playerController.getTotalDuration());
    }
}
