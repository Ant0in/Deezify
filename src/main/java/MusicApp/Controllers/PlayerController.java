package MusicApp.Controllers;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import MusicApp.Exceptions.ID3TagException;
import MusicApp.Models.*;
import MusicApp.Views.PlayerView;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Controller class for the music player.
 * <p>
 * This class is responsible for managing the audio player and the song library.
 * It provides methods to play, pause, skip, and go back to the previous song.
 * It also allows to add songs to a queue and play them in the order they were added.
 */
public class PlayerController {
    private final AudioPlayer audioPlayer;
    private final Library library;
    private final Queue queue;
    private final PlaylistManager playlistManager;
    private int currentIndex;
    private double currentSpeed = 1.0;

    private final PlayerView playerView;
    private final MetaController metaController;


    /**
     * Constructor
     */
    public PlayerController(MetaController metaController) throws IOException {
        this.metaController = metaController;
        this.audioPlayer = new AudioPlayer();
        this.library = new Library();
        this.queue = new Queue();
        this.playlistManager = new PlaylistManager(library);
        Settings settings = metaController.getSettings();
        this.audioPlayer.setBalance(settings.getBalance());
        this.loadLibrary(settings.getMusicDirectory());
        this.playerView = new PlayerView(this);
    }

    /**
     * Show the player view.
     * @param stage The stage to show the view on.
     */
    public void show(Stage stage) {
        this.playerView.show(stage);
    }

    /**
     * Loads the library with some sample songs from a the settings folder
     */
    public void loadLibrary(Path folderPath) {
        List<Path> songs;
        try {
            songs = MusicLoader.getAllSongPaths(folderPath);
        } catch (IOException e) {
            System.out.println("Error while loading library: " + e.getMessage() + " \n Song list initialized empty");
            return;
        }

        library.clear();
        for (Path songPath : songs) {

            HashMap<String, String> metadata = new HashMap<>();

            try {
                metadata = MetadataReader.getMetadata(songPath.toFile());
            } catch (ID3TagException e) {
                System.out.println("Error while reading metadata: " + e.getMessage());
            }
            
            library.add(new Song(
                    metadata,      // metadata
                    songPath       // file path
            ));
        }
        if (this.playerView != null){
            this.playerView.updatePlayListView();
        }

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
            applyCurrentSpeed();
            audioPlayer.setOnEndOfMedia(this::skip);
            audioPlayer.unpause();
            System.out.println("Playing: " + song.getSongName());
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
     * Get the current speed value.
     * @param speedLabel The speed label.
     * @return The speed value.
     */
    public double getSpeedValue(String speedLabel) {
        return switch (speedLabel) {
            case "0.25x" -> 0.25;
            case "0.5x" -> 0.5;
            case "0.75x" -> 0.75;
            case "1.25x" -> 1.25;
            case "1.5x" -> 1.5;
            case "1.75x" -> 1.75;
            case "2x" -> 2.0;
            default -> 1.0;
        };
    }

    /**
     * Change speed of the currently playing song.
     * @param speed The speed to set.
     */
    public void changeSpeed(double speed) {
        this.currentSpeed = speed;
        audioPlayer.changeSpeed(speed);
    }

    /**
     * Apply the current speed to the audio player.
     */
    public void applyCurrentSpeed() {
        audioPlayer.changeSpeed(currentSpeed);
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

    /**
     * Get the library.
     * @return The library.
     */
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
     * @param song The song to add.
     */
    public void addToQueue(Song song) {
        queue.add(song);
    }

    /**
     * Remove a song from the queue.
     * @param song The song to remove.
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
            applyCurrentSpeed();
            audioPlayer.setOnEndOfMedia(this::skip);
            audioPlayer.unpause();
            removeFromQueue(song);
            playerView.updateQueueListView();
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
        return getAudioPlayer().currentSongStringProperty();
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
    public List<Song> searchLibrary(String query) {
        List<Song> results = new ArrayList<>();
        for (Song song : library.toList()) {
            if (song.getSongName().toLowerCase().contains(query.toLowerCase()) ||
                song.getArtistName().toLowerCase().contains(query.toLowerCase()) ||
                song.getStyle().toLowerCase().contains(query.toLowerCase())) {
                results.add(song);
            }
        }
        return results;
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

    /**
     * Get a song from the library.
     * @param index The index of the song in the library.
     * @return The song at the specified index.
     */
    public Song getFromLibrary(int index) {
        return library.get(index);
    }


    /**
     * Get a song from the queue.
     * @param index The index of the song in the queue.
     * @return The song at the specified index.
     */
    public Song getFromQueue(int index) {
        return queue.get(index);
    }

    /**
     * Get the volume property.
     * @return The volume property.
     */
    public DoubleProperty volumeProperty() {
        return getAudioPlayer().volumeProperty();
    }

    /**
     * Get the cover image of the song.
     * @return The path to the cover image.
     */
    public String getCover(Song song) {
        return song.getCover();
    }

    /**
     * Get the current song.
     * @return The current song.
     */
    public Song getCurrentSong() {
        if (audioPlayer.isPlaying() != null) {
            return audioPlayer.getCurrentSong();
        } else {
            return null;
        }
    }

    /**
     * Close the audio player.
     */
    public void close() {
        audioPlayer.close();
    }

    /**
     * Open the settings window.
     */
    public void openSettings() {
        metaController.showSettings();
    }

    /**
     * Refresh the UI.
     */
    public void refreshUI() {
        playerView.refreshUI();
    }

    /**
     * Toggle the shuffle mode.
     * @param isEnabled The shuffle button state.
     */
    public void toggleShuffle(boolean isEnabled) {
        Song currentSong = (currentIndex >= 0 && currentIndex < library.size()) ? library.get(currentIndex) : null;
        playlistManager.toggleShuffle(isEnabled, currentSong);
        if (currentSong != null) {
            int newIndex = isEnabled ? 0 : playlistManager.getOriginalIndex(currentSong);
            if (newIndex != -1) {
                currentIndex = newIndex;
                goTo(currentIndex);
            }
        }
    }

    /**
     * Actions to do when the settings are changed
     * @param newSettings The new settings.
     */
    public void onSettingsChanged(Settings newSettings) {
        audioPlayer.setBalance(newSettings.getBalance());
        loadLibrary(newSettings.getMusicDirectory());
    }
}

