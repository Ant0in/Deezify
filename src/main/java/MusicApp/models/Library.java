package MusicApp.models;

import MusicApp.utils.MusicLoader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Library class to store songs.
 */
public class Library {
    List<Song> songList = new ArrayList<>();

    /**
     * Constructor
     *
     * @param songList The list of songs.
     */
    public Library(List<Song> songList) {
        this.songList = songList;
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
    }

    /**
     * Add a song to the library.
     *
     * @param song The song to add.
     */
    public void add(Song song) {
        if (this.songList.contains(song)) {
            System.err.println("Song already in library");
            return;
        }
        songList.add(song);
    }

    /**
     * Add a song to the library at a specific index.
     *
     * @param index The index to add the song.
     * @param song  The song to add.
     */
    public void add(int index, Song song) {
        if (this.songList.contains(song)) {
            System.err.println("Song already in library");
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
        if (!this.songList.contains(song)) {
            throw new IllegalArgumentException("Song not in library");
        }
        songList.remove(song);
    }

    /**
     * Get the size of the library.
     *
     * @return The size of the library.
     */
    public int size() {
        return songList.size();
    }

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
}
