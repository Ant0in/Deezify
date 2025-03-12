package MusicApp.Controllers;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import MusicApp.Exceptions.BadFileTypeException;
import MusicApp.Exceptions.ID3TagException;
import MusicApp.Models.*;
import MusicApp.Views.PlayerView;
import MusicApp.utils.MetadataReader;
import MusicApp.utils.MusicLoader;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Controller class for the music player.
 * <p>
 * This class is responsible for managing the audio player and the song library.
 * It provides methods to play, pause, skip, and go back to the previous song.
 * It also allows to add songs to a queue and play them in the order they were added.
 */
public class PlayerController {

    private final Library library;
    private final Queue queue;
    private final PlaylistManager playlistManager;
    private int currentIndex;

    private PlayerView playerView;
    private final MetaController metaController;
    private ControlPanelController controlPanelController;
    private ToolBarController toolBarController;



    /**
     * Constructor
     */
    public PlayerController(MetaController metaController) throws IOException {
        this.metaController = metaController;
        this.library = new Library();
        this.queue = new Queue();
        this.playlistManager = new PlaylistManager(library);
        this.controlPanelController = new ControlPanelController(this);
        this.toolBarController = new ToolBarController(this);
        Settings settings = metaController.getSettings();
        this.controlPanelController.setBalance(settings.getBalance());
        this.loadLibrary(settings.getMusicDirectory());



        initPlayerView();
        // this.playerView = new PlayerView(this);
    }

    private void initPlayerView() throws IOException {
        this.playerView = new PlayerView();
        this.playerView.setPlayerController(this);
        try {
            this.playerView.initializeScene("/fxml/MainLayout.fxml");
            this.playerView.initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Show the player view.
     * @param stage The stage to show the view on.
     */
    public void show(Stage stage) {
        this.playerView.show(stage);
    }

    /**
     * Loads the library with some sample songs from a settings folder
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

            try {

                Metadata metadata = MetadataReader.getMetadata(songPath.toFile());

                library.add(new Song(
                        metadata,      // metadata
                        songPath       // file path
                ));
            } catch (ID3TagException e) {
                System.out.println("Error while reading metadata: " + e.getMessage());
            } catch (BadFileTypeException e) {
                System.out.println("Bad file type: " + e.getMessage());
            }
            

        }
        if (this.playerView != null){
            this.playerView.updatePlayListView();
        }

    }

    public void updatePlaylistView(){
        this.playerView.updatePlayListView();
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
                this.controlPanelController.playCurrent(getCurrentSong());
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
            this.controlPanelController.playCurrent(getCurrentSong());
        }
    }

    /**
     * Go to a specific song in the library.
     * @param index The index of the song in the library.
     */
    public void goTo(int index) {
        if (index >= 0 && index < library.size()) {
            currentIndex = index;
            this.controlPanelController.playCurrent(getCurrentSong());
        }
    }


    

    /**
     * Get the audio player.
     * @return The audio player.
     */
    // public AudioPlayer getAudioPlayer() { return audioPlayer; }


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
            this.controlPanelController.playCurrent(getCurrentSong());
        }
    }

    /**
     * Play a song from the queue.
     * @param index The index of the song in the queue.
     */
    public void playFromQueue(int index) {
        if (index >= 0 && index < queue.size()) {
            Song song = queue.get(index);
            this.controlPanelController.playCurrent(song);
            removeFromQueue(song);
            playerView.updateQueueListView();
            System.out.println("Playing from queue: " + song.getSongName());
        }
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
        if (currentIndex < 0 || currentIndex >= library.size()) {
            return null;
        }
        return library.get(currentIndex);
    }

    /**
     * Close the audio player.
     */
    public void close() {
        this.controlPanelController.close();
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
        this.playerView.updatePlayListView();
    }

    /**
     * Actions to do when the settings are changed
     * @param newSettings The new settings.
     */
    public void onSettingsChanged(Settings newSettings) {
        this.controlPanelController.setBalance(newSettings.getBalance());
        loadLibrary(newSettings.getMusicDirectory());
    }

    public Pane getControlPanelRoot() {
        return this.controlPanelController.getRoot();
    }

    public Pane getToolBarRoot(){
        return this.toolBarController.getRoot();
    }


    /**
     * Handle the next song button.
     */
    public void handleNextSong() {
        skip();
        this.playerView.updateQueueListView();
    }

    /**
     * Handle the previous song button.
     */
    public void handlePreviousSong() {
        prec();
    }

}

