package musicApp.utils.gsonTypeAdapter;

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
    @Override
    public void write(JsonWriter out, Library playlist) throws IOException {
        out.beginObject();

        out.name("name");
        out.value(playlist.getName());

        out.name("image");
        if (playlist.getImage() == null) {
            out.value("");
        } else {
            out.value(playlist.getImage().toString());
        }

        out.name("songList");
        out.beginArray();
        for (Song song : playlist.toList()) {
            out.value(song.getFilePath().toString());
        }
        out.endArray();

        out.endObject();
    }

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