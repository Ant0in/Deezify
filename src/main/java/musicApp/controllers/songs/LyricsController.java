package musicApp.controllers.songs;

import musicApp.controllers.PlayerController;
import musicApp.controllers.ViewController;
import musicApp.exceptions.SettingsFilesException;
import musicApp.models.Song;
import musicApp.services.LyricsService;
import musicApp.views.songs.LyricsView;

import java.util.List;
import java.util.Optional;

/**
 * The type Lyrics controller.
 */
public class LyricsController extends ViewController<LyricsView> implements LyricsView.LyricsViewListener {

    private final PlayerController playerController;
    private final LyricsService lyricsManager;

    /**
     * Instantiates a new Lyrics controller.
     *
     * @param _controller the controller
     */
    public LyricsController(PlayerController _controller) {
        super(new LyricsView());
        view.setLyricsListener(this);
        playerController = _controller;
        LyricsService newLyricsManager = null; // Initialization to null to avoid lsp warning
        try {
            newLyricsManager = new LyricsService();
        } catch (SettingsFilesException e) {
            alertService.showFatalErrorAlert("Error loading lyrics settings", e);
        }
        lyricsManager = newLyricsManager;
        initView("/fxml/Lyrics.fxml");
        new KaraokeController(playerController, lyricsManager, view);
    }

    /**
     * Returns the lyrics of the currently loaded song
     * If no song is loaded, returns a list with a single element "No song loaded."
     *
     * @return the current song lyrics
     */
    public List<String> getCurrentSongLyrics() {
        Song song = playerController.getCurrentlyLoadedSong();
        if (song == null) {
            return List.of("No song loaded.");
        }
        return song.getLyrics();
    }

    /**
     * Updates the current song lyrics
     * Saves the provided lyrics to a file and updates mapping
     *
     * @param newLyrics the new lyrics
     */
    public void updateCurrentSongLyrics(String newLyrics) {
        Song currentSong = playerController.getCurrentlyLoadedSong();
        if (currentSong == null) {
            System.err.println("No song loaded.");
            return;
        }
        try {
            lyricsManager.saveLyrics(currentSong, newLyrics);
        } catch (Exception e) {
            alertService.showExceptionAlert(e);
        }
        view.updateLyrics();
    }

    public void handleLoadedSongChange(Runnable callback) {
        playerController.getCurrentlyLoadedSongStringProperty().
                addListener((_, _, _) -> callback.run());
    }

    public void handleShowLyrics() {
        view.updateLyrics();
        view.showLyrics();
    }

    /**
     * Edit lyrics.
     */
    public void handleEditLyrics() {
        List<String> currentLyricsList = getCurrentSongLyrics();
        String initialText = "";
        if (currentLyricsList != null && !currentLyricsList.isEmpty()) {
            initialText = String.join("\n", currentLyricsList);
        }

        Optional<String> result = view.getEditedLyrics(initialText);
        result.ifPresent(newLyrics -> {
            updateCurrentSongLyrics(newLyrics);
            view.updateLyrics();
        });
    }

    /**
     * Refresh ui.
     */
    public void refreshUI() {
        view.refreshUI();
    }

}
