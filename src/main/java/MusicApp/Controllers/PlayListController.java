package MusicApp.Controllers;

import MusicApp.Models.Library;
import MusicApp.Models.Song;
import MusicApp.Views.PlayListView;

import java.util.List;

public abstract class PlayListController<V extends PlayListView<V, C>, C extends PlayListController<V, C>>
        extends ViewController<V, C> {

    protected PlayerController playerController;
    protected final Library library;

    public PlayListController(V view, PlayerController playerController) {
        super(view);
        this.playerController = playerController;
        this.library = new Library();
    }

    protected Song getSong(int index) {
        if (index < 0 || index >= library.size()) {
            view.displayError("Invalid song index: " + index);
            return null;
        }

        return library.get(index);
    }

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


    public List<Song> toList(){
        return library.toList();
    }

    public void handlePlaySong(){
        int songIndex = view.getSelectedSongIndex();
        if (songIndex != -1){
            playSong(songIndex);
        }
    }

    public void clearSelection() {
        this.view.clearSelection();
    }
}

