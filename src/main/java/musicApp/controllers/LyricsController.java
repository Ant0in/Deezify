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
        return LyricsMappingManager.getSongLyrics(song.getFilePath().toString());
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
        // save lyrics to .txt file and name it after the song
        String songName = Paths.get(currentSong.getFilePath().toString()).getFileName().toString();
        String lyricsFileName;

        if (songName.endsWith(".mp3")) {
            lyricsFileName = songName.replace(".mp3", ".txt");
        } else if (songName.endsWith(".wav")) {
            lyricsFileName = songName.replace(".wav", ".txt");
        } else {
            System.out.println("Format not supported"); // TODO : exception ?
            return;
        }

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
        String newRelativePath = lyricsFileName;
        LyricsMappingManager.updateLyricsMapping(songName, newRelativePath);
        currentSong.setLyrics(newRelativePath);  // metadata update
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

}
