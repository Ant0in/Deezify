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

    public KaraokeController(PlayerController playerController,
                             LyricsManager lyricsManager,
                             LyricsView view) {
        this.playerController = playerController;
        this.lyricsManager = lyricsManager;
        this.view = view;
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

        String txtPath = lyricsManager.getTxtLyricsPath(song);
        boolean txtExists = txtPath != null && Files.exists(lyricsManager.getLyricsFile(txtPath));

        boolean overwriteTxt;
        if (txtExists) {
            Optional<Boolean> userChoice = view.showOverwriteTxtConfirmation();
            if (userChoice.isEmpty()) return;
            overwriteTxt = userChoice.get();
        } else {
            overwriteTxt = true; 
        }

        lyricsManager.importLrc(song, selectedLrc.get(), overwriteTxt);
        view.updateKaraokeLyrics();
    }

}
