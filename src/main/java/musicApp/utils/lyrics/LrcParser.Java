package musicApp.utils.lyrics;

import javafx.util.Duration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class to parse .lrc files into a list of KaraokeLine objects.
 */
public class LrcParser {

    /**
     * Parses the given .lrc file into a list of KaraokeLine objects.
     * Each line has format: [mm:ss.xx] lyric text
     *
     * @param lrcPath The path to the .lrc file.
     * @return A list of KaraokeLine sorted in ascending time order.
     * @throws IOException If reading the file fails.
     */
    public static List<KaraokeLine> parseLrcFile(Path lrcPath) throws IOException {
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
                // Extract minutes, seconds, fraction
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

        // in case the LRC lines are out of order
        karaokeLines.sort((a, b) -> a.getTime().compareTo(b.getTime()));

        return karaokeLines;
    }
}
