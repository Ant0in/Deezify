package musicApp.controllers.songs;

import javafx.scene.control.Alert;
import javafx.util.Duration;
import musicApp.controllers.PlayerController;
import musicApp.controllers.ViewController;
import musicApp.exceptions.LyricsNotFoundException;
import musicApp.models.KaraokeLine;
import musicApp.models.Song;
import musicApp.services.AlertService;
import musicApp.services.LyricsService;
import musicApp.views.songs.LyricsView;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

/**
 * This controller handles the synchronization of karaoke lines
 * with the current playback time of a song.
 */
public class KaraokeController extends ViewController<LyricsView> implements LyricsView.KaraokeListener {

    private final PlayerController playerController;
    private final LyricsService lyricsManager;
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
        super(_view);
        playerController = _controller;
        lyricsToDisplay = List.of();
        lyricsManager = _manager;
        view.setKaraokeListener(this);
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
        } catch (Exception e) {
            return List.of();
        }
    }

    /**
     * Imports karaoke lyrics from a .lrc file and updates the song's lyrics.
     * If a .txt file already exists, prompts the user for confirmation to overwrite it.
     */
    public void handleImportKaraokeLyrics() {
        Song currentSong = playerController.getCurrentlyLoadedSong();
        if (currentSong == null) return;

        Optional<Path> selectedFile = view.getLrcFile();
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
        handleShowKaraoke();
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
            Optional<Boolean> userChoice = view.showOverwriteTxtConfirmation();
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
    public void handleShowKaraoke() {
        Song currentSong = playerController.getCurrentlyLoadedSong();

        if (currentSong == null) return;

        try {
            lyricsToDisplay = currentSong.getKaraokeLines();
        } catch (LyricsNotFoundException e) {
            alertService.showAlert("Error getting karaoke lines: " + e.getMessage(), Alert.AlertType.ERROR);
            return;
        }
        handleStopKaraoke();
        view.updateKaraokeLyricsHighlight(lyricsToDisplay, null);
        view.updateTimeLine();
        view.showKaraoke();
    }

    /**
     * Stop the timeline so we no longer update lines.
     */
    public void handleStopKaraoke() {
        view.stopKaraoke();
    }

    /**
     * Called periodically by the timeline to highlight the correct line.
     */
    public void handleSyncLyrics() {
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
