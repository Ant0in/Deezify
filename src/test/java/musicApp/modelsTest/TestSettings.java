package musicApp.modelsTest;

import musicApp.models.Settings;
import musicApp.utils.DataProvider;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;

public class TestSettings {
    private DataProvider dataProvider = new DataProvider();

    @Test
    public void testStringParser() throws IOException {
        Settings defaultSettings = new Settings(0.0, dataProvider.getDefaultMusicFolder());
        Path tempFolder = Files.createTempDirectory("testFolder");

        String[] valid = new String[]{
                "balance=1.0",
                "musicFolder=" + tempFolder.toString(),
                "balance=-1.0\nmusicFolder=" + tempFolder.toString(),
                "balance=1",
                "balance=9.0\nbalance=-1.0",
                "musicFolder=fdqdfqsdf\nmusicFolder=" + tempFolder.toString(),
        };

        Settings[] validSettings = new Settings[]{
                new Settings(1.0, dataProvider.getDefaultMusicFolder()),
                new Settings(0.0, tempFolder),
                new Settings(-1.0, tempFolder),
                new Settings(1.0, dataProvider.getDefaultMusicFolder()),
                new Settings(-1.0, dataProvider.getDefaultMusicFolder()),
                new Settings(0.0, tempFolder),
        };

        for (int i = 0; i < valid.length; i++) {
            assertEquals(validSettings[i], new Settings(valid[i]));
        }

        String[] invalid = new String[]{
                "falseWord=0.0",
                "balance=1.0\nfalseWord=/path/to/music/folder",
                "balance=3.0",
                "musicFolder=302",
                "balance=oui",
                "balance=-3",
                "",
                null,
                "\n\n\n\n",
        };

        Settings[] invalidSettings = new Settings[]{
                defaultSettings,
                new Settings(1.0, dataProvider.getDefaultMusicFolder()),
                defaultSettings,
                defaultSettings,
                defaultSettings,
                defaultSettings,
                defaultSettings,
                defaultSettings,
                defaultSettings,
                defaultSettings,
        };

        for (int i = 0; i < invalid.length; i++) {
            assertEquals(invalidSettings[i], new Settings(invalid[i]));
        }
    }
}
