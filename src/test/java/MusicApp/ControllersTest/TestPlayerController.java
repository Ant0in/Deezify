package MusicApp.ControllersTest;

import MusicApp.Controllers.PlayerController;
import javafx.application.Platform;
import javafx.util.Duration;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class TestPlayerController {

    // Cette méthode sera exécutée une seule fois avant tous les tests
    @BeforeClass
    public static void setUpBeforeClass() {
        // Cette ligne initialise la plateforme JavaFX avant d'exécuter les tests.
        Platform.startup(() -> {
            // Cette méthode initialise JavaFX dans le thread de l'application.
        });
    }

    @Test
    public void testPlayFromLibrary() {
        PlayerController playerController = new PlayerController();
        playerController.playFromLibrary(0);
        playerController.getAudioPlayer().getMediaPlayer().onReadyProperty().addListener((observable, oldValue, newValue) -> {
            assertTrue(playerController.isPlaying());
            assertEquals("Song1", playerController.getAudioPlayer().currentSongProperty().get());
        });
    }

    @Test
    public void testSkip() {
        PlayerController playerController = new PlayerController();
        playerController.playFromLibrary(0);
        playerController.getAudioPlayer().getMediaPlayer().onReadyProperty().addListener((observable, oldValue, newValue) -> {
            playerController.skip();
            assertTrue(playerController.isPlaying());
            assertEquals("Song2", playerController.getAudioPlayer().currentSongProperty().get());
        });
    }

    @Test
    public void testPrec() {
        PlayerController playerController = new PlayerController();
        playerController.playFromLibrary(1);
        playerController.getAudioPlayer().getMediaPlayer().onReadyProperty().addListener((observable, oldValue, newValue) -> {
            playerController.prec();
            assertTrue(playerController.isPlaying());
            assertEquals("Song1", playerController.getAudioPlayer().currentSongProperty().get());
        });
    }

    @Test
    public void testPauseAndUnpause() {
        PlayerController playerController = new PlayerController();
        playerController.playFromLibrary(0);
        playerController.getAudioPlayer().getMediaPlayer().onReadyProperty().addListener((observable, oldValue, newValue) -> {
            playerController.pause();
            assertFalse(playerController.isPlaying());
            playerController.unpause();
            assertTrue(playerController.isPlaying());
        });
    }

    @Test
    public void testAddToQueue() {
        PlayerController playerController = new PlayerController();
        playerController.addToQueue(1);
        List<String> queue = playerController.getQueue();
        assertEquals(1, queue.size());
        assertEquals("Song2", queue.get(0));
    }

    @Test
    public void testRemoveFromQueue() {
        PlayerController playerController = new PlayerController();
        playerController.addToQueue(1);
        playerController.removeFromQueue(0);
        List<String> queue = playerController.getQueue();
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testClearQueue() {
        PlayerController playerController = new PlayerController();
        playerController.addToQueue(1);
        playerController.clearQueue();
        List<String> queue = playerController.getQueue();
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testPlayFromQueue() {
        PlayerController playerController = new PlayerController();
        playerController.addToQueue(1);
        playerController.playFromQueue(0);
        playerController.getAudioPlayer().getMediaPlayer().onReadyProperty().addListener((observable, oldValue, newValue) -> {
            assertTrue(playerController.isPlaying());
            assertEquals("Song2", playerController.getAudioPlayer().currentSongProperty().get());
        });
    }

    @Test
    public void testGetCurrentTime() {
        PlayerController playerController = new PlayerController();
        playerController.playFromLibrary(0);
        Duration currentTime = playerController.getCurrentTime();
        assertNotNull(currentTime);
    }

    @Test
    public void testGetTotalDuration() {
        PlayerController playerController = new PlayerController();
        playerController.playFromLibrary(0);
        Duration totalDuration = playerController.getTotalDuration();
        assertNotNull(totalDuration);
    }
}