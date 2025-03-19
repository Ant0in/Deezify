
package musicApp.controllers;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import musicApp.models.Song;
import musicApp.views.SongCellView;

public class SongCellController extends ViewController<SongCellView, SongCellController>{

    private final MainLibraryController mainLibraryController;
    private Song song;

    public SongCellController(MainLibraryController mainLibraryController) {
        super(new SongCellView());
        this.mainLibraryController = mainLibraryController;
        initView("/fxml/SongCell.fxml");
    }

    /**
     * Update the song in the view.
     *
     * @param newSong the new song
     */
    public void update(Song newSong){
        if (!newSong.equals(getSong())) {
            this.song = newSong;
            this.view.update(song);
        }
    }

    /**
     * Get the song.
     *
     * @return the song
     */
    public Song getSong(){
        return song;
    }

    /**
     * Is the song loaded.
     *
     * @return True if the song is loaded, false otherwise.
     */
    public boolean isLoaded(){
        Song playingSong = mainLibraryController.getCurrentlyLoadedSong();
        return playingSong != null && playingSong.equals(song);
    }

    /**
     * Is the song playing.
     *
     * @return True if the song is playing, false otherwise.
     */
    public boolean isPlaying() {
        return mainLibraryController.isPlaying();
    }

    /**
     * Handle when the user wants to play the song.
     */
    public void handlePlay(){
        if (song == null) {
            view.displayError("No song to play");
            return;
        }
        mainLibraryController.playSong(song);
    }

    /**
     * Handle when the user wants to pause the song.
     */
    public void handlePause(){
        mainLibraryController.pause();
    }

    /**
     * Handle when the user wants to unpause the song.
     */
    public void handleUnpause() {
        mainLibraryController.unpause();
    }

    /**
     * Get the currently loaded song string property.
     *
     * @return The currently loaded song string property.
     */
    public StringProperty getCurrentlyLoadedSongStringProperty(){
        return mainLibraryController.getCurrentlyLoadedSongStringProperty();
    }

    /**
     * Get the isPlaying property.
     * True if unpaused, false if paused.
     *
     * @return The is playing property.
     */
    public BooleanProperty isPlayingProperty(){
        return mainLibraryController.isPlayingProperty();
    }
}
