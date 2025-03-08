package MusicApp.Models;

/**
 * Queue class to store songs.
 */
public class Queue extends Library {

    public Queue() {
        super();
    }

    /**
     * Add a song to the queue.
     * @param song The song to add.
     */
    public void add(Song song) {
        if (this.songList.contains(song)) {
            throw new IllegalArgumentException("Song already in queue");
        }
        songList.add(song);
    }

    /**
     * Remove a song from the queue.
     * @param song The song to remove.
     */
    public void remove(Song song) {
        if (!this.songList.contains(song)) {
            throw new IllegalArgumentException("Song not in queue");
        }
        songList.remove(song);
    }

    /**
     * Remove the first song from the queue.
     * @return The first song in the queue just removed.
     */
    public Song pop() {
        if (songList.isEmpty()) {
            throw new IllegalArgumentException("Queue is empty");
        }
        return songList.remove(0);
    }
}
