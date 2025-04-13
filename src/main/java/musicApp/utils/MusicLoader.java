package musicApp.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import musicApp.exceptions.BadM3URadioException;
import musicApp.enums.SupportedFileType;
import musicApp.models.Radio;
import java.util.ArrayList;
import musicApp.models.Song;

public class MusicLoader {
    /**
     * Retrieves all valid song paths (MP3 or WAV) from the specified folder.
     * This method scans the given folder shallowly, validates that it exists and is a directory,
     * and then filters out the files to include only those with the `.mp3` or `.wav` extensions.
     * The resulting list contains the paths of the files that match these formats.
     * If the folder path is invalid (either doesn't exist or isn't a directory), an IOException is thrown.
     *
     * @param folderPath The path of the folder to scan for music files.
     * @return A list of paths to valid song files (MP3 or WAV) in the specified folder.
     * @throws IOException If the folder does not exist, is not a directory, or if an error occurs while reading files.
     */

    public List<Song> getAllSongs(Path folderPath) throws IOException {
        List<Song> songList = new ArrayList<>();
        List<Path> songPaths;
        songPaths = getAllSongPaths(folderPath);
        for (Path songPath : songPaths) {
            if (songPath.toString().endsWith(SupportedFileType.M3U.getExt())) {
                Radio newRadio = null;
                try {
                    newRadio = new Radio(songPath);
                    songList.add(newRadio);
                } catch (BadM3URadioException e) {
                    // Couldn't read the radio, so we skip it
                }
            } else {
                songList.add(new Song(songPath));
            }
        }
        return songList;
    }
    /**
     * Retrieves all valid song file paths from the specified folder.
     *
     * <p>This method checks whether the provided folder path exists and is indeed a directory. Then it filters
     * the files within the directory, only returning those with supported file formats (e.g., MP3, WAV, M3U).</p>
     *
     * @param folderPath The path of the folder to search for song files.
     * @return A list of paths to song files that are in valid formats.
     * @throws IOException If the folder path is invalid or an error occurs while reading the directory.
     */
    public List<Path> getAllSongPaths(Path folderPath) throws IOException {
        // Checks that folder exists and that path is a folder path
        if (!Files.exists(folderPath) || !Files.isDirectory(folderPath)) {
            throw new IOException("The specified path is not valid");
        }
        // Filters files by formats
        return getAllFiles(folderPath).stream()
                .filter(this::isValidFormat)
                .collect(Collectors.toList());
    }

    /**
     * Checks if the given file path represents a valid song file format (MP3, WAV, or M3U).
     *
     * <p>This method checks whether the file has a valid extension such as ".mp3", ".wav", or ".m3u".
     * It is used to filter files by their format in the folder.</p>
     *
     * @param filePath The path of the file to check.
     * @return {@code true} if the file has a valid format, otherwise {@code false}.
     */
    private boolean isValidFormat(Path filePath) {
        String fileName = filePath.getFileName().toString();
        return Arrays.stream(SupportedFileType.getExtensions()).anyMatch(fileName::endsWith);
    }

    /**
     * Retrieves all regular files (non-directory files) from the specified folder.
     *
     * <p>This method lists all files in the provided folder and returns them as a list, ignoring any subdirectories.</p>
     *
     * @param folderPath The path of the folder to list files from.
     * @return A list of paths to all regular files in the folder.
     * @throws IOException If an error occurs while reading the folder or if the folder path is invalid.
     */
    private List<Path> getAllFiles(Path folderPath) throws IOException {
        // Ignore every subfolders
        try (Stream<Path> stream = Files.list(folderPath)) {
            return stream
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new IOException("An error occurred reading files in folder: " + folderPath);
        }
    }
}
