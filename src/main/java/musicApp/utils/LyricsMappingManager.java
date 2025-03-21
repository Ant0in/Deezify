package musicApp.utils;

import java.util.Map;
import java.util.List;
import java.io.IOException;
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
import java.io.BufferedReader;


public class LyricsMappingManager {

    public static final String LYRICS_DIR = "data/lyrics";
    private String pathLyrics; 

    public LyricsMappingManager(String songPath) {
        this.pathLyrics = findLyricsPath(songPath);
    }

    /**
     * Find the path to the lyrics file from the song path.
     */
    private String findLyricsPath(String songPath) {
        String songName = Paths.get(songPath).getFileName().toString();
        Map<String, String> mapping = LyricsMappingManager.loadLyricsMapping();
        String lyricsPath = mapping.get(songName);
        if (lyricsPath != null) {
            System.out.println("Mapping found for " + songName + ": " + lyricsPath);
        } else {
            System.out.println("No mapping found for " + songName);
        }
        return lyricsPath;
    }

    /**
     * Load the lyrics from the lyrics file.
     * @return The lyrics of the song.
     */
    public List<String> getLyrics() {
        if (pathLyrics == null) {
            return List.of();
        }
        try {
            Path filePath = Paths.get(LyricsMappingManager.LYRICS_DIR, pathLyrics);
            if (!Files.exists(filePath)) {
                return List.of("Lyrics file not found: " + filePath.toAbsolutePath());
            }
            return Files.readAllLines(filePath);
        } catch (IOException e) {
            System.err.println("getLyrics: IOException - " + e.getMessage());
            return List.of();
        }
    }

    public static List<String> getSongLyrics(String songPath) {
        LyricsMappingManager lyricsmappingmanager = new LyricsMappingManager(songPath);
        return lyricsmappingmanager.getLyrics();
    }
    
    public static String getSongLyricsPath(String songPath) {
        LyricsMappingManager lyricsmappingmanager = new LyricsMappingManager(songPath);
        return lyricsmappingmanager.findLyricsPath(songPath);
    }

    /**
     * Load the lyrics mapping from the lyrics file, by reading the JSON file and parsing it. 
     *
     * @return The mapping of song names to lyrics paths.
     */
    public static Map<String, String> loadLyricsMapping() {
        Map<String, String> mapping = new HashMap<>();
        Path mappingFile = Paths.get(LYRICS_DIR, "lyric.json");
        if (!Files.exists(mappingFile)) {
            System.err.println("Mapping file not found at: " + mappingFile.toAbsolutePath());
            return mapping;
        }
        try (BufferedReader reader = Files.newBufferedReader(mappingFile)) {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, List<Map<String, String>>>>(){}.getType(); 
            Map<String, List<Map<String, String>>> jsonMap = gson.fromJson(reader, type);
            List<Map<String, String>> songsList = jsonMap.get("songs"); 
            if (songsList != null) {
                for (Map<String, String> entry : songsList) {
                    String pathSong = entry.get("pathSong");
                    String pathLyrics = entry.get("pathLyrics");
                    String songName = Paths.get(pathSong).getFileName().toString();
                    mapping.put(songName, pathLyrics);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mapping;
    }

    /**
     * Update the lyrics mapping with the new lyrics path for the song.
     * If the song is not found in the mapping, a new entry is created.
     */

    public static void updateLyricsMapping(String songName, String newRelativePath) {
        Path mappingFile = Paths.get(LYRICS_DIR, "lyric.json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Map<String, Object> jsonMap = new HashMap<>();

        try {
            if (Files.exists(mappingFile)) {
                jsonMap = gson.fromJson(Files.newBufferedReader(mappingFile), new TypeToken<Map<String, Object>>(){}.getType());
            } else {
                jsonMap.put("songs", new ArrayList<Map<String, String>>());
            }
            List<Map<String, String>> songsList = (List<Map<String, String>>) jsonMap.get("songs");
            //System.out.println("before update, mapping: " + gson.toJson(jsonMap));
            //System.out.println("updating mapping for song: " + songName + " with new lyrics path: " + newRelativePath);

            boolean found = false;
            for (Map<String, String> entry : songsList) {
                String existingSongName = Paths.get(entry.get("pathSong")).getFileName().toString();
                if (existingSongName.equals(songName)) {
                    entry.put("pathLyrics", newRelativePath);
                    found = true;
                    break;
                }
            }
            if (!found) {
                Map<String, String> newEntry = new HashMap<>();
                newEntry.put("pathSong", "songs/" + songName);
                newEntry.put("pathLyrics", newRelativePath);
                songsList.add(newEntry);
            }
            //System.out.println("After update, mapping: " + gson.toJson(jsonMap));
            Files.writeString(mappingFile, gson.toJson(jsonMap));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


