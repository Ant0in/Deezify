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
    private final LyricsMappingManager lyricsManager;

    public LyricsController(PlayerController playerController) {
        super(new LyricsView());
        this.playerController = playerController;
        this.lyricsManager = new LyricsMappingManager();
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
        String pathSong = lyricsManager.getPathSong(song);
        return lyricsManager.getSongLyrics(pathSong);
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

        String songName = currentSong.getFilePath().getFileName().toString();
        String lyricsFileName;

        if (songName.endsWith(".mp3")) {
            lyricsFileName = songName.replace(".mp3", ".txt");
        } else if (songName.endsWith(".wav")) {
            lyricsFileName = songName.replace(".wav", ".txt");
        }
        else{
            System.err.println("Unsupported file format.");
            return;
        }
        Path lyricsFilePath = lyricsManager.getLyricsDir().resolve(lyricsFileName);
        
        //write the new lyrics to the file
        try {
            if (!Files.exists(lyricsFilePath.getParent())) {
                Files.createDirectories(lyricsFilePath.getParent());
            }
            Files.writeString(lyricsFilePath, newLyrics);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        lyricsManager.updateLyricsMapping(currentSong.getFilePath().toString(), lyricsFileName); 
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
    
    public LyricsMappingManager getLyricsManager() {
        return lyricsManager;
    }

    public String generateLyricsFileName(Song song) {
        return song.getFilePath().getFileName().toString() + ".txt";
}

}
