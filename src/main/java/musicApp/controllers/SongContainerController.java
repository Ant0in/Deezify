package musicApp.controllers;

import musicApp.models.Library;
import musicApp.models.Song;
import musicApp.views.SongContainerView;

import java.nio.file.Path;
import java.util.List;

/**
 * Abstract container class for all classes that will contain Songs.
 *
 * @param <V> the View class
 * @param <C> the Controller class
 */
public abstract class SongContainerController<V extends SongContainerView<V, C, M>, C extends SongContainerController<V, C, M>, M extends Library>
        extends ViewController<V, C> {

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
    protected void playSong(Song song) {
        if (playerController == null) {
            view.displayError("PlayerController is not initialized!");
            return;
        }
        if (song == null) {
            view.displayError("Cannot play a null song.");
            return;
        }
        this.playerController.playSong(song);
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
        Song song = this.getSong(index);
        if (song == null) {
            view.displayError("No song found at index: " + index);
            return;
        }
        playSong(song);
    }

    protected void pause() {
        playerController.pause();
    }

    protected void unpause() {
        playerController.unpause();
    }

    protected boolean isPlaying() {
        return playerController.isPlaying();
    }


    /**
     * To list list.
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
        this.view.clearSelection();
    }

    public Song getSongByPath(Path path) {
        return library.getSongByPath(path);
    }
}

