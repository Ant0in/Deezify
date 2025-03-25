
package MusicApp.controllers;


import java.util.ArrayList;

import MusicApp.models.Metadata;
import MusicApp.models.Song;
import MusicApp.utils.MetadataUtils;
import MusicApp.views.SongView;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;

public class SongController extends ViewController<SongView, SongController>{

    private final MainLibraryController mainLibraryController;
    private Song song;

    public SongController(MainLibraryController mainLibraryController) {
        super(new SongView());
        this.mainLibraryController = mainLibraryController;
        initView("/fxml/Song.fxml");
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

    /**
     * Handle when the user wants to edit the metadata of the song.
     * Leave the field to `null` if you don't want to change it.
     *
     * @param title the title
     * @param artist the artist
     * @param genre the genre
     */
    public void handleEditMetadata(String title, String artist, String genre, ArrayList<String> userTags, String coverPath){

        if (song == null) {
            view.displayError("No song to edit");
            return;
        }
        try {
            Metadata newMetadata = song.getMetadata();
            newMetadata.setTitle(title);
            newMetadata.setArtist(artist);
            newMetadata.setGenre(genre);
            newMetadata.setUserTags(userTags);
            newMetadata.loadCoverFromPath(coverPath);
        MetadataUtils util = new MetadataUtils();

            util.setMetadata(newMetadata, song.getFilePath().toFile());
        } catch (Exception e) {
            e.printStackTrace();
            view.displayError(e.getMessage());
            return;
        }

        // update the view
        song.reloadMetadata();
        view.update(song);
    }
    
}
