package musicApp.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.beans.property.StringProperty;
import musicApp.models.Song;
import musicApp.utils.LyricsMappingManager;
import musicApp.views.LyricsView;

public class LyricsController extends ViewController<LyricsView, LyricsController>{
    private final PlayerController playerController;

    public LyricsController(PlayerController playerController) {
        super(new LyricsView());
        this.playerController = playerController;
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
        String compositeKey = LyricsMappingManager.getSongKey(song);
        return LyricsMappingManager.getSongLyrics(compositeKey);
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
        // composite key ; ex :"Hello-Adele-185"
        String compositeKey = LyricsMappingManager.getSongKey(currentSong);
        String lyricsFileName = compositeKey + ".txt";

        // write the lyrics to a file
        Path lyricsFilePath = Paths.get(LyricsMappingManager.LYRICS_DIR, lyricsFileName);
        try {
            if (!Files.exists(lyricsFilePath.getParent())) {
                Files.createDirectories(lyricsFilePath.getParent());
            }
            Files.writeString(lyricsFilePath, newLyrics);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        // update song mapping
        LyricsMappingManager.updateLyricsMapping(compositeKey, lyricsFileName);
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
}
