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
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();


    private static Path getMappingFile() {
        return getLyricsDir().resolve("lyrics.json");
    }
    /**
     * Reads the entire lyric.json file into a LyricsLibrary object.
     * If the file doesn't exist, returns an empty library.
     */
    private static LyricsLibrary readLibrary() {
        Path mappingFile = getMappingFile();
        if (!Files.exists(mappingFile)) {
            return new LyricsLibrary();
        }
        try (BufferedReader reader = Files.newBufferedReader(mappingFile)) {
            return GSON.fromJson(reader, LyricsLibrary.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new LyricsLibrary();
        }
    }

    /**
     * Writes the given LyricsLibrary object to lyric.json.
     */
    private static void writeLibrary(LyricsLibrary library) {
        Path mappingFile = getMappingFile();
        try {
            // Make sure directory exists
            if (!Files.exists(mappingFile.getParent())) {
                Files.createDirectories(mappingFile.getParent());
            }
            Files.writeString(mappingFile, GSON.toJson(library));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Path getLyricsDir() {
        return ConfigPathUtil.getConfigDir();
    }

    /**
     * Given a song name, returns the associated pathLyrics if found,
     * or null if not found.
     */
    public static String getSongLyricsPath(String songKey) {
        LyricsLibrary library = readLibrary();
        for (SongLyricsEntry entry : library.getSongs()) {
            if (entry.getPathSong().equals(songKey)) {
                return entry.getPathLyrics();
            }
        }
        return null; 

    }

    /**
     * Updates or creates a mapping between songKey and newRelativePath (path to lyrics).
     * If it doesn't exist, it creates a new entry.
     */

    public static void updateLyricsMapping(String songKey, String newRelativePath) {
        LyricsLibrary library = readLibrary();
        boolean found = false;

        for (SongLyricsEntry entry : library.getSongs()) {
            if (entry.getPathSong().equals(songKey)) {
                entry.setPathLyrics(newRelativePath);
                found = true;
                break;
            }
        }
            if (!found) {
                // Ex: "songs/Hello.mp3"
                SongLyricsEntry newEntry = new SongLyricsEntry(songKey, newRelativePath);
                library.getSongs().add(newEntry);
            }

            writeLibrary(library);
    }
    
    /**
     * Given a song path, returns the lyrics as a list of strings.
     * If the lyrics file doesn't exist, returns a list with an error message.
     */
    public static List<String> getSongLyrics(String songKey) {
        // get the last part of the song path (ex: "Hello.mp3")
        String pathLyrics = getSongLyricsPath(songKey);
        if (pathLyrics == null) {
            return null;
        }
        Path filePath = getLyricsDir().resolve(pathLyrics);
        if (!Files.exists(filePath)) {
            return java.util.List.of("Lyrics file not found: " + filePath.toAbsolutePath());
        }
        try {
            return Files.readAllLines(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return java.util.List.of("Error reading lyrics file: " + e.getMessage());
        }
    }
    
    /**
     * Generates a unique key for the song based on its title, artist, and duration.
     * Used to map the song to its lyrics.
     */
    public static String getSongKey(Song song) {
        // get the title, artist, and duration of the song
        String title = song.getTitle(); 
        String artist = song.getArtist();
        int durationSec = (int) song.getDuration().toSeconds();

        // concatenate the metadata into a unique key
        return title + "-" + artist + "-" + durationSec;
    }
}


