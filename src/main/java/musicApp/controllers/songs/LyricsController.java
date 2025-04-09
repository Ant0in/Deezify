package musicApp.controllers.songs;

import java.util.List;
import java.util.Optional;
import javafx.stage.FileChooser;
import java.io.File;
import java.nio.file.Path;
import javafx.beans.property.StringProperty;

import musicApp.controllers.PlayerController;
import musicApp.controllers.ViewController;
import musicApp.models.Song;
import musicApp.utils.lyrics.LyricsManager;
import musicApp.utils.DataProvider;
import musicApp.utils.lyrics.LyricsDataAccess;
import musicApp.views.songs.LyricsView;
import musicApp.utils.lyrics.KaraokeLine;

public class LyricsController extends ViewController<LyricsView, LyricsController> {

    private final PlayerController playerController;
    private final LyricsManager lyricsManager;

    public LyricsController(PlayerController playerController) {
        super(new LyricsView());
        this.playerController = playerController;
        //TODO : refactor : avoid creating dataprovider and lyricsManager here
        DataProvider dataProvider = new DataProvider();
        LyricsDataAccess lyricsDataAccess = new LyricsDataAccess(dataProvider);
        this.lyricsManager = new LyricsManager(lyricsDataAccess);
        initView("/fxml/Lyrics.fxml");  
    }


    public StringProperty getCurrentlyLoadedSongStringProperty(){
        return playerController.getCurrentlyLoadedSongStringProperty();
    }

    /**
     * Returns the lyrics of the currently loaded song
     * If no song is loaded, returns a list with a single element "No song loaded."
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

    public void refreshUI() {
        this.view.refreshUI();
    }

    public List<KaraokeLine> getKaraokeLyrics() {
        Song song = playerController.getCurrentlyLoadedSong();
        if (song == null) return List.of();
        return lyricsManager.getKaraokeLines(song);
    }


    public void importKaraokeLyrics() {
        Song song = playerController.getCurrentlyLoadedSong();
        if (song == null) {
            System.err.println("No song loaded.");
            return;
        }

        Optional<Path> selectedLrc = view.showLrcFileChooser();
        if (selectedLrc.isEmpty()) return;

        boolean txtExists = !lyricsManager.getLyrics(song).isEmpty();
        boolean overwriteTxt = !txtExists && view.showOverwriteTxtConfirmation();

        lyricsManager.importLrc(song, selectedLrc.get(), overwriteTxt);
        view.updateKaraokeLyrics();
    }

}
