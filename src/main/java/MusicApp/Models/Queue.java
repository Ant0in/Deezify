package MusicApp.Models;

public class Queue extends Library {
    public Queue() {
        super();
    }

    public void add(Song song) {
        if (this.songList.contains(song)) {
            throw new IllegalArgumentException("Song already in queue");
        }
        songList.add(song);
    }

    public void remove(Song song) {
        if (!this.songList.contains(song)) {
            throw new IllegalArgumentException("Song not in queue");
        }
        songList.remove(song);
    }

    public Song pop() {
        if (songList.isEmpty()) {
            throw new IllegalArgumentException("Queue is empty");
        }
        return songList.remove(0);
    }
}
