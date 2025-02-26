package MusicApp.Controllers;

import MusicApp.Models.AudioPlayer;
import MusicApp.Models.Library;
import MusicApp.Models.Queue;
import MusicApp.Models.Song;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Duration;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for the music player.
 *
 * This class is responsible for managing the audio player and the song library.
 * It provides methods to play, pause, skip, and go back to the previous song.
 * It also allows to add songs to a queue and play them in the order they were added.
 */
public class PlayerController {

    /**
     * Interface for the song change listener.
     */
    public interface SongChangeListener {
        /**
         * Called when the current song changes.
         */
        void onSongChanged();
    }
    
    private SongChangeListener songChangeListener;

    /**
     * Set the song change listener.
     * @param listener The listener to set.
     */
    public void setSongChangeListener(SongChangeListener listener) {
        this.songChangeListener = listener;
    }

    
    private final AudioPlayer audioPlayer;
    private final Library library;
    private final Queue queue;
    private int currentIndex;

    /**
     * Constructor
     */
    public PlayerController() {
        this.audioPlayer = new AudioPlayer();
        this.library = new Library();
        this.queue = new Queue();
        loadLibrary();
    }

    /**
     * Load the library with some sample songs.
     * This method is for testing purposes only.
     * In a real application, the library would be loaded from a database or a file.
     */
    private void loadLibrary() {
        library.add(new Song("HIGHEST IN THE ROOM", "Travis Scott", "Pop", Paths.get("src/main/resources/songs/song1.mp3"), Duration.minutes(3)));
        library.add(new Song("World, Hold On", "Bob Sinclar", "Rock", Paths.get("src/main/resources/songs/song2.mp3"), Duration.minutes(4)));
        library.add(new Song("ARSENAL", "Maes", "Jazz", Paths.get("src/main/resources/songs/song3.mp3"), Duration.minutes(5)));
        library.add(new Song("Dance Monkey", "Tones and I", "Pop", Paths.get("src/main/resources/songs/song4.mp3"), Duration.minutes(3)));
        library.add(new Song("Blinding Lights", "The Weeknd", "Pop", Paths.get("src/main/resources/songs/song5.mp3"), Duration.minutes(4)));
        library.add(new Song("Roses", "SAINt JHN", "Pop", Paths.get("src/main/resources/songs/song6.mp3"), Duration.minutes(3)));
    }

    /**
     * Skip to the next song in the library or the queue.
     * If the queue is not empty, the next song in the queue is played.
     * Otherwise, the next song in the library is played.
     */
    public void skip() {
        if (queue.isEmpty()) {
            if (currentIndex < library.size() - 1) {
                currentIndex++;
                playCurrent();
            }
        } else {
            playFromQueue(0);
        }
    }

    /**
     * Go back to the previous song in the library.
     */
    public void prec() {
        if (currentIndex > 0) {
            currentIndex--;
            playCurrent();
        }
    }

    /**
     * Pause the currently playing song.
     */
    public void pause() {
        audioPlayer.pause();
    }

    /**
     * Unpause the currently paused song.
     */
    public void unpause() {
        audioPlayer.unpause();
    }

    /**
     * Go to a specific song in the library.
     * @param index The index of the song in the library.
     */
    public void goTo(int index) {
        if (index >= 0 && index < library.size()) {
            currentIndex = index;
            playCurrent();
        }
    }

    /**
     * Load and Play the currently selected song.
     */
    private void playCurrent() {
        if (currentIndex >= 0 && currentIndex < library.size()) {
            Song song = library.get(currentIndex);
            audioPlayer.loadSong(song);
            audioPlayer.setOnEndOfMedia(this::skip);
            audioPlayer.unpause();
            System.out.println("Playing: " + song.getSongName());
    
            if (songChangeListener != null) {
                songChangeListener.onSongChanged();
            }
        }
    }
    

    /**
     * Get the audio player.
     * @return The audio player.
     */
    public AudioPlayer getAudioPlayer() { return audioPlayer; }


    /**
     * Set the volume of the audio player.
     * @param volume The volume level (0.0 to 1.0).
     */
    public void setVolume(double volume) {
        getAudioPlayer().setVolume(volume);
    }


    /**
     * Get the list of songs in the library.
     * @return The list of songs in the library.
     */
    public List<String> getLibraryNames() {
        List<String> songNames = new ArrayList<>();
        for (Song song : library.toList()) {
            songNames.add(song.toString());
        }
        return songNames;
    }

    public Library getLibrary() {
        return library;
    }

    /**
     * Get the list of songs in the queue.
     * @return The list of songs in the queue.
     */
    public List<String> getQueueNames() {
        List<String> songNames = new ArrayList<>();
        for (Song song : queue.toList()) {
            songNames.add(song.toString());
        }
        return songNames;
    }

    public Queue getQueue() {
        return queue;
    }

    /**
     * Add a song to the queue.
     * @param index The index of the song in the library.
     */
    public void addToQueue(Song song) {
        queue.add(song);
    }

    /**
     * Remove a song from the queue.
     * @param index The index of the song in the queue.
     */
    public void removeFromQueue(Song song) {
        queue.remove(song);
    }

    /**
     * Clear the queue.
     */
    public void clearQueue() {
        queue.clear();
    }
    
    /**
     * Play a song from the library.
     * @param index The index of the song in the library.
     */
    public void playFromLibrary(int index) {
        if (index >= 0 && index < library.size()) {
            currentIndex = index;
            playCurrent();
        }
    }

    /**
     * Play a song from the queue.
     * @param index The index of the song in the queue.
     */
    public void playFromQueue(int index) {
        if (index >= 0 && index < queue.size()) {
            Song song = queue.get(index);
            audioPlayer.loadSong(song);
            audioPlayer.unpause();
            removeFromQueue(song);
            System.out.println("Playing from queue: " + song.getSongName());
        }
    }

    /**
     * Get the current time of the song.
     * @return The current time of the song.
     */
    public Duration getCurrentTime() {
        return getAudioPlayer().getCurrentTime();
    }

    /**
     * Get the total duration of the song.
     * @return The total duration of the song.
     */
    public Duration getTotalDuration() {
        return getAudioPlayer().getTotalDuration();
    }

    /**
     * Get the progress of the song.
     * @return The progress of the song.
     */
    public javafx.beans.property.DoubleProperty progressProperty() {
        return getAudioPlayer().progressProperty();
    }

    /**
     * Return whether the song is playing.
     * @return Whether the song is playing.
     */
    public BooleanProperty isPlaying() {
        return getAudioPlayer().isPlaying();
    }

    /**
     * Get the current song property.
     * @return The current song property.
     */
    public StringProperty currentSongProperty() {
        return getAudioPlayer().currentSongProperty();
    }

    /**
     * Seek to a specific duration in the song.
     * @param duration The duration to seek to.
     */
    public void seek(double duration) {
        getAudioPlayer().seek(duration);
    }

    /**
     * Search the library for songs that match the query.
     * @param query The query to search for.
     * @return A list of song names that match the query.
     */
    public List<String> searchLibrary(String query) {
        List<Song> results = new ArrayList<>();
        for (Song song : library.toList()) {
            if (song.getSongName().toLowerCase().contains(query.toLowerCase()) ||
                song.getArtistName().toLowerCase().contains(query.toLowerCase()) ||
                song.getStyle().toLowerCase().contains(query.toLowerCase())) {
                results.add(song);
            }
        }
        List<String> songNames = new ArrayList<>();
        for (Song song : results) {
            songNames.add(song.getSongName() + " · " + song.getArtistName());
        }
        return songNames;
    }
    
    /**
     * Reorganize the queue by moving a song from one index to another.
     * @param fromIndex The initial index of the song.
     * @param toIndex The index where the song should be placed.
     */

    public void reorderQueue(int fromIndex, int toIndex) {
        if (fromIndex >= 0 && fromIndex < queue.size() && toIndex >= 0 && toIndex <= queue.size()) {
            Song song = queue.get(fromIndex);
            queue.remove(song);
            queue.add(toIndex, song);
        }
    }

    public Song getFromLibrary(int index) {
        return library.get(index);
    }

    public Song getFromQueue(int index) {
        return queue.get(index);
    }
}

