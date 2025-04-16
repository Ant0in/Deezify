package musicApp.models;

import musicApp.exceptions.BadM3URadioException;
import javafx.scene.image.Image;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;
import java.util.List;
import java.io.IOException;
import java.nio.file.Files;


public class Radio extends Song {

    private String webUrl;

    /**
     * Constructor for radio.
     * @param filePath, the path to the radio file
     * @throws BadM3URadioException Thrown if the M3U file is not valid.
     */
    public Radio(Path filePath) throws BadM3URadioException {
        super(filePath);
        webUrl = parseM3U(filePath);
    }

    /**
     * Checks if the line looks like a URL.
     * @param line the line to check
     * @return true if the line looks like a URL, false otherwise
     */
    private boolean looksLikeURL(String line) {
        try {
            URI uri = new URI(line);
            // Vérifie que le scheme est http ou https, et qu'il y a un host
            return (uri.getScheme() != null && (uri.getScheme().equals("http") || uri.getScheme().equals("https"))
                    && uri.getHost() != null);
        } catch (URISyntaxException e) {
            return false;
        }
    }

    /**
     * Reads an M3U file and extract the web url to the radio from it.
     * @param m3uFile the path to the M3U file
     * @return the url as a string.
     * @throws BadM3URadioException Thrown if the M3U file is not valid.
     */
    private String parseM3U(Path m3uFile) throws BadM3URadioException {
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
            throw new BadM3URadioException(e);
        }
        return url;
    }

    /**
     * Sets the radio web url.
     * @param _webUrl the new web url to set
     */
    public void setWebUrl(String _webUrl) {
        webUrl = _webUrl;
    }
    
    /**
     * Returns the radio web url.
     * @return the web url as a string.
     */
    @Override
    public String getSource() throws BadM3URadioException {
        if (!looksLikeURL(webUrl)) {
            throw new BadM3URadioException("The M3U file is empty or does not contain a valid URL.");
        }
        return webUrl;
    } 

    /**
     * Returns the radio cover image.
     * @return the cover image as an Image object.
     */
    @Override
    public Image getCoverImage() {
        return new Image(Objects.requireNonNull(getClass().getResource("/images/radio.png")).toExternalForm());
    }
    
    /**
     * Returns the radio type.
     * @return false, as this is not a song.
     */
    @Override
    public Boolean isSong() {
        return false;
    }

}