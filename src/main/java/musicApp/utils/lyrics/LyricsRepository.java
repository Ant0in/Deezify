package musicApp.utils.lyrics;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import musicApp.utils.DataProvider;

/**
 * Repository for accessing and managing lyrics file paths.
 * Handles the mapping between songs and their lyrics files.
 */
public class LyricsRepository {

    private final Path lyricsFile;
    private final Path lyricsDir;
    
    /**
     * Store both the text and karaoke paths for a song
     */
    public class LyricsFilePaths {
        private String textPath;
        private String karaokePath;
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
    public LyricsRepository(DataProvider dataProvider) {
        lyricsDir = dataProvider.getLyricsDir();
        dataProvider.createFolderIfNotExists(lyricsDir);
        lyricsFile = lyricsDir.resolve("lyrics.json");
    }

    /**
     * Returns the path of the lyrics directory.
     */
    public Path getLyricsDir() {
        return lyricsDir;
    }

    /**
     * Gets the paths to lyrics files for a song.
     * @param songPath The path to the song file
     * @return LyricsFilePaths containing text and karaoke paths, or null if not found
     */
    public Optional<LyricsFilePaths> getLyricsPaths(String songPath) {
        if (songPath == null) return Optional.empty();

        List<LyricsFilePaths> lib = readLyricsLibrary();

        return lib.stream().filter(entry -> entry.getSongPath().equals(songPath))
                .findFirst();
    }

    /**
     * Updates the lyrics file paths for a song.
     * @param songPath Path to the song file
     * @param textPath Path to the text lyrics file (relative to lyrics directory)
     * @param karaokePath Path to the karaoke lyrics file (relative to lyrics directory)
     * @return true if successful, false otherwise
     */
    private void updateLyricsPaths(String songPath, String textPath, String karaokePath) throws IllegalArgumentException, IOException {
        if (songPath == null) throw new IllegalArgumentException("Song path cannot be null");
        List<LyricsFilePaths> lib = readLyricsLibrary();
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
        writeLyricsLibrary(lib);
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

    private Gson createGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    /**
     * Reads the lyrics library from the lyrics file.
     * If the lyrics file does not exist, it will be created with an empty library.
     */
    private List<LyricsFilePaths> readLyricsLibrary() {
        Gson gson = createGson();
        if (!Files.exists(lyricsFile)) return new ArrayList<LyricsFilePaths>();
        try (var reader = Files.newBufferedReader(lyricsFile)) {
            var type = new TypeToken<List<LyricsFilePaths>>() {}.getType();
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            System.err.println("An error occurred while reading the lyrics file: " + e.getMessage());
            return new ArrayList<LyricsFilePaths>();
        }
    }

    /**
     * Writes the lyrics library to the lyrics file.
     * @return true if successful, false otherwise
     */
    private void writeLyricsLibrary(List<LyricsFilePaths> lib) throws IOException {
        Gson gson = createGson();
        Files.writeString(lyricsFile, gson.toJson(lib));
    }
}
