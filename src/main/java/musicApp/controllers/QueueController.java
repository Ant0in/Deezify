package musicApp.controllers;

import javafx.beans.binding.BooleanBinding;
import musicApp.models.Library;
import musicApp.models.Song;
import musicApp.views.QueueView;

/**
 * The type Queue controller.
 */
public class QueueController extends SongContainerController<QueueView, QueueController, Library> {

    /**
     * Instantiates a new Queue controller.
     *
     * @param playerController the player controller
     */
    public QueueController(PlayerController playerController) {
        super(new QueueView(), playerController);
        initView("/fxml/Queue.fxml");
    }

    /**
     * Is playlist item selected boolean binding.
     *
     * @return the boolean binding
     */
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

    /**
     * Clear play list view selection.
     */
    public void clearPlayListViewSelection(){
        playerController.clearPlayListViewSelection();
    }


    @Override
    protected void playSong(Song song){
        library.remove(song);
        super.playSong(song);
        this.view.updateListView();
    }

    /**
     * Handle add song.
     */
    public void handleAddSong(){
        Song song = playerController.getSelectedPlayListSong();
        try {
            library.add(song);
            this.view.updateListView();
        } catch (IllegalArgumentException e) {
            this.view.displayError("Song already in queue");
        }
    }

    /**
     * Handle delete song.
     */
    public void handleDeleteSong(){
        Song song = library.get(this.view.getSelectedSongIndex());
        library.remove(song);
        this.view.updateListView();
    }

    /**
     * Handle clear queue.
     */
    public void handleClearQueue(){
        library.clear();
        this.view.updateListView();
    }

    /**
     * Queue is empty boolean.
     *
     * @return the boolean
     */
    public boolean queueIsEmpty(){
        return library.isEmpty();
    }

    /**
     * Append playlist to queue.
     * TODO: Maybe refactor the library add to accept the same songs if queued
     *
     * @param playlist the playlist
     */
    public void appendPlaylistToQueue(Library playlist){
        for (Song song : playlist.toList()) {
            library.toList().add(song);
        }
        this.view.updateListView();
    }

    /**
     * Replace queue.
     *
     * @param playlist the playlist
     */
    public void replaceQueue(Library playlist){
        library.clear();
        appendPlaylistToQueue(playlist);
    }
}
