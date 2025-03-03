package MusicApp.Models;

import java.util.ArrayList;
import java.util.List;

public class Library {
    List<Song> songList;

    public Library(List<Song> songList) {
        this.songList = songList;
    }

    public Library() {
        this.songList = new ArrayList<>();
    }

    public void add(Song song) {
        if (this.songList.contains(song)) {
            throw new IllegalArgumentException("Song already in library");
        }
        songList.add(song);
    }

    public void add(int index, Song song) {
        if (this.songList.contains(song)) {
            throw new IllegalArgumentException("Song already in library");
        }
        songList.add(index, song);
    }

    public void addSongs(List<Song> songs) {
        for (Song song : songs) {
            add(song);
        }
    }

    public void remove(Song song) {
        if (!this.songList.contains(song)) {
            throw new IllegalArgumentException("Song not in library");
        }
        songList.remove(song);
    }

    public int size() {
        return songList.size();
    }

    public Boolean isEmpty() {
        return songList.isEmpty();
    }

    public Song get(int index) {
        return songList.get(index);
    }

    public List<Song> toList(){
        return songList;
    }

    public void clear() {
        songList.clear();
    }
}
