package musicApp.models;

import com.google.gson.annotations.Expose;
import javafx.scene.image.Image;
import musicApp.utils.AlertService;
import musicApp.utils.LanguageManager;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * Library class to store songs.
 */
public class Library {
    @Expose
    List<Song> songList;
    @Expose
    private String name;
    @Expose
    private Path image;


    /**
     * Constructor to create a library with a list of songs, a name and an image.
     *
     * @param _songList The list of songs.
     * @param _name     The name of the library.
     * @param _image    The path to the cover image.
     */
    public Library(List<Song> _songList, String _name, Path _image) {
        songList = _songList;
        name = _name;
        image = _image;
    }

    /**
     * Constructor to create an empty library.
     */
    public Library() {
        songList = new ArrayList<>();
    }

    /**
     * Add a song to the library.
     *
     * @param song The song to add.
     */
    public void add(Song song) throws IllegalArgumentException {
        if (songList.contains(song)) {
            throw new IllegalArgumentException(
                    LanguageManager.getInstance().get("error.mediaAlreadyInLibrary")
                            + " " + song.toString()
            );
        }
        songList.add(song);
    }

    /**
     * Add a media to the library at a specific index.
     *
     * @param index The index to add the song.
     * @param song  The media to add.
     */
    public void add(int index, Song song) throws IllegalArgumentException {
        if (songList.contains(song)) {
            throw new IllegalArgumentException(
                    LanguageManager.getInstance().get("error.mediaAlreadyInLibrary")
                            + " " + song.toString()
            );
        }
        songList.add(index, song);
    }

    /**
     * Remove a song from the library.
     *
     * @param song The song to remove.
     */
    public void remove(Song song) {
        if (!songList.contains(song)) {
            throw new IllegalArgumentException("Media not in library");
        }
        songList.remove(song);
    }

    /**
     * Get the size of the library.
     *
     * @return The size of the library.
     */
    public int size() { return songList.size(); }

    /**
     * Check if the library is empty.
     *
     * @return True if the library is empty, false otherwise.
     */
    public Boolean isEmpty() {
        return songList.isEmpty();
    }

    /**
     * Get a song from the library.
     *
     * @param index The index of the song.
     * @return The song at the index.
     */
    public Song get(int index) {
        return songList.get(index);
    }

    /**
     * Get the list of songs.
     *
     * @return The list of songs.
     */
    public List<Song> toList() {
        return songList;
    }

    /**
     * Clear the library.
     */
    public void clear() {
        songList.clear();
    }

    /**
     * Search for songs in the library.
     *
     * @param text The text to search for.
     * @return The list of songs that contain the text.
     */
    public List<Song> search(String text) {
        return songList
                .stream()
                .filter(s -> s.containsText(text))
                .toList();
    }

    /**
     * Get a song by its path.
     *
     * @param path The path of the song.
     * @return The song at the path.
     */
    public Song getSongByPath(Path path) {
        for (Song song : songList) {
            if (song.getFilePath().equals(path)) {
                return song;
            }
        }
        return null;
    }

    /**
     * Get the name of the library.
     *
     * @return The name of the library.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the library.
     *
     * @param newName the new name
     */
    public void setName(String newName) {
        name = newName;
    }

    /**
     * Get the image path of the library.
     *
     * @return The image path of the library.
     */
    public Path getImagePath() {
        return image;
    }

    /**
     * Set the image path of the library.
     *
     * @param imagePath the new image path
     */
    public void setImagePath(Path imagePath) {
        image = imagePath;
    }

    /**
     * Get the cover image for this library.
     *
     * @return The cover image or a default image if none is set
     */
    public Image getCoverImage() {
        String defaultCover = getClass().getResource("/images/playlist.png").toExternalForm();
        try {
            if (image != null) {
                return new Image(image.toUri().toURL().toExternalForm());
            }
            return new Image(Objects.requireNonNull(defaultCover));
        } catch (Exception e) {
            AlertService alertService = new AlertService();
            alertService.showExceptionAlert(e);
            return new Image(Objects.requireNonNull(defaultCover));
        }
    }
    
    /**
     * Get the auto-completion for a song artist.
     * 
     * @param input The input string to complete.
     * @return The auto-completion for the song artist.
     */
    public Optional<String> getArtistAutoCompletion(String input) {
        return getAutoCompletion(input, song -> List.of(song.getArtist()));
    }

    /**
     * Get the auto-completion for a song tag.
     * 
     * @param input The input string to complete.
     * @return The auto-completion for the song tag.
     */
    public Optional<String> getTagAutoCompletion(String input) {
        return getAutoCompletion(input, Song::getUserTags);
    }

    /**
     * Get the auto-completion for a string in a list given as argument.
     * 
     * @param input The input string to complete.
     * @param extractor The function to extract the list of strings for the autocompletion.
     * @return The auto-completion for the input string.
     */
    private Optional<String> getAutoCompletion(String input, Function<Song, List<String>> extractor) {
        if (input == null || input.isEmpty() || songList.isEmpty()) {
            return Optional.empty();
        }

        String lowerInput = input.toLowerCase();
        int size = songList.size();
        int randomIndex = (int) (Math.random() * size);

        // Search for an autocompletion starting at a random index
        for (int offset = 0; offset < size; offset++) {
            int i = (randomIndex + offset) % size;
            List<String> fields = extractor.apply(songList.get(i));

            for (String field : fields) {
                if (field == null) continue;
                String lowerField = field.toLowerCase();
                if (lowerField.startsWith(lowerInput) && lowerField.length() > lowerInput.length()) {
                    return Optional.of(field);
                }
            }
        }
        return Optional.empty();
    }
}
