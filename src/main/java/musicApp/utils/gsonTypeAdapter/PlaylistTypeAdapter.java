package musicApp.utils.gsonTypeAdapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import musicApp.models.Playlist;
import musicApp.models.Song;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.ArrayList;

public class PlaylistTypeAdapter extends TypeAdapter<Playlist> {
    @Override
    public void write(JsonWriter out, Playlist playlist) throws IOException {
        out.beginObject();

        out.name("name");
        out.value(playlist.getName());

        out.name("image");
        if (playlist.getImage() == null) {
            out.nullValue();
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
    public Playlist read(JsonReader in) throws IOException {
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

        return new Playlist(name, image, songList);
    }
}