package musicApp.utils.lyrics;

import javafx.util.Duration;

/**
 * Represents a single line in a karaoke file (LRC).
 * It contains the timestamp and the actual lyric text.
 */
public class KaraokeLine {

    private final Duration timestamp;
    private final String lyric;

    /**
     * Constructs a KaraokeLine with the given timestamp and lyric.
     */
    public KaraokeLine(Duration timestamp, String lyric) {
        this.timestamp = timestamp;
        this.lyric = lyric;
    }

    /**
     * @return The timestamp for this line (javafx.util.Duration).
     */
    public Duration getTime() {
        return timestamp;
    }

    /**
     * @return The lyric text for this line.
     */
    public String getLyric() {
        return lyric;
    }
}
