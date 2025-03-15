package musicApp.controllers;


import javafx.beans.property.StringProperty;
import musicApp.models.Song;
import musicApp.views.MainLibraryView;
import javafx.beans.binding.BooleanBinding;

import java.nio.file.Path;
import java.util.List;
import java.util.Random;

/**
 * The controller for the Main Library view.
 */
public class MainLibraryController extends PlayListController<MainLibraryView, MainLibraryController> {
    private int currentIndex;
    private Boolean shuffle = false;

    /**
     * Instantiates a new Main library controller.
     *
     * @param controller the controller
     */
    public MainLibraryController(PlayerController controller) {
        super(new MainLibraryView(),controller);
        initView("/fxml/MainLibrary.fxml");
    }

    /**
     * Loads the library with some sample songs from a settings folder
     *
     * @param folderPath the folder path
     */
    public void loadLibrary(Path folderPath) {
        library.load(folderPath);
        view.updateListView();
    }

    /**
     * Skip to the next song in the library.
     */
    public void skip(){
        if (shuffle) {
            Random random = new Random();
            this.playSong(random.nextInt(library.size()));
        } else {
            if (currentIndex < library.size() - 1) {
                this.playSong(currentIndex + 1);
            }
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

    public Boolean isShuffle() {
        return this.shuffle;
    }

    public void toggleShuffle() {
        this.shuffle = !this.shuffle;
    }

    public Song getCurrentlyPlayingSong() {
        return playerController.getCurrentlyPlayingSong();
    }
    public StringProperty getCurrentlyPlayingSongStringProperty(){
        return playerController.getCurrentlyPlayingSongStringProperty();
    }
}
