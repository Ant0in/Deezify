package musicApp.controllers.songs;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.stage.FileChooser;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import musicApp.controllers.PlayerController;
import musicApp.models.Song;
import musicApp.utils.lyrics.KaraokeLine;
import musicApp.utils.lyrics.LyricsManager;
import musicApp.views.songs.LyricsView;

import java.util.List;

/**
 * This controller handles the synchronization of karaoke lines
 * with the current playback time of a song.
 */
public class KaraokeController {

    private final PlayerController playerController;
    private final LyricsManager lyricsManager;
    private final LyricsView view;

    private Timeline syncTimeline;
    private List<KaraokeLine> lyricsToDisplay;

    /**
     * Instantiates a new Karaoke controller.
     *
     * @param _controller the controller
     * @param _manager    the manager
     * @param _view       the view
     */
    public KaraokeController(PlayerController _controller,
                             LyricsManager _manager,
                             LyricsView _view) {
        playerController = _controller;
        lyricsToDisplay = List.of();
        lyricsManager = _manager;
        view = _view;
    }

    /**
     * Gets karaoke lines.
     *
     * @return the karaoke lines
     */
    public List<KaraokeLine> getKaraokeLines() {
        Song currentSong = playerController.getCurrentlyLoadedSong();
        if (currentSong == null) return List.of();
        return lyricsManager.getKaraokeLines(currentSong);
    }

    /**
     * Imports karaoke lyrics from a .lrc file and updates the song's lyrics.
     * If a .txt file already exists, prompts the user for confirmation to overwrite it.
     */
    public void importKaraokeLyrics() {
        Song currentSong = playerController.getCurrentlyLoadedSong();
        if (currentSong == null) return;

        Optional<Path> selectedFile = view.showLrcFileChooser();
        if (selectedFile.isEmpty()) return;

        String txtPath = lyricsManager.getTxtLyricsPath(currentSong);
        boolean txtExists = txtPath != null && Files.exists(lyricsManager.getLyricsFile(txtPath));
        boolean overwriteTxt;

        if (txtExists) {
            Optional<Boolean> userChoice = view.showOverwriteTxtConfirmation();
            if (userChoice.isEmpty()) return;
            overwriteTxt = userChoice.get();
        } else {
            overwriteTxt = true; 
        }

        lyricsManager.importLrc(currentSong, selectedFile.get(), overwriteTxt);
        view.updateKaraokeLyrics();
        startKaraoke();
    }

    /**
     * Starts the karaoke feature by creating a timeline that updates the lyrics
     * based on the current playback time of the song.
     */
    public void startKaraoke() {
        Song currentSong = playerController.getCurrentlyLoadedSong();

        if (currentSong == null) return;

        lyricsToDisplay = lyricsManager.getKaraokeLines(currentSong);
        stopKaraoke();

        view.updateKaraokeLyricsHighlight(lyricsToDisplay, null);

        syncTimeline = new Timeline(new KeyFrame(Duration.millis(200), e -> syncLyrics()));
        syncTimeline.setCycleCount(Timeline.INDEFINITE);
        syncTimeline.play();
    }

    /**
     * Stop the timeline so we no longer update lines.
     */
    public void stopKaraoke() {
        if (syncTimeline != null) {
            syncTimeline.stop();
            syncTimeline = null;
        }
    }

    /**
     * Called periodically by the timeline to highlight the correct line.
     */
    private void syncLyrics() {
        if (lyricsToDisplay.isEmpty()) return;

        Duration currentTime = playerController.getCurrentSongTime();
        if (currentTime == null) return;

        KaraokeLine activeLine = getActiveLineAtTime(currentTime);
        view.updateKaraokeLyricsHighlight(lyricsToDisplay, activeLine);
    }


    /**
     * Finds the last line whose timestamp is before or equal to the current playback time.
     */
    private KaraokeLine getActiveLineAtTime(Duration currentTime) {
        for (int i = lyricsToDisplay.size() - 1; i >= 0; i--) {
            if (currentTime.greaterThanOrEqualTo(lyricsToDisplay.get(i).getTime())) {
                return lyricsToDisplay.get(i);
            }
        }
        return null;
    }
}
