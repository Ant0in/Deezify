package musicApp.repositories.gsonTypeAdapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import musicApp.models.Library;
import musicApp.models.Song;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class LibraryTypeAdapter extends TypeAdapter<Library> {
    /**
     * Writes the Library object to JSON format.
     *
     * @param out      The JsonWriter to write to.
     * @param playlist The Library object to write.
     * @throws IOException If writing fails.
     */
    @Override
    public void write(JsonWriter out, Library playlist) throws IOException {
        out.beginObject();

        out.name("name");
        out.value(playlist.getName());

        out.name("image");
        if (playlist.getImagePath() == null) {
            out.value("");
        } else {
            out.value(playlist.getImagePath().toString());
        }

        out.name("songList");
        out.beginArray();
        for (Song song : playlist.toList()) {
            out.value(song.getFilePath().toString());
        }
        out.endArray();

        out.endObject();
    }

    /**
     * Reads a Library object from JSON format.
     *
     * @param in The JsonReader to read from.
     * @return The Library object read from JSON.
     * @throws IOException If reading fails.
     */
    @Override
    public Library read(JsonReader in) throws IOException {
        String name = null;
        Path image = null;
        List<Song> songList = null;

        in.beginObject();
        while (in.hasNext()) {
            String fieldName = in.nextName();
            switch (fieldName) {
                case "name":
                    name = in.nextString();
                    break;
                case "image":
                    String imagePath = in.nextString();
                    image = imagePath.isEmpty() ? null : Path.of(imagePath);
                    break;
                case "songList":
                    in.beginArray();
                    songList = new ArrayList<>();
                    while (in.hasNext()) {
                        String songPath = in.nextString();
                        songList.add(new Song(Path.of(songPath)));
                    }
                    in.endArray();
                    break;
                default:
                    in.skipValue();
                    break;
            }
        }
        in.endObject();

        return new Library(songList, name, image);
    }
}