package musicApp.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileManager {

    /**
     * Copies the given file to the target directory.
     * If a file with the same name already exists, an IOException is thrown.
     *
     * @param sourceFile      The file to copy.
     * @param targetDirectory The directory where the file should be copied.
     * @return The path to the newly copied file.
     * @throws IOException If the target directory is invalid, the file already exists, or another I/O error occurs.
     */
    public static Path copyFileToDirectory(File sourceFile, Path targetDirectory) throws IOException {
        if (!Files.exists(targetDirectory) || !Files.isDirectory(targetDirectory)) {
            throw new IOException("Target directory is invalid: " + targetDirectory);
        }

        Path targetPath = targetDirectory.resolve(sourceFile.getName());

        if (Files.exists(targetPath)) {
            throw new IOException("A file with the name \"" + sourceFile.getName() + "\" already exists in the target directory.");
        }

        return Files.copy(sourceFile.toPath(), targetPath);
    }
}
