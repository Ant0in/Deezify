package MusicApp.Controllers;

import MusicApp.Models.Queue;
import MusicApp.Models.Song;
import MusicApp.Views.QueueView;
import javafx.beans.binding.BooleanBinding;

import java.util.List;

public class QueueController extends ViewController<QueueView, QueueController> {
    private final Queue queue;
    PlayerController playerController;


    public QueueController(PlayerController playerController) {
        super(new QueueView());
        this.queue = new Queue();
        this.playerController = playerController;
        initView("/fxml/Queue.fxml");
    }

    public BooleanBinding isPlaylistItemSelected() {
        return playerController.isPlaylistItemSelected();
    }

    public List<Song> queueToList(){
        return queue.toList();
    }

    /**
     * Reorganize the queue by moving a song from one index to another.
     *
     * @param fromIndex The initial index of the song.
     * @param toIndex   The index where the song should be placed.
     */
    public void reorderQueue(int fromIndex, int toIndex) {
        if (fromIndex >= 0 && fromIndex < queue.size() && toIndex >= 0 && toIndex <= queue.size()) {
            Song song = queue.get(fromIndex);
            queue.remove(song);
            queue.add(toIndex, song);
        }
    }

    public void clearPlayListViewSelection(){
        playerController.clearPlayListViewSelection();
    }

    public void handlePlaySong(){
        int songIndex = view.getSelectedSongIndex();
        if (songIndex != -1){
            playSong(songIndex);
        }
    }

    private Song getSong(int index){
        if (index < 0 || index >= queue.size()){
            return null;
        }
        return queue.get(index);
    }

    public void playSong(int songIndex){
        Song song = getSong(songIndex);
        queue.remove(song);
        playerController.playSong(song);
        view.updateQueueListView();
        clearSelection();
    }

    public void clearSelection() {
        view.clearSelection();
    }

    public void handleAddSong(){
        Song song = playerController.getSelectedPlayListSong();
        queue.add(song);
        this.view.updateQueueListView();
    }

    public void handleDeleteSong(){
        Song song = queue.get(this.view.getSelectedSongIndex());
        queue.remove(song);
        this.view.updateQueueListView();
    }

    public void handleClearQueue(){
        queue.clear();
        this.view.updateQueueListView();
    }

    public void refreshUI(){
        this.view.refreshUI();
    }

    public boolean queueIsEmpty(){
        return queue.isEmpty();
    }
}
