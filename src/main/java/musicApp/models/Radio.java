package musicApp.models;

import java.io.IOException;
import javafx.scene.image.Image;
import java.nio.file.Path;
import java.util.Objects;
import java.util.List;
import java.nio.file.Files;


public class Radio extends Song {

    private String webUrl;

    /**
     * Constructor for radio.
     * @param filePath, the path to the radio file
     */
    public Radio(Path filePath) {
        super(filePath);
        this.webUrl = this.parseM3U(filePath);
    }

    /**
     * Sets the radio web url.
     * @param webUrl
     */
    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }
    
    /**
     * Returns the radio web url.
     * @return the web url as a string.
     */
    public String getFilePathString() {
        return webUrl;
    } 

    /**
     * Returns the radio cover image.
     * @return the cover image as an Image object.
     */
    public Image getCoverImage() {
        return new Image(Objects.requireNonNull(getClass().getResource("/images/radio.png")).toExternalForm());
    }

    /**
     * Reads an M3U file and extract the web url to the radio from it.
     * @param m3uFile
     * @return the url as a string.
     */
    public String parseM3U(Path m3uFile) {
        String url = "";
        try {
            List<String> lines = Files.readAllLines(m3uFile);
            for (String line : lines) {
                line = line.trim();
                if (!line.isEmpty() && !line.startsWith("#")) {
                    url = line;
                }
            }
        } catch (IOException e) {
            System.err.println("Impossible to read " + m3uFile + " : " + e.getMessage());
        }
        return url;
    }
    
    @Override
    public Boolean isSong() {
        return false;
    }

}