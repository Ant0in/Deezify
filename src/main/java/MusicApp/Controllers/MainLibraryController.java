package MusicApp.Controllers;


import MusicApp.Exceptions.BadFileTypeException;
import MusicApp.Exceptions.ID3TagException;
import MusicApp.Models.Metadata;
import MusicApp.Models.PlaylistManager;
import MusicApp.Models.Song;
import MusicApp.Views.MainLibraryView;
import MusicApp.Utils.MetadataReader;
import MusicApp.Utils.MusicLoader;
import javafx.beans.binding.BooleanBinding;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * The controller for the Main Library view.
 */
public class MainLibraryController extends PlayListController<MainLibraryView, MainLibraryController> {
    private int currentIndex;
    private final PlaylistManager playlistManager;

    /**
     * Instantiates a new Main library controller.
     *
     * @param controller the controller
     */
    public MainLibraryController(PlayerController controller) {
        super(new MainLibraryView(),controller);
        this.playlistManager = new PlaylistManager(library);
        initView("/fxml/MainLibrary.fxml");
    }

    /**
     * Loads the library with some sample songs from a settings folder
     *
     * @param folderPath the folder path
     */
    public void loadLibrary(Path folderPath) {
        List<Path> songs;
        try {
            songs = MusicLoader.getAllSongPaths(folderPath);
        } catch (IOException e) {
            view.displayError("Error while loading library: " + e.getMessage() + " \n Song list initialized empty");
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
                System.err.println("Error while reading metadata: " + e.getMessage());
            } catch (BadFileTypeException e) {
                System.err.println("Bad file type: " + e.getMessage());
            }
        }
        if (this.view != null) {
            updateListView();
        }
    }

    /**
     * Skip to the next song in the library.
     */
    public void skip(){
        if (currentIndex < library.size() - 1) {
            this.playSong(currentIndex+1);
        }
    }

    /**
     * Go back to the previous song in the library.
     */
    public void prec() {
        if (currentIndex > 0) {
            this.playSong(currentIndex-1);
        }
    }


    /**
     * Search the library for songs that match the query.
     *
     * @param query The query to search for.
     * @return A list of song names that match the query.
     */
    public List<Song> searchLibrary(String query) {
        return library.search(query.toLowerCase());
    }

    /**
     * Gets the boolean binding of the selected song.
     *
     * @return the boolean binding
     */
    public BooleanBinding isSelected() {
        return view.isSelected();
    }

    /**
     * Gets selected song.
     *
     * @return the selected song
     */
    public Song getSelectedSong() {
        return getSong(view.getSelectedSongIndex());
    }

    /**
     * Update list view.
     */
    public void updateListView() {
        view.updateListView();
    }

    private int getSongIndex(Song song){
        return library.toList().indexOf(song);
    }

    /**
     * Go to song.
     *
     * @param song the song
     */
    public void goToSong(Song song){
        int index = getSongIndex(song);
        if (index != -1){
            playSong(index);
        }
    }

    /**
     * Toggle the shuffle mode.
     *
     * @param isEnabled The shuffle button state.
     */
    public void toggleShuffle(boolean isEnabled) {
        Song currentSong = getSong(currentIndex);
        playlistManager.toggleShuffle(isEnabled, currentSong);
        if (currentSong != null) {
            int newIndex = isEnabled ? 0 : playlistManager.getOriginalIndex(currentSong);
            if (newIndex != -1) {
                this.playSong(newIndex);
            }
        }
        updateListView();
    }

    @Override
    protected void playSong(int index) {
        currentIndex = index;
        super.playSong(index);
    }

    /**
     * Clear queue selection.
     */
    public void clearQueueSelection(){
        playerController.clearQueueSelection();
    }
}
