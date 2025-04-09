package musicApp.controllers;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import musicApp.models.Library;
import musicApp.models.Song;
import musicApp.utils.MusicLoader;
import musicApp.views.LibraryView;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

/**
 * The controller for the Main Library view.
 */
public class LibraryController extends SongContainerController<LibraryView, LibraryController, Library> {
    private int currentIndex;
    private Boolean shuffle = false;

    /**
     * Instantiates a new Main library controller.
     *
     * @param controller the controller
     */
    public LibraryController(PlayerController controller) {
        super(new LibraryView(), controller);
        initView("/fxml/MainLibrary.fxml");
    }

    public void loadPlaylist(Library playlist) {
        this.library = playlist;
        view.updateListView();
    }

    /**
     * Skip to the next song in the library.
     */
    public void skip() {
        if (shuffle) {
            Random random = new Random();
            this.playSong(random.nextInt(library.size()));
        } else {
            if (currentIndex < library.size() - 1) {
                this.currentIndex++;
                this.playSong(this.currentIndex);
            }
        }
    }

    /**
     * Go back to the previous song in the library.
     */
    public void prec() {
        if (currentIndex > 0) {
            this.playSong(currentIndex - 1);
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

    private int getSongIndex(Song song) {
        return library.toList().indexOf(song);
    }

    /**
     * Go to song.
     *
     * @param song the song
     */
    public void goToSong(Song song) {
        int index = getSongIndex(song);
        if (index != -1) {
            playSong(index);
        }
    }

    @Override
    public void playSong(int index) {
        currentIndex = index;
        super.playSong(index);
    }

    @Override
    public void playSong(Song song) {
        currentIndex = getSongIndex(song);
        super.playSong(song);
    }

    /**
     * Clear queue selection.
     */
    public void clearQueueSelection() {
        playerController.clearQueueSelection();
    }

    /**
     * Is shuffle boolean.
     *
     * @return the boolean
     */
    public Boolean isShuffle() {
        return this.shuffle;
    }

    /**
     * Toggle shuffle.
     */
    public void toggleShuffle() {
        this.shuffle = !this.shuffle;
    }

    /**
     * Gets the currently loaded song.
     *
     * @return the currently loaded song
     */
    public Song getCurrentlyLoadedSong() {
        return playerController.getCurrentlyLoadedSong();
    }

    /**
     * Gets the currently loaded song string property.
     *
     * @return the currently loaded song string property
     */
    public StringProperty getCurrentlyLoadedSongStringProperty() {
        return playerController.getCurrentlyLoadedSongStringProperty();
    }

    /**
     * Returns if the player is currently playing a song.
     *
     * @return the boolean property
     */
    public BooleanProperty isPlayingProperty() {
        return playerController.isPlayingProperty();
    }

    /**
     * Toggle the favorites status of a song.
     * If the song is already a favorite, it will be removed from the favorites.
     * If the song is not a favorite, it will be added to the favorites.
     *
     * @param song the song
     */
    public void toggleFavorites(Song song) {
        playerController.toggleFavorites(song);
    }

    /**
     * Checks if a song is a favorite.
     *
     * @param song the song
     * @return the boolean
     */
    public boolean isFavorite(Song song) {
        return playerController.isFavorite(song);
    }

    /**
     * Get all the playlists
     *
     * @return the favorites list
     */
    public List<Library> getPlaylists() {
        return playerController.getPlaylists();
    }

    /**
     * Add a song to a playlist
     *
     * @param song     the song
     * @param playlist the playlist
     */
    public void addSongToPlaylist(Song song, Library playlist) {
        playerController.addSongToPlaylist(song, playlist);
    }

    /**
     * Remove a song from a playlist
     *
     * @param song     the song
     * @param playlist the playlist
     */
    public void removeSongFromPlaylist(Song song, Library playlist) {
        playerController.removeSongFromPlaylist(song, playlist);
        refreshUI();
    }

    /**
     * Remove a song from the main library
     *
     * @param song the song
     */
    public void removeSongFromPlaylist(Song song) {
        playerController.removeSongFromPlaylist(song, this.library);
        refreshUI();
    }

    /**
     * Checks if the main library is currently being shown
     */
    public boolean isShowingMainLibrary() {
        return playerController.getPlaylists().get(0).equals(library);
    }


    /**
     * Handle add song.
     */
    public void handleAddSong() {
        playerController.handleAddSongToMainLibrary();
    }

    /**
     * Add song to library.
     * @param songPath
     */
    public void addSong(Path songPath) {
        Song song = new Song(songPath);
        library.add(song);
        view.updateListView();
    }

}
