package musicApp.utils.lyrics;

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
    
    /**
     * Imports a .lrc file for the given song.
     * 1) Copies the .lrc to your lyrics folder,
     * 2) Updates the LRC mapping in lyrics.json,
     * 3) (Optional) Overwrites the .txt with the extracted lines.
     *
     * @param song          The Song object for which we import LRC.
     * @param sourceLrcFile The path of the LRC file selected by the user.
     * @param overwriteTxt  If true, we extract the text from LRC lines and overwrite the .txt file.
     */
    public void importLrc(Song song, Path sourceLrcFile, boolean overwriteTxt) {
        if (song == null || sourceLrcFile == null) {
            System.err.println("Song or LRC file is null. Cannot import.");
            return;
        }

        // 1) Copy .lrc to your local lyrics folder
        String fileName = sourceLrcFile.getFileName().toString();
        Path destination = lyricsDataAccess.getLyricsDir().resolve(fileName);

        try {
            Files.copy(sourceLrcFile, destination, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("LRC file copied to " + destination);
        } catch (IOException e) {
            System.err.println("Error copying LRC file: " + e.getMessage());
            return;
        }

        // 2) Update the LRC mapping in lyrics.json
        lyricsDataAccess.updateLrcMapping(song.getFilePath().toString(), fileName);

        // 3) If the user wants to overwrite .txt with the text from the LRC
        if (overwriteTxt) {
            try {
                // Parse the .lrc
                List<KaraokeLine> lines = LrcParser.parseLrcFile(destination);

                // Extract just the text (without timestamps)
                String plainText = lines.stream()
                    .map(KaraokeLine::getText)
                    .collect(Collectors.joining("\n"));

                // Then save it as .txt
                saveLyrics(song, plainText); // Reuses your existing method for normal lyrics
                System.out.println(".txt file overwritten with LRC content for " + song.getTitle());
            } catch (IOException e) {
                System.err.println("Error parsing LRC file: " + e.getMessage());
            }
        }
    }
}
