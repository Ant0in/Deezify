package MusicApp.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    static public List<Path> getAllSongPaths(Path folderPath) throws IOException {
        // Checks that folder exists and that path is a folder path
        if (!Files.exists(folderPath) || !Files.isDirectory(folderPath)) {
            throw new IOException("The specified path is not valid");
        }
        // Filters files by formats
        return getAllFiles(folderPath).stream()
                .filter(MusicLoader::isValidFormat)
                .collect(Collectors.toList());
    }

    static private boolean isValidFormat(Path filePath) {
        String fileName = filePath.getFileName().toString();
        return fileName.endsWith(".mp3") || fileName.endsWith(".wav");
    }

    static private List<Path> getAllFiles(Path folderPath) throws IOException {
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
