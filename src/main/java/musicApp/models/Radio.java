package musicApp.models;

import javafx.scene.image.Image;
import musicApp.utils.RadioLoader;
import java.nio.file.Path;
import java.util.Objects;

public class Radio extends Song {

    private String webUrl;
    private RadioLoader radioLoader;

    public Radio(Path filePath) {
        super(filePath);
        this.radioLoader = new RadioLoader();
        this.webUrl = radioLoader.parseM3U(filePath).get(0);
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getFilePathString() {
        return webUrl;
    } 

    public Image getCoverImage() {
        return new Image(Objects.requireNonNull(getClass().getResource("/images/radio.png")).toExternalForm());
    }
}