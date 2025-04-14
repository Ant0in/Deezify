package musicApp.modelsTest;

import javafx.util.Duration;
import musicApp.models.KaraokeLine;
import org.junit.Test;

import static org.junit.Assert.assertThrows;

public class TestKaraokeLine {
    @Test
    public void testNegativeTimestamp() {
        assertThrows(IllegalArgumentException.class, () -> {
            new KaraokeLine(Duration.millis(-1), "Test lyric");
        });
    }

    @Test
    public void testNullLyric() {
        assertThrows(IllegalArgumentException.class, () -> {
            new KaraokeLine(Duration.millis(1000), null);
        });
    }
}
