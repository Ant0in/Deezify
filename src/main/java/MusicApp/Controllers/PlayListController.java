package MusicApp.Controllers;


import MusicApp.Exceptions.BadFileTypeException;
import MusicApp.Exceptions.ID3TagException;
import MusicApp.Models.Library;
import MusicApp.Models.Metadata;
import MusicApp.Models.PlaylistManager;
import MusicApp.Models.Song;
import MusicApp.Views.PlayListView;
import MusicApp.utils.MetadataReader;
import MusicApp.utils.MusicLoader;
import javafx.beans.binding.BooleanBinding;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class PlayListController extends ViewController<PlayListView, PlayListController> {
    private final PlayerController playerController;
    private final Library library;
    private int currentIndex;
    private final PlaylistManager playlistManager;

    public PlayListController(PlayerController controller) {
        super(new PlayListView());
        this.playerController = controller;
        this.library = new Library();
        this.playlistManager = new PlaylistManager(library);
        initView("/fxml/PlayList.fxml");
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
        if (this.view != null) {
            updatePlayListView();
        }

    }

    public void skip(){
        if (currentIndex < library.size() - 1) {
            currentIndex++;
            this.playerController.playSong(getCurrentSong());
        }
    }

    /**
     * Go back to the previous song in the library.
     */
    public void prec() {
        if (currentIndex > 0) {
            currentIndex--;
            this.playerController.playSong(getCurrentSong());
        }
    }

    /**
     * Go to a specific song in the library.
     *
     * @param index The index of the song in the library.
     */
    public void goTo(int index) {
        if (index >= 0 && index < library.size()) {
            currentIndex = index;
            this.playerController.playSong(getCurrentSong());
        }
    }

    /**
     * Get the current song.
     *
     * @return The current song.
     */
    public Song getCurrentSong() {
        if (currentIndex < 0 || currentIndex >= library.size()) {
            return null;
        }
        return library.get(currentIndex);
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

    public BooleanBinding isSelected() {
        return view.isSelected();
    }

    public void clearSelection() {
        view.clearSelection();
    }

    public Song getSelectedSong() {
        return getSong(view.getSelectedSongIndex());
    }

    public void updatePlayListView() {
        view.updatePlayListView();
    }

    public Song getSong(int index) {
        return library.get(index);
    }

    private int getSongIndex(Song song){
        return library.toList().indexOf(song);
    }

    public void goToSong(Song song){
        int index = getSongIndex(song);
        if (index != -1){
            goTo(index);
        }
    }

    /**
     * Toggle the shuffle mode.
     *
     * @param isEnabled The shuffle button state.
     */
    public void toggleShuffle(boolean isEnabled) {
        Song currentSong = getCurrentSong();
        playlistManager.toggleShuffle(isEnabled, currentSong);
        if (currentSong != null) {
            int newIndex = isEnabled ? 0 : playlistManager.getOriginalIndex(currentSong);
            if (newIndex != -1) {
                currentIndex = newIndex;
                goTo(currentIndex);
            }
        }
        updatePlayListView();
    }

    public void handlePlaySong(){
        int songIndex = view.getSelectedSongIndex();
        if (songIndex != -1){
            goTo(songIndex);
        }
        clearSelection();
    }

    public List<Song> libraryToList(){
        return library.toList();
    }

    public void clearQueueSelection(){
        playerController.clearQueueSelection();
    }
}
