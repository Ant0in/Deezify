package musicApp.controllers.songs;

import java.util.List;
import java.util.Optional;
import javafx.stage.FileChooser;
import java.io.File;
import java.nio.file.Path;
import javafx.beans.property.StringProperty;
import java.nio.file.Files;

import musicApp.controllers.PlayerController;
import musicApp.controllers.ViewController;
import musicApp.models.Song;
import musicApp.utils.lyrics.LyricsManager;
import musicApp.utils.DataProvider;
import musicApp.utils.lyrics.LyricsDataAccess;
import musicApp.views.songs.LyricsView;
import musicApp.utils.lyrics.KaraokeLine;

/**
 * The type Lyrics controller.
 */
public class LyricsController extends ViewController<LyricsView, LyricsController> {

    private final PlayerController playerController;
    private final LyricsManager lyricsManager;

    /**
     * Instantiates a new Lyrics controller.
     *
     * @param _controller the controller
     */
    public LyricsController(PlayerController _controller) {
        super(new LyricsView());
        playerController = _controller;
        DataProvider dataProvider = new DataProvider();
        LyricsDataAccess lyricsDataAccess = new LyricsDataAccess(dataProvider);
        lyricsManager = new LyricsManager(lyricsDataAccess);
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
        return lyricsManager.getLyrics(song);
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
        lyricsManager.saveLyrics(currentSong, newLyrics);
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
