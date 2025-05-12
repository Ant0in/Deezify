package musicApp.services;

import javafx.util.Duration;
import musicApp.exceptions.LyricsNotFoundException;
import musicApp.exceptions.LyricsOperationException;
import musicApp.exceptions.SettingsFilesException;
import musicApp.models.KaraokeLine;
import musicApp.models.Song;
import musicApp.models.SongLyrics;
import musicApp.repositories.JsonRepository;
import musicApp.repositories.LyricsRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Service class for managing song lyrics.
 * Handles loading lyrics content and creating SongLyricsEntry objects.
 */
public class LyricsService {

    private final LyricsRepository repository;

    public LyricsService() throws SettingsFilesException {
        repository = new LyricsRepository(new JsonRepository());
    }

    /**
     * Gets a SongLyricsEntry containing the lyrics content for a song.
     * @param song The song to get lyrics for
     * @return A SongLyricsEntry with the song's lyrics content
     */
    public SongLyrics getLyricsEntry(Song song) throws LyricsNotFoundException {
        if (song == null) return new SongLyrics();
        String songPath = song.getFilePath().toString();
        LyricsRepository.LyricsFilePaths paths = repository.getLyricsPaths(songPath)
                .orElseThrow(() -> new LyricsNotFoundException("Lyrics not found for song: " + songPath));

        if (paths == null) return new SongLyrics();

        List<String> textLyrics = loadTextLyrics(paths.getTextPath());
        List<KaraokeLine> karaokeLyrics = loadKaraokeLyrics(paths.getKaraokePath());
        
        return new SongLyrics(textLyrics, karaokeLyrics);
    }

    /**
     * Loads text lyrics from a file
     * @param relativePath Path to the lyrics file, relative to the lyrics folder
     * @return List of lyrics lines, or empty list if file doesn't exist or can't be read
     */
    private List<String> loadTextLyrics(String relativePath) {
        if (!isValidLyricsPath(relativePath)) {
            return Collections.emptyList();
        }

        Path lyricsFile = repository.getLyricsDir().resolve(relativePath);

        try {
            return Files.readAllLines(lyricsFile);
        } catch (IOException e) {
            System.err.println("Error reading lyrics file: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Loads karaoke lyrics from an LRC file
     * @param relativePath Path to the LRC file, relative to the lyrics folder
     * @return List of KaraokeLine objects, or empty list if file doesn't exist or can't be parsed
     */
    private List<KaraokeLine> loadKaraokeLyrics(String relativePath) {
        if (!isValidLyricsPath(relativePath)) {
            return Collections.emptyList();
        }
        Path lrcPath = repository.getLyricsDir().resolve(relativePath);
        try {
            return parseLrcFile(lrcPath);
        } catch (IOException e) {
            System.err.println("Error parsing LRC file: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Checks if the given relative path is valid and exists in the lyrics folder
     * @param relativePath The relative path to check
     * @return true if the path is valid and exists, false otherwise
     */
    private boolean isValidLyricsPath(String relativePath) {
        if (relativePath == null) {
            return false;
        }

        Path lyricsFile = repository.getLyricsDir().resolve(relativePath);
        return Files.exists(lyricsFile);
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
        if (song == null || lyricsContent == null) throw new NullPointerException("Song or lyrics content cannot be null");

        String songName = song.getFilePath().getFileName().toString();
        String lyricsFileName = getLyricsFileName(songName);
        if (lyricsFileName == null) throw new LyricsOperationException("Unsupported file format: " + songName);

        Path lyricsFilePath = repository.getLyricsDir().resolve(lyricsFileName);
        Files.writeString(lyricsFilePath, lyricsContent);

        repository.updateTextLyricsPath(song.getFilePath().toString(), lyricsFileName);

        List<String> lines = lyricsContent.lines().collect(Collectors.toList());
        try {
            song.setLyricsEntry(new SongLyrics(lines, song.getKaraokeLines()));
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
        
        SongLyrics entry = song.getLyricsEntry().orElseThrow(() -> new LyricsNotFoundException("No lyrics entry found"));

        String plainText = entry.getKaraokeLines().stream()
                .map(KaraokeLine::getLyric)
                .filter(s -> !s.isBlank())
                .collect(Collectors.joining("\n"));

        saveLyrics(song, plainText);
    }

    /**
     * Imports a .lrc file and updates the lyrics mapping.
     * @throws IOException If there is an error reading or writing files
     * @throws NullPointerException If song or LRC file is null
     * @throws LyricsOperationException If any lyrics operation fails
     */
    public void importLrc(Song song, Path sourceLrcFile, Boolean overwriteTxt)
            throws IOException, NullPointerException, LyricsOperationException, LyricsNotFoundException {
        if (song == null || sourceLrcFile == null) throw new NullPointerException("Song or LRC file is null. Cannot import.");

        String fileName = sourceLrcFile.getFileName().toString();
        Path destination = repository.getLyricsDir().resolve(fileName);
        
        Files.copy(sourceLrcFile, destination, StandardCopyOption.REPLACE_EXISTING);

        repository.updateKaraokeLyricsPath(song.getFilePath().toString(), fileName);

        List<KaraokeLine> karaokeLines = loadKaraokeLyrics(fileName);
        
        SongLyrics currentEntry = song.getLyricsEntry().orElseThrow(() -> new LyricsNotFoundException("No lyrics entry found"));
        song.setLyricsEntry(new SongLyrics(
                currentEntry != null ? currentEntry.getLyrics() : Collections.emptyList(),
                karaokeLines
        ));
        
        if (overwriteTxt || (currentEntry != null && !currentEntry.hasTextLyrics())) {
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
    private List<KaraokeLine> parseLrcFile(Path lrcPath) throws IOException {
        List<String> lines = Files.readAllLines(lrcPath);
        List<KaraokeLine> karaokeLines = new ArrayList<>();

        Pattern pattern = Pattern.compile("\\[(\\d{2}):(\\d{2})(\\.\\d{1,2})?\\]\\s*(.*)");

        lines.forEach(line -> parseKaraokeLine(pattern, line).ifPresent(karaokeLines::add));
        karaokeLines.sort(Comparator.comparing(KaraokeLine::getTime));
        return karaokeLines;
    }

    /**
     * Parses a single line of karaoke lyrics.
     * @param pattern The regex pattern to match the line.
     * @param line The line to parse.
     * @return An Optional containing the KaraokeLine if matched, otherwise empty.
     */
    private Optional<KaraokeLine> parseKaraokeLine(Pattern pattern, String line) {
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
            return Optional.of(kl);
        }
        return Optional.empty();
    }

    public boolean txtExists(Song currentSong) {
        Optional<LyricsRepository.LyricsFilePaths> paths = repository.getLyricsPaths(currentSong.getFilePath().toString());
        if (paths.isPresent()) {
            String textPath = paths.get().getTextPath();
            return textPath != null && Files.exists(repository.getLyricsDir().resolve(textPath));
        }
        return false;
    }
}
