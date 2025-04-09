package musicApp.utils.lyrics;

import musicApp.models.Song;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.stream.Collectors;
import java.util.List;

import musicApp.utils.lyrics.KaraokeLine;
import musicApp.utils.lyrics.LrcParser;

public class LyricsManager {

    private final LyricsDataAccess lyricsDataAccess;

    public LyricsManager(LyricsDataAccess lyricsDataAccess) {
        this.lyricsDataAccess = lyricsDataAccess;
    }

    /**
     * Returns the lyrics of the given song.
     * If no lyrics are found, returns an empty list.
     */
    public List<String> getLyrics(Song song) {
        String pathLyrics = lyricsDataAccess.getLyricsPath(song.getFilePath().toString());
        if (pathLyrics == null) {
            return List.of();
        }
        Path lyricsFile = lyricsDataAccess.getLyricsDir().resolve(pathLyrics);
        if (!Files.exists(lyricsFile)) {
            return List.of();
        }
        try {
            return Files.readAllLines(lyricsFile);
        } catch (IOException e) {
            System.err.println("Error reading lyrics file: " + e.getMessage());
            return List.of();
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

        Path lyricsFilePath = lyricsDataAccess.getLyricsDir().resolve(lyricsFileName);
        try {
            Files.writeString(lyricsFilePath, lyricsContent);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        lyricsDataAccess.updateLyricsMappingTxt(song.getFilePath().toString(), lyricsFileName);
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

    public void LrcToTxt(Song song) {
        List<KaraokeLine> lines = getKaraokeLines(song);

        String plainText = lines.stream()
                .map(KaraokeLine::getLyric)
                .filter(s -> !s.isBlank())
                .collect(Collectors.joining("\n"));

        saveLyrics(song, plainText);
    }

    public void importLrc(Song song, Path sourceLrcFile, boolean overwriteTxt) {
        if (song == null || sourceLrcFile == null) {
            System.err.println("Song or LRC file is null. Cannot import.");
            return;
        }
        String fileName = sourceLrcFile.getFileName().toString();
        Path destination = lyricsDataAccess.getLyricsDir().resolve(fileName);
        try {
            Files.copy(sourceLrcFile, destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.println("Error copying LRC file: " + e.getMessage());
            return;
        }

        lyricsDataAccess.updateLyricsMappingKaraoke(song.getFilePath().toString(), fileName);

        boolean txtExists = txtLyricsExists(song);
        boolean shouldExtractTxt = overwriteTxt || !txtExists;
        if (shouldExtractTxt) {
            LrcToTxt(song);
        }
}


    public List<KaraokeLine> getKaraokeLines(Song song) {
        String lrcFileName = lyricsDataAccess.getKaraokeLyricsPath(song.getFilePath().toString());
        if (lrcFileName == null) return List.of();

        Path lrcPath = lyricsDataAccess.getLyricsDir().resolve(lrcFileName);
        if (!Files.exists(lrcPath)) return List.of();

        try {
            return LrcParser.parseLrcFile(lrcPath);
        } catch (IOException e) {
            System.err.println("Error parsing LRC: " + e.getMessage());
            return List.of();
        }
    }

    public boolean txtLyricsExists(Song song) {
        String txtPath = lyricsDataAccess.getLyricsPathTxt(song.getFilePath().toString());
        if (txtPath == null) return false;
        Path file = lyricsDataAccess.getLyricsDir().resolve(txtPath);
        return Files.exists(file);
    }

    public String getTxtLyricsPath(Song song) {
        return lyricsDataAccess.getLyricsPathTxt(song.getFilePath().toString());
    }

    public Path getLyricsFile(String relativePath) {
        return lyricsDataAccess.getLyricsDir().resolve(relativePath);
    }

}
