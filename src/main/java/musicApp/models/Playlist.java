package musicApp.models;

import com.google.gson.annotations.Expose;

import java.nio.file.Path;
import java.util.List;

public class Playlist extends Library {
    @Expose
    private String name;
    @Expose
    private Path image;

    public Playlist(String name, Path image, List<Song> songList) {
        super(songList);
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public Path getImage() {
        return image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(Path image) {
        this.image = image;
    }
}
