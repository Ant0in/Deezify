package musicApp.utils.lyrics;

import javafx.util.Duration;
import musicApp.exceptions.LyricsNotFoundException;
import musicApp.exceptions.LyricsOperationException;
import musicApp.models.KaraokeLine;
import musicApp.models.Song;
import musicApp.models.SongLyricsEntry;
import musicApp.utils.DataProvider;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Service class for managing song lyrics.
 * Handles loading lyrics content and creating SongLyricsEntry objects.
 */
public class LyricsService {

    private final LyricsRepository repository;

    public LyricsService() {
        repository = new LyricsRepository(new DataProvider());
    }

    /**
     * Gets a SongLyricsEntry containing the lyrics content for a song.
     * @param song The song to get lyrics for
     * @return A SongLyricsEntry with the song's lyrics content
     */
    public SongLyricsEntry getLyricsEntry(Song song) throws LyricsNotFoundException {
        if (song == null) {
            return new SongLyricsEntry();
        }
        
        String songPath = song.getFilePath().toString();
        LyricsRepository.LyricsFilePaths paths = repository.getLyricsPaths(songPath)
                .orElseThrow(() -> new LyricsNotFoundException("Lyrics not found for song: " + songPath));

        if (paths == null) {
            return new SongLyricsEntry();
        }
        
        List<String> textLyrics = loadTextLyrics(paths.getTextPath());
        List<KaraokeLine> karaokeLyrics = loadKaraokeLyrics(paths.getKaraokePath());
        
        return new SongLyricsEntry(textLyrics, karaokeLyrics);
    }

    /**
     * Loads text lyrics from a file
     * @param relativePath Path to the lyrics file, relative to the lyrics directory
     * @return List of lyrics lines, or empty list if file doesn't exist or can't be read
     */
    private List<String> loadTextLyrics(String relativePath) {
        if (relativePath == null) {
            return Collections.emptyList();
        }
        
        Path lyricsFile = repository.getLyricsDir().resolve(relativePath);
        if (!Files.exists(lyricsFile)) {
            return Collections.emptyList();
        }
        
        try {
            return Files.readAllLines(lyricsFile);
        } catch (IOException e) {
            System.err.println("Error reading lyrics file: " + e.getMessage());
            return Collections.emptyList();
        }
    }
    
    /**
     * Loads karaoke lyrics from an LRC file
     * @param relativePath Path to the LRC file, relative to the lyrics directory
     * @return List of KaraokeLine objects, or empty list if file doesn't exist or can't be parsed
     */
    private List<KaraokeLine> loadKaraokeLyrics(String relativePath) {
        if (relativePath == null) {
            return Collections.emptyList();
        }
        
        Path lrcPath = repository.getLyricsDir().resolve(relativePath);
        if (!Files.exists(lrcPath)) {
            return Collections.emptyList();
        }
        
        try {
            return parseLrcFile(lrcPath);
        } catch (IOException e) {
            System.err.println("Error parsing LRC file: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Saves text lyrics for a song
     * @param song The song to save lyrics for
     * @param lyricsContent The lyrics content to save
     * @throws IOException If there is an error writing to the file
     * @throws NullPointerException If song or lyrics content is null
     * @throws LyricsOperationException If the repository update fails
     */
    public void saveLyrics(Song song, String lyricsContent) throws IOException, NullPointerException, LyricsOperationException {
        if (song == null || lyricsContent == null) {
            throw new NullPointerException("Song or lyrics content cannot be null");
        }
        
        String songName = song.getFilePath().getFileName().toString();
        String lyricsFileName = getLyricsFileName(songName);
        if (lyricsFileName == null) {
            throw new LyricsOperationException("Unsupported file format: " + songName);
        }

        Path lyricsFilePath = repository.getLyricsDir().resolve(lyricsFileName);
        Files.writeString(lyricsFilePath, lyricsContent);

        // Update the repository with the new file path
        boolean updated = repository.updateTextLyricsPath(song.getFilePath().toString(), lyricsFileName);
        if (!updated) {
            throw new LyricsOperationException("Failed to update lyrics path in repository");
        }
        
        // Update the song's lyrics entry with the new content
        List<String> lines = lyricsContent.lines().collect(Collectors.toList());
        try {
            song.setLyricsEntry(new SongLyricsEntry(lines, song.getKaraokeLines()));
        } catch (LyricsNotFoundException e) {
            throw new LyricsOperationException("Error setting lyrics entry: " + e.getMessage(), e);
        }
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
     * Extracts plain text from karaoke lyrics and saves it
     * @param song The song to extract lyrics for
     * @throws LyricsNotFoundException If lyrics are not found
     * @throws NullPointerException If song is null
     * @throws IOException If there is an error writing to the file
     * @throws LyricsOperationException If any lyrics operation fails
     */
    public void lrcToTxt(Song song) throws LyricsNotFoundException, NullPointerException, IOException, LyricsOperationException {
        if (song == null) throw new NullPointerException("Song is null");
        
        SongLyricsEntry entry = song.getLyricsEntry().orElseThrow(() -> new LyricsNotFoundException("No lyrics entry found"));

        String plainText = entry.getKaraokeLines().stream()
                .map(KaraokeLine::getLyric)
                .filter(s -> !s.isBlank())
                .collect(Collectors.joining("\n"));

        saveLyrics(song, plainText);
    }

    /**
     * Imports a .lrc file and updates the lyrics mapping.
     * If extractText is true, it will also extract the text lyrics from the LRC.
     * @throws IOException If there is an error reading or writing files
     * @throws NullPointerException If song or LRC file is null
     * @throws LyricsOperationException If any lyrics operation fails
     */
    public void importLrc(Song song, Path sourceLrcFile)
            throws IOException, NullPointerException, LyricsOperationException, LyricsNotFoundException {
        if (song == null || sourceLrcFile == null) {
            throw new NullPointerException("Song or LRC file is null. Cannot import.");
        }

        boolean extractText = song.getLyricsEntry().map(SongLyricsEntry::hasTextLyrics).orElse(false);
        
        String fileName = sourceLrcFile.getFileName().toString();
        Path destination = repository.getLyricsDir().resolve(fileName);
        
        Files.copy(sourceLrcFile, destination, StandardCopyOption.REPLACE_EXISTING);

        // Update the repository with the new karaoke file path
        boolean updated = repository.updateKaraokeLyricsPath(song.getFilePath().toString(), fileName);
        if (!updated) {
            throw new LyricsOperationException("Failed to update karaoke lyrics path in repository");
        }
        
        // Load the karaoke lines
        List<KaraokeLine> karaokeLines = loadKaraokeLyrics(fileName);
        
        // Update the song's lyrics entry with the new karaoke content
        SongLyricsEntry currentEntry = song.getLyricsEntry().orElseThrow(() -> new LyricsNotFoundException("No lyrics entry found"));
        song.setLyricsEntry(new SongLyricsEntry(
                currentEntry != null ? currentEntry.getLyrics() : Collections.emptyList(),
                karaokeLines
        ));
        
        // Extract text if needed
        if (extractText || (currentEntry != null && !currentEntry.hasTextLyrics())) {
            lrcToTxt(song);
        }
    }

    /**
     * Parses the given .lrc file into a list of KaraokeLine objects.
     * Each line has format: [mm:ss.xx] lyric text
     *
     * @param lrcPath The path to the .lrc file.
     * @return A list of KaraokeLine sorted in ascending time order.
     * @throws IOException If reading the file fails.
     */
    public List<KaraokeLine> parseLrcFile(Path lrcPath) throws IOException {
        List<String> lines = Files.readAllLines(lrcPath);
        List<KaraokeLine> karaokeLines = new ArrayList<>();

        // Regex example: [00:12.34] Hello world
        //   group(1) = minutes
        //   group(2) = seconds
        //   group(3) = .xx  (can be null if fraction not present)
        //   group(4) = the lyric text
        Pattern pattern = Pattern.compile("\\[(\\d{2}):(\\d{2})(\\.\\d{1,2})?\\]\\s*(.*)");

        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                int minutes = Integer.parseInt(matcher.group(1));
                int seconds = Integer.parseInt(matcher.group(2));
                double fraction = 0.0;
                if (matcher.group(3) != null) {
                    // e.g. ".34" -> 0.34
                    fraction = Double.parseDouble(matcher.group(3));
                }
                double totalSeconds = minutes * 60 + seconds + fraction;

                String lyricText = matcher.group(4).trim();
                KaraokeLine kl = new KaraokeLine(Duration.seconds(totalSeconds), lyricText);
                karaokeLines.add(kl);
            }
        }
        karaokeLines.sort((a, b) -> a.getTime().compareTo(b.getTime()));
        return karaokeLines;
    }
}
