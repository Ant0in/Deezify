package musicApp.controllers;

import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.Alert;
import musicApp.models.Library;
import musicApp.models.Song;
import musicApp.views.LibraryView;
import musicApp.views.QueueView;

/**
 * Controller responsible for managing the song queue view and logic.
 * Handles adding, removing, reordering, and playing songs in the queue.
 */
public class QueueController extends SongContainerController<QueueView, Library> implements QueueView.QueueViewListener {

    /**
     * Instantiates a new Queue controller.
     *
     * @param playerController the player controller
     */
    public QueueController(PlayerController playerController) {
        super(new QueueView(), playerController);
        view.setListener((QueueView.QueueViewListener) this);
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
    public void clearPlayListViewSelection() {
        playerController.clearPlayListViewSelection();
    }

    /**
     * Plays the given song from the queue.
     * Removes the song from the queue before playing it,
     * then triggers the view to update the list visually.
     *
     * @param song The song to play.
     */
    @Override
    public void playSong(Song song) {
        library.remove(song);
        super.playSong(song);
        view.updateListView();
    }

    /**
     * Handle add song.
     */
    public void handleAddSong() {
        Song song = playerController.getSelectedPlayListSong();
        try {
            library.add(song);
            view.updateListView();
        } catch (IllegalArgumentException e) {
            alertService.showExceptionAlert(
                    e,
                    Alert.AlertType.INFORMATION
            );
        }
    }

    /**
     * Handle delete song.
     */
    public void handleDeleteSong() {
        Song song = library.get(view.getSelectedSongIndex());
        library.remove(song);
        view.updateListView();
    }

    /**
     * Handle clear queue.
     */
    public void handleClearQueue() {
        library.clear();
        view.updateListView();
    }

    /**
     * Queue is empty boolean.
     *
     * @return the boolean
     */
    public boolean queueIsEmpty() {
        return library.isEmpty();
    }

    /**
     * Append playlist to queue.
     *
     * @param playlist the playlist
     */
    public void appendPlaylistToQueue(Library playlist) {
        for (Song song : playlist.toList()) {
            library.toList().add(song);
        }
        view.updateListView();
    }

    /**
     * Replace queue.
     *
     * @param playlist the playlist
     */
    public void replaceQueue(Library playlist) {
        library.clear();
        appendPlaylistToQueue(playlist);
    }
}