package musicApp.repositories;

import com.google.gson.annotations.Expose;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

/**
 * Repository for managing lyrics file paths.
 * This class handles the storage and retrieval of lyrics file paths for songs.
 * It uses a JSON file to store the paths and provides methods to read and write these paths, but also ensure
 * the integrity of the data.
 */
public class LyricsRepository {
    private final JsonRepository jsonRepository;
    
    /**
     * Store both the text and karaoke paths for a song
     */
    public static class LyricsFilePaths {
        @Expose
        private String textPath;
        @Expose
        private String karaokePath;
        @Expose
        private final String songPath;
        
        public LyricsFilePaths(String _songPath, String _textPath, String _karaokePath) {
            textPath = _textPath;
            karaokePath = _karaokePath;
            songPath = _songPath;
        }
        
        public String getTextPath() {
            return textPath;
        }
        
        public String getKaraokePath() {
            return karaokePath;
        }

        public String getSongPath() {
            return songPath;
        }

        public void setPathLyricsTxt(String path) {
            textPath = path;
        }

        public void setPathLyricsKaraoke(String path) {
            karaokePath = path;
        }
    }

    /**
     * Constructor injecting a DataProvider, which handles file paths and JSON reading/writing.
     */
    public LyricsRepository(JsonRepository jsonRepository) {
        this.jsonRepository = jsonRepository;
    }

    /**
     * Returns the path of the lyrics directory.
     */
    public Path getLyricsDir() {
        return jsonRepository.getLyricsDir();
    }

    /**
     * Gets the paths to lyrics files for a song.
     * @param songPath The path to the song file
     * @return LyricsFilePaths containing text and karaoke paths, or null if not found
     */
    public Optional<LyricsFilePaths> getLyricsPaths(String songPath) {
        if (songPath == null) return Optional.empty();

        List<LyricsFilePaths> lib = jsonRepository.readLyricsLibrary();

        return lib.stream().filter(entry -> entry.getSongPath().equals(songPath))
                .findFirst();
    }

    /**
     * Updates the lyrics file paths for a song.
     * @param songPath Path to the song file
     * @param textPath Path to the text lyrics file (relative to lyrics directory)
     * @param karaokePath Path to the karaoke lyrics file (relative to lyrics directory)
     */
    private void updateLyricsPaths(String songPath, String textPath, String karaokePath) throws IllegalArgumentException, IOException {
        if (songPath == null) throw new IllegalArgumentException("Song path cannot be null");
        List<LyricsFilePaths> lib = jsonRepository.readLyricsLibrary();
        boolean updated = false;
        for (LyricsFilePaths entry : lib) {
            if (entry.getSongPath().equals(songPath)) {
                if (textPath != null) entry.setPathLyricsTxt(textPath);
                if (karaokePath != null) entry.setPathLyricsKaraoke(karaokePath);
                updated = true;
                break;
            }
        }
        if (!updated) {
            lib.add(new LyricsFilePaths(songPath, textPath, karaokePath));
        }
        jsonRepository.writeLyricsLibrary(lib);
    }

    /**
     * Updates just the text lyrics path for a song.
     */
    public void updateTextLyricsPath(String songPath, String textPath) throws IllegalArgumentException, IOException {
        updateLyricsPaths(songPath, textPath, null);
    }

    /**
     * Updates just the karaoke lyrics path for a song.
     */
    public void updateKaraokeLyricsPath(String songPath, String karaokePath) throws IllegalArgumentException, IOException {
        updateLyricsPaths(songPath, null, karaokePath);
    }



}
