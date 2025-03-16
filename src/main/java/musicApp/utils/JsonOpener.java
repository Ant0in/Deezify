package musicApp.utils;


import java.nio.file.Path;

import java.util.ArrayList;
import java.util.List;

public class JsonOpener {

    public static List<String> getSongLyrics(Path songPath) {

        // Temporary lyrics for testing
        List<String> lyrics = new ArrayList<>();
        lyrics.add(songPath.toAbsolutePath().toString());
        for(int i = 0; i < 100; i++) {
            lyrics.add("lyric" + i);
        }
        return lyrics;
    }
}