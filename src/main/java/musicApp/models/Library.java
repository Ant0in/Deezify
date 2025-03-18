package musicApp.models;

import musicApp.utils.MusicLoader;
import musicApp.utils.RadioLoader;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Library class to store songs.
 */
public class Library {
    List<Song> mediaList = new ArrayList<>();

    /**
     * Constructor
     *
     * @param mediaList The list of media.
     */
    public Library(List<Song> mediaList) {
        this.mediaList = mediaList;
    }

    /**
     * Constructor to create an empty library.
     */
    public Library() {
    }

    /**
     * Constructor to create a library initialized with folder content.
     */
    public Library(Path folderPath) {
        this.load(folderPath);
    }

    /**
     * Loads the library with some sample songs from a settings folder
     */
    public void load(Path folderPath) {
        List<Path> songs;
        try {
            MusicLoader loader = new MusicLoader();
            songs = loader.getAllSongPaths(folderPath);
        } catch (IOException e) {
            System.out.println("Error while loading library: " + e.getMessage() + " \n Song list initialized empty");
            return;
        }
        this.clear();
        for (Path songPath : songs) {
            this.add(new Song(songPath));
        }

        Path radiosPath = Paths.get("src/main/resources/radios");
        RadioLoader radioLoader = new RadioLoader();

        try {
            List<Path> m3uFiles = radioLoader.loadM3UFiles(radiosPath);
            System.out.println(m3uFiles.toString());

            for (Path m3ufilePath :m3uFiles){
                this.add(new Radio(m3ufilePath));
            }
        } catch (IOException e) {
            System.err.println("Error while loading radios : " + e.getMessage());
        }
    }

    /**
     * Add a media to the library.
     *
     * @param song The song to add.
     */
    public void add(Song song) {
        if (this.mediaList.contains(song)) {
            System.err.println("Media already in library");
            return;
        }
        mediaList.add(song);
    }

    /**
     * Add a media to the library at a specific index.
     *
     * @param index The index to add the song.
     * @param song  The media to add.
     */
    public void add(int index, Song song) {
        if (this.mediaList.contains(song)) {
            System.err.println("Media already in library");
            return;
        }
        mediaList.add(index, song);
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
        if (!mediaList.contains(song)) {
            throw new IllegalArgumentException("Media not in library");
        }
        mediaList.remove(song);
    }

    /**
     * Get the size of the library.
     *
     * @return The size of the library.
     */
    public int size() { return mediaList.size(); }

    /**
     * Check if the library is empty.
     *
     * @return True if the library is empty, false otherwise.
     */
    public Boolean isEmpty() {
        return mediaList.isEmpty();
    }

    /**
     * Get a song from the library.
     *
     * @param index The index of the song.
     * @return The song at the index.
     */
    public Song get(int index) {
        return mediaList.get(index);
    }

    /**
     * Get the list of songs.
     *
     * @return The list of songs.
     */
    public List<Song> toList() {
        return this.mediaList;
    }

    /**
     * Clear the library.
     */
    public void clear() {
        mediaList.clear();
    }

    /**
     * Search for songs in the library.
     *
     * @param text The text to search for.
     * @return The list of songs that contain the text.
     */
    public List<Song> search(String text) {
        return mediaList
                .stream()
                .filter(s -> s.containsText(text))
                .toList();
    }
}
