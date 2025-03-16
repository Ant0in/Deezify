package musicApp.utils;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class RadioLoader {

    public List<Path> loadM3UFiles(Path directory) throws IOException {
        List<Path> m3uFiles = new ArrayList<>();

        if (!Files.isDirectory(directory)) {
            return m3uFiles;
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory, "*.m3u")) {
            for (Path path : stream) {
                if (Files.isRegularFile(path)) {
                    m3uFiles.add(path);
                }
            }
        }

        return m3uFiles;
    }

    public List<String> parseM3U(Path m3uFile) {
        List<String> urls = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(m3uFile);
            for (String line : lines) {
                line = line.trim();
                if (!line.isEmpty() && !line.startsWith("#")) {
                    urls.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Impossible de lire " + m3uFile + " : " + e.getMessage());
        }
        return urls;
    }
}
