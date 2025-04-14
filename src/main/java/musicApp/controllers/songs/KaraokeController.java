package musicApp.controllers.songs;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import musicApp.controllers.PlayerController;
import musicApp.exceptions.LyricsNotFoundException;
import musicApp.models.Song;
import musicApp.models.KaraokeLine;
import musicApp.services.AlertService;
import musicApp.services.LyricsService;
import musicApp.views.songs.LyricsView;

/**
 * This controller handles the synchronization of karaoke lines
 * with the current playback time of a song.
 */
public class KaraokeController {

    private final PlayerController playerController;
    private final LyricsService lyricsManager;
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
                             LyricsService _manager,
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
        try {
            return currentSong.getKaraokeLines();
        } catch (LyricsNotFoundException e) {
            return List.of();
        }
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

        Optional<Boolean> evaluation = evaluateOverwriteTxt(currentSong);
        if (evaluation.isEmpty()) return;
        boolean overwriteTxt = evaluation.get();
        try {
            lyricsManager.importLrc(currentSong, selectedFile.get(), overwriteTxt);
        } catch (Exception e) {
            AlertService as = new AlertService();
            as.showExceptionAlert(e);
        }
        view.updateKaraokeLyrics();
        startKaraoke();
    }

    /**
     * Evaluates whether to overwrite the existing .txt file for the current song.
     * If the .txt file exists, prompts the user for confirmation.
     *
     * @param currentSong The current song
     * @return Optional containing true if the user chooses to overwrite, false otherwise
     */
    private Optional<Boolean> evaluateOverwriteTxt(Song currentSong) {
        boolean txtExists = lyricsManager.txtExists(currentSong);
        boolean overwriteTxt;

        if (txtExists) {
            Optional<Boolean> userChoice =view.showOverwriteTxtConfirmation();
            if (userChoice.isEmpty()) return Optional.empty();
            overwriteTxt = userChoice.get();
        } else {
            overwriteTxt = true;
        }
        return Optional.of(overwriteTxt);
    }

    /**
     * Starts the karaoke feature by creating a timeline that updates the lyrics
     * based on the current playback time of the song.
     */
    public void startKaraoke() {
        Song currentSong = playerController.getCurrentlyLoadedSong();

        if (currentSong == null) return;

        try {
            lyricsToDisplay = currentSong.getKaraokeLines();
        } catch (LyricsNotFoundException e) {
            System.err.println("Error getting karaoke lines: " + e.getMessage());
            return;
        }
        stopKaraoke();

        view.updateKaraokeLyricsHighlight(lyricsToDisplay, null);

        syncTimeline = new Timeline(new KeyFrame(Duration.millis(200), _ -> syncLyrics()));
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
