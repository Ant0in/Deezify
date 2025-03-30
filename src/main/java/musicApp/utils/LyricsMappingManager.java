package musicApp.utils;


import java.io.IOException;
import java.io.BufferedReader;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

import musicApp.models.Song;


public class LyricsMappingManager {
    private final Gson gson;
    private final Path mappingFile;
    private final Path lyricsDir;   

    public LyricsMappingManager() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.lyricsDir = ConfigPathUtil.getConfigDir();
        this.mappingFile = lyricsDir.resolve("lyrics.json");
    }

    public Path getLyricsDir() {
        return lyricsDir;
    }

    /**
     * Reads the entire lyric.json file into a LyricsLibrary object.
     * If the file doesn't exist, returns an empty library.
     */
    private  LyricsLibrary readLibrary() {
        if (!Files.exists(mappingFile)) {
            return new LyricsLibrary();
        }
        try (BufferedReader reader = Files.newBufferedReader(mappingFile)) {
            return gson.fromJson(reader, LyricsLibrary.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new LyricsLibrary();
        }
    }

    /**
     * Writes the given LyricsLibrary object to lyric.json.
     */
    private void writeLibrary(LyricsLibrary library) {
        try {
            if (!Files.exists(mappingFile.getParent())) {
                Files.createDirectories(mappingFile.getParent());
            }
            Files.writeString(mappingFile, gson.toJson(library));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Given a song name, returns the associated pathLyrics if found,
     * or null if not found.
     */
    public String getSongLyricsPath(String pathSong) {
        LyricsLibrary library = readLibrary();
        for (SongLyricsEntry entry : library.getSongs()) {
            if (entry.getPathSong().equals(pathSong)) {
                return entry.getPathLyrics();
            }
        }
        return null; 

    }

    /**
     * Given a song path, returns the lyrics as a list of strings.
     * If the lyrics file doesn't exist, returns a list with an error message.
     */
    public List<String> getSongLyrics(String pathSong) {
        // get the last part of the song path (ex: "Hello.mp3")
        String pathLyrics = getSongLyricsPath(pathSong);
        if (pathLyrics == null) {
            return List.of();
        }
        Path filePath = getLyricsDir().resolve(pathLyrics);
        if (!Files.exists(filePath)) {
            return List.of();
        }
        try {
            return Files.readAllLines(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return java.util.List.of("Error reading lyrics file: " + e.getMessage());
        }
    }

    /**
     * Updates or creates a mapping between pathSong and newRelativePath (path to lyrics).
     * If it doesn't exist, it creates a new entry.
     */

    public void updateLyricsMapping(String pathSong, String newRelativePath) {
        LyricsLibrary library = readLibrary();
        boolean found = false;

        for (SongLyricsEntry entry : library.getSongs()) {
            if (entry.getPathSong().equals(pathSong)) {
                entry.setPathLyrics(newRelativePath);
                found = true;
                break;
            }
        }
            if (!found) {
                // Ex: "songs/Hello.mp3"
                SongLyricsEntry newEntry = new SongLyricsEntry(pathSong, newRelativePath);
                library.getSongs().add(newEntry);
            }
            writeLibrary(library);
    }
    
    
    /**
     * Generates a unique key for the song based on its title, artist, and duration.
     * Used to map the song to its lyrics.
     */
    public String getPathSong(Song song) {
        return song.getFilePath().toAbsolutePath().toString();
    }
}


