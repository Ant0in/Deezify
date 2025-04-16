package musicApp.controllers;

import javafx.scene.control.Alert;
import musicApp.exceptions.BadSongException;
import musicApp.models.Library;
import musicApp.models.Song;
import musicApp.views.SongContainerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract container class for all classes that will contain Songs.
 */
public abstract class SongContainerController<V extends SongContainerView, M extends Library>
        extends ViewController<V>
        implements SongContainerView.SongContainerViewListener {

    /**
     * The Player controller.
     */
    protected PlayerController playerController;
    /**
     * The Library.
     */
    protected M library;

    /**
     * Instantiates a new SongContainer controller.
     *
     * @param view             the view
     * @param playerController the player controller
     */
    public SongContainerController(V view, PlayerController playerController) {
        super(view);
        view.setListener(this);
        library = (M) new Library(new ArrayList<>(), "??library??", null);
        this.playerController = playerController;
    }

    /**
     * Gets song.
     *
     * @param index the index
     * @return the song
     */
    protected Song getSong(int index) {
        if (index < 0 || index >= library.size()) {
            view.displayError("Invalid song index: " + index);
            return null;
        }

        return library.get(index);
    }

    /**
     * Play song.
     *
     * @param song the song
     */
    public void playSong(Song song) {
        if (playerController == null) {
            view.displayError("PlayerController is not initialized!");
            return;
        }
        if (song == null) {
            view.displayError("Cannot play a null song.");
            return;
        }
        try {
            playerController.playSong(song);
        } catch (BadSongException e) {
            alertService.showExceptionAlert(e, Alert.AlertType.ERROR);
        }
    }

    /**
     * Play song.
     *
     * @param index the index
     */
    protected void playSong(int index) {
        if (index < 0 || index >= library.size()) {
            view.displayError("Invalid song index: " + index);
            return;
        }
        Song song = getSong(index);
        if (song == null) {
            view.displayError("No song found at index: " + index);
            return;
        }
        playSong(song);
    }

    /**
     * Pause the playback of the current song.
     * Delegates the pause operation to the player controller.
     */
    public void pause() {
        playerController.pause();
    }

    /**
     * Resume playback of the current song if paused.
     * Delegates the unpause operation to the player controller.
     */
    public void unpause() {
        playerController.unpause();
    }

    /**
     * Check if a song is currently being played.
     *
     * @return true if the player is playing, false if paused or stopped.
     */
    public boolean isPlaying() {
        return playerController.isPlaying();
    }


    /**
     * Transforms the library into a list of Song.
     *
     * @return the list
     */
    public List<Song> toList() {
        return library.toList();
    }

    /**
     * Handle play song.
     */
    public void handlePlaySong() {
        int songIndex = view.getSelectedSongIndex();
        if (songIndex != -1) {
            playSong(songIndex);
        }
    }

    /**
     * Clear selection.
     */
    public void clearSelection() {
        view.clearSelection();
    }

    public Library getLibrary() {
        return library;
    }

    /**
     * Refresh ui.
     */
    public void refreshUI() {
        view.refreshUI();
    }
}