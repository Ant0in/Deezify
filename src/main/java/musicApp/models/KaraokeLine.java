package musicApp.models;

import javafx.util.Duration;

/**
 * Represents a single line in a karaoke file (LRC).
 * It contains the timestamp and the actual lyric text.
 */
public class KaraokeLine {

    private Duration timestamp;
    private String lyric;

    /**
     * Constructs a KaraokeLine with the given timestamp and lyric.
     */
    public KaraokeLine(Duration timestamp, String lyric) throws IllegalArgumentException {
        setTimestamp(timestamp);
        setLyric(lyric);
    }

    private void setTimestamp(Duration _timestamp) throws IllegalArgumentException {
        if (_timestamp == null || _timestamp.lessThan(Duration.ZERO)) {
            throw new IllegalArgumentException("Timestamp cannot be null or negative.");
        }
        timestamp = _timestamp;
    }

    private void setLyric(String _lyric) throws IllegalArgumentException {
        if (_lyric == null) {
            throw new IllegalArgumentException("Lyric cannot be null.");
        }
        lyric = _lyric;
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
