package musicApp.utils;

import musicApp.models.Song;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class LyricsManager {
    private final DataProvider dataProvider;

    public LyricsManager(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    /**
     * Returns the lyrics of the given song.
     * If no lyrics are found, returns an empty list.
     */
    public List<String> getLyrics(Song song) {
        String pathLyrics = dataProvider.getLyricsPath(song.getFilePath().toString());
        if (pathLyrics == null) {
            return List.of();
        }
        Path lyricsFile = dataProvider.getLyricsDir().resolve(pathLyrics);
        if (!Files.exists(lyricsFile)) {
            return List.of();
        }
        try {
            return Files.readAllLines(lyricsFile);
        } catch (IOException e) {
            return List.of("Error reading lyrics file: " + e.getMessage());
        }
    }

    /**
     * Saves the provided lyrics to a file.
     * The file name is derived from the song name.
     * Updates the mapping in the data provider.
     */
    public void saveLyrics(Song song, String lyricsContent) {
        String songName = song.getFilePath().getFileName().toString();
        String lyricsFileName = getLyricsFileName(songName);
        if (lyricsFileName == null) {
            System.err.println("Unsupported format.");
            return;
        }

        Path lyricsFilePath = dataProvider.getLyricsDir().resolve(lyricsFileName);
        try {
            Files.writeString(lyricsFilePath, lyricsContent);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        dataProvider.updateLyricsMapping(song.getFilePath().toString(), lyricsFileName);
    }

    /**
     * Returns the lyrics file name based on the song name.
     * If the song name does not end with .mp3 or .wav, returns null.
     */
    private String getLyricsFileName(String songName) {
        if (songName.endsWith(".mp3")) return songName.replace(".mp3", ".txt");
        if (songName.endsWith(".wav")) return songName.replace(".wav", ".txt");
        System.err.println("Unsupported format: " + songName);
        return null;
    }
}
