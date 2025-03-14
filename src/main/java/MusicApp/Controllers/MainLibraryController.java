package MusicApp.Controllers;


import MusicApp.Exceptions.BadFileTypeException;
import MusicApp.Exceptions.ID3TagException;
import MusicApp.Models.Library;
import MusicApp.Models.Metadata;
import MusicApp.Models.PlaylistManager;
import MusicApp.Models.Song;
import MusicApp.Views.MainLibraryView;
import MusicApp.utils.MetadataReader;
import MusicApp.utils.MusicLoader;
import javafx.beans.binding.BooleanBinding;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class MainLibraryController extends PlayListController<MainLibraryView, MainLibraryController> {
    private int currentIndex;
    private final PlaylistManager playlistManager;

    public MainLibraryController(PlayerController controller) {
        super(new MainLibraryView(),controller);
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
            updateListView();
        }

    }

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

    public BooleanBinding isSelected() {
        return view.isSelected();
    }

    public Song getSelectedSong() {
        return getSong(view.getSelectedSongIndex());
    }

    public void updateListView() {
        view.updateListView();
    }

    private int getSongIndex(Song song){
        return library.toList().indexOf(song);
    }

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

    public void clearQueueSelection(){
        playerController.clearQueueSelection();
    }
}
