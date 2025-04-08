package musicApp.models;

import com.google.gson.annotations.Expose;
import javafx.scene.image.Image;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Library class to store songs.
 */
public class Library {
    @Expose
    List<Song> songList = new ArrayList<>();
    @Expose
    private String name;
    @Expose
    private Path image;


    /**
     * Constructor
     *
     * @param songList The list of media.
     */
    public Library(List<Song> songList, String name, Path image) {
        this.songList = songList;
        this.name = name;
        this.image = image;
    }

    /**
     * Constructor to create an empty library.
     */
    public Library() {
    }

    /**
     * Add a song to the library.
     *
     * @param song The song to add.
     */
    public void add(Song song) {
        if (this.songList.contains(song)) {
            System.err.println("Media already in library");
            return;
        }
        songList.add(song);
    }

    /**
     * Add a media to the library at a specific index.
     *
     * @param index The index to add the song.
     * @param song  The media to add.
     */
    public void add(int index, Song song) {
        if (this.songList.contains(song)) {
            System.err.println("Media already in library");
            return;
        }
        songList.add(index, song);
    }

    /**
     * Add a list of songs to the library.
     *
     * @param songs The list of songs to add.
     */
    public void addSongs(List<Song> songs) {
        for (Song song : songs) {
            add(song);
        }
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
        return this.songList;
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


    public Song getSongByPath(Path path) {
        for (Song song : songList) {
            if (song.getFilePath().equals(path)) {
                return song;
            }
        }
        return null;
    }

    public String getName() {
        return this.name;
    }

    /**
     * Set the name of the library.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    public Path getImage() {
        return this.image;
    }

    public void setImage(Path image) {
        this.image = image;
    }

    /**
     * Set the image path of the library.
     *
     * @param imagePath the new image path
     */
    public void setImagePath(Path imagePath) {
        this.image = imagePath;
    }

    /**
     * Get the cover image for this library.
     *
     * @return The cover image or a default image if none is set
     */
    public Image getCoverImage() {
        try {
            if (image != null) {
                return new Image(image.toUri().toURL().toExternalForm());
            }
            return new Image(Objects.requireNonNull(getClass().getResource("/images/playlist.png")).toExternalForm());
        } catch (Exception e) {
            System.err.println("Error loading cover image: " + e.getMessage());
            return new Image(Objects.requireNonNull(getClass().getResource("/images/playlist.png")).toExternalForm());
        }
    }

    public Optional<String> getArtistAutoCompletion(String input) {
        if (input == null || input.isEmpty() || songList.isEmpty()) {
            return Optional.empty();
        }

        String lowerInput = input.toLowerCase();
        int size = songList.size();
        int randomIndex = (int) (Math.random() * size);

        for (int offset = 0; offset < size; offset++) {
            int i = (randomIndex + offset) % size;
            String artist = songList.get(i).getArtist();
            if (artist != null) {
                String lowerArtist = artist.toLowerCase();
                if (lowerArtist.startsWith(lowerInput) && lowerArtist.length() > lowerInput.length()) {
                    return Optional.of(artist);
                }
            }
        }

        return Optional.empty();
    }

}
