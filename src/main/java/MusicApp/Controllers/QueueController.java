package MusicApp.Controllers;

import MusicApp.Models.Queue;
import MusicApp.Models.Song;
import MusicApp.Views.QueueView;
import javafx.beans.binding.BooleanBinding;

import java.util.List;

public class QueueController extends PlayListController<QueueView, QueueController> {

    public QueueController(PlayerController playerController) {
        super(new QueueView(), playerController);
        initView("/fxml/Queue.fxml");
    }

    public BooleanBinding isPlaylistItemSelected() {
        return playerController.isPlaylistItemSelected();
    }


    /**
     * Reorganize the queue by moving a song from one index to another.
     *
     * @param fromIndex The initial index of the song.
     * @param toIndex   The index where the song should be placed.
     */
    public void reorderQueue(int fromIndex, int toIndex) {
        if (fromIndex >= 0 && fromIndex < library.size() && toIndex >= 0 && toIndex <= library.size()) {
            Song song = getSong(fromIndex);
            library.remove(song);
            library.add(toIndex, song);
        }
    }

    public void clearPlayListViewSelection(){
        playerController.clearPlayListViewSelection();
    }


    @Override
    protected void playSong(Song song){
        library.remove(song);
        super.playSong(song);
        this.view.updateListView();
    }

    public void handleAddSong(){
        Song song = playerController.getSelectedPlayListSong();
        library.add(song);
        this.view.updateListView();
    }

    public void handleDeleteSong(){
        Song song = library.get(this.view.getSelectedSongIndex());
        library.remove(song);
        this.view.updateListView();
    }

    public void handleClearQueue(){
        library.clear();
        this.view.updateListView();
    }

    public void refreshUI(){
        this.view.refreshUI();
    }

    public boolean queueIsEmpty(){
        return library.isEmpty();
    }
}
