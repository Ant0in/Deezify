package MusicApp.Controllers;

import MusicApp.Models.AudioPlayer;
import MusicApp.Models.Song;
import javafx.beans.property.StringProperty;
import javafx.util.Duration;
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
    private final List<Song> library;
    private final List<Song> queue;
    private int currentIndex;

    /**
     * Constructor
     */
    public PlayerController() {
        this.audioPlayer = new AudioPlayer();
        this.library = new ArrayList<>();
        this.queue = new ArrayList<>();
        loadLibrary();
    }

    /**
     * Load the library with some sample songs.
     * This method is for testing purposes only.
     * In a real application, the library would be loaded from a database or a file.
     */
    private void loadLibrary() {
        library.add(new Song("Song1", "Artist1", "Pop", "src/main/resources/songs/song1.mp3", Duration.minutes(3)));
        library.add(new Song("Song2", "Artist2", "Rock", "src/main/resources/songs/song2.mp3", Duration.minutes(4)));
        library.add(new Song("Song3", "Artist3", "Jazz", "src/main/resources/songs/song3.mp3", Duration.minutes(5)));
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
            queue.removeFirst();
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
    public List<String> getLibrary() {
        List<String> songNames = new ArrayList<>();
        for (Song song : library) {
            songNames.add(song.getSongName());
        }
        return songNames;
    }

    /**
     * Get the list of songs in the queue.
     * @return The list of songs in the queue.
     */
    public List<String> getQueue() {
        List<String> songNames = new ArrayList<>();
        for (Song song : queue) {
            songNames.add(song.getSongName());
        }
        return songNames;
    }

    /**
     * Add a song to the queue.
     * @param index The index of the song in the library.
     */
    public void addToQueue(int index) {
        if (index >= 0 && index < library.size()) {
            queue.add(library.get(index));
        }
    }

    /**
     * Remove a song from the queue.
     * @param index The index of the song in the queue.
     */
    public void removeFromQueue(int index) {
        if (index >= 0 && index < queue.size()) {
            queue.remove(index);
        }
    }

    public void clearQueue() {
        queue.clear();
    }
    
    
    public void playFromLibrary(int index) {
        if (index >= 0 && index < library.size()) {
            currentIndex = index;
            playCurrent();
        }
    }
    
    public void playFromQueue(int index) {
        if (index >= 0 && index < queue.size()) {
            Song song = queue.get(index);
            audioPlayer.loadSong(song);
            audioPlayer.unpause();
            System.out.println("Playing from queue: " + song.getSongName());
        }
    }
    
    public Duration getCurrentTime() {
        return getAudioPlayer().getCurrentTime();
    }
    
    public Duration getTotalDuration() {
        return getAudioPlayer().getTotalDuration();
    }
    
    public javafx.beans.property.DoubleProperty progressProperty() {
        return getAudioPlayer().progressProperty();
    }

    public Boolean isPlaying() {
        return getAudioPlayer().isPlaying();
    }

    public StringProperty currentSongProperty() {
        return getAudioPlayer().currentSongProperty();
    }
    

}

