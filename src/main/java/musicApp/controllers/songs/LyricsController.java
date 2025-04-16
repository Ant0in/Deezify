package musicApp.controllers.songs;

import java.util.List;
import java.util.Optional;

import javafx.beans.property.StringProperty;
import musicApp.controllers.ViewController;
import musicApp.controllers.PlayerController;
import musicApp.models.Song;
import musicApp.utils.lyrics.LyricsService;
import musicApp.views.songs.LyricsView;
import musicApp.views.songs.LyricsView.LyricsViewListener;

/**
 * The type Lyrics controller.
 */
public class LyricsController extends ViewController<LyricsView> implements LyricsViewListener{

    private final PlayerController playerController;
    private final LyricsService lyricsManager;

    /**
     * Instantiates a new Lyrics controller.
     *
     * @param _controller the controller
     */
    public LyricsController(PlayerController _controller) {
        super(new LyricsView());
        view.setListener(this);
        playerController = _controller;
        lyricsManager = new LyricsService();
        initView("/fxml/Lyrics.fxml");
        view.setKaraokeController(new KaraokeController(playerController, lyricsManager, view));
    }


    /**
     * Get currently loaded song string property.
     *
     * @return the string property
     */
    public StringProperty getCurrentlyLoadedSongStringProperty(){
        return playerController.getCurrentlyLoadedSongStringProperty();
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
            e.printStackTrace();
        }
        view.updateLyrics();
}

    /**
     * Edit lyrics.
     */
    public void editLyrics() {
        List<String> currentLyricsList = getCurrentSongLyrics();
        String initialText = "";
        if (currentLyricsList != null && !currentLyricsList.isEmpty()) {
            initialText = String.join("\n", currentLyricsList);
        }
        
        Optional<String> result = view.showEditLyricsDialog(initialText);
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
