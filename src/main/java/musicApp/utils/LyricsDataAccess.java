package musicApp.utils;

import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * Provides specialized read/write access to the lyrics.json file
 */
public class LyricsDataAccess {

    private final DataProvider dataProvider;
    private final Path lyricsFile;
    private final Path lyricsDir;

    /**
     * Constructor injecting a DataProvider, which handles file paths and JSON reading/writing.
     */
    public LyricsDataAccess(DataProvider dataProvider) {
        this.dataProvider = dataProvider;

        this.lyricsDir = dataProvider.getLyricsDir();
        dataProvider.createFolderIfNotExists(lyricsDir);
        this.lyricsFile = this.lyricsDir.resolve("lyrics.json");
    }

    /**
     * Returns the path of the lyrics directory.
     */
    public Path getLyricsDir() {
        return this.lyricsDir;
    }
    /**
     * Returns the path of the lyrics file for a given song.
     * If the song is not found, returns null.
     */
    public String getLyricsPath(String pathSong) {
        LyricsLibrary lib = readLyricsLibrary();
        return lib.getSongs().stream()
                .filter(e -> e.getPathSong().equals(pathSong))
                .map(SongLyricsEntry::getPathLyrics)
                .findFirst()
                .orElse(null);
    }

    /**
     * Updates the lyrics mapping for a song.
     * If the song is not found, it will be added to the library.
     */
    public void updateLyricsMapping(String pathSong, String pathLyrics) {
        LyricsLibrary lib = readLyricsLibrary();
        boolean updated = false;

        for (SongLyricsEntry entry : lib.getSongs()) {
            if (entry.getPathSong().equals(pathSong)) {
                entry.setPathLyrics(pathLyrics);
                updated = true;
                break;
            }
        }
        if (!updated) {
            lib.getSongs().add(new SongLyricsEntry(pathSong, pathLyrics));
        }
        writeLyricsLibrary(lib);
    }

    /**
     * Reads the lyrics library from the lyrics file.
     * If the lyrics file does not exist, it will be created with an empty library.
     */
    private LyricsLibrary readLyricsLibrary() {
       Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        if (!Files.exists(lyricsFile)) return new LyricsLibrary();
        try (var reader = Files.newBufferedReader(lyricsFile)) {
            return gson.fromJson(reader, LyricsLibrary.class);
        } catch (IOException e) {
            System.err.println("An error occurred while reading the lyrics file: " + e.getMessage());
            return new LyricsLibrary();
        }
    }

    /**
     * Writes the lyrics library to the lyrics file.
     */
    private void writeLyricsLibrary(LyricsLibrary lib) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        try {
            Files.writeString(lyricsFile, gson.toJson(lib));
        } catch (IOException e) {
            System.err.println("An error occurred while writing the lyrics file: " + e.getMessage());
        }
    }

}
