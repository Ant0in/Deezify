package musicApp.controllers;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import musicApp.models.Library;
import musicApp.models.Song;
import musicApp.views.LibraryView;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * The controller for the Main Library view.
 */
public class LibraryController extends SongContainerController<LibraryView, LibraryController, Library> {
    private int currentIndex;
    private Boolean shuffle;

    /**
     * Instantiates a new Main library controller.
     *
     * @param controller the controller
     */
    public LibraryController(PlayerController controller) {
        super(new LibraryView(), controller);
        shuffle = false;
        initView("/fxml/MainLibrary.fxml");
    }

    /**
     * Loads a new playlist into the library and updates the view.
     *
     * @param playlist The Library object representing the new playlist to load.
     */
    public void loadPlaylist(Library playlist) {
        library = playlist;
        view.updateListView();
    }

    /**
     * Skip to the next song in the library.
     */
    public void skip() {
        if (shuffle) {
            Random random = new Random();
            playSong(random.nextInt(library.size()));
        } else {
            if (currentIndex < library.size() - 1) {
                currentIndex++;
                playSong(currentIndex);
            }
        }
    }

    /**
     * Go back to the previous song in the library.
     */
    public void prec() {
        if (currentIndex > 0) {
            playSong(currentIndex - 1);
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

    /**
     * Retrieves the index of a given song in the music library.
     *
     * @param song The Song object whose index is to be found.
     * @return The index of the song in the library list, or -1 if the song is not found.
     */
    private int getSongIndex(Song song) {
        return library.toList().indexOf(song);
    }

    /**
     * Plays a song at the specified index in the library.
     *
     * @param index The index of the song to play.
     */
    @Override
    public void playSong(int index) {
        currentIndex = index;
        super.playSong(index);
    }

    /**
     * Plays the specified song.
     *
     * @param song The Song object to play.
     */
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
     * Toggle shuffle.
     */
    public void toggleShuffle() {
        shuffle = !shuffle;
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
        playerController.removeSongFromPlaylist(song, library);
        refreshUI();
    }

    /**
     * Checks if the main library is currently being shown
     */
    public boolean isShowingMainLibrary() {
        return playerController.getPlaylists().getFirst().equals(library);
    }


    /**
     * Handle add song.
     */
    public void handleAddSong() {
        playerController.handleAddSongToMainLibrary();
    }

    /**
     * Adds a song to the library.
     *
     * @param songPath The path to the song file to be added.
     */
    public void addSong(Path songPath) {
        Song song = new Song(songPath);
        library.add(song);
        view.updateListView();
    }

    /**
     * Provides artist name auto-completion based on user input.
     *
     * @param input The partial artist name entered by the user.
     * @return An Optional containing the completed artist name if found.
     */
    public Optional<String> getArtistAutoCompletion(String input) {
        return library.getArtistAutoCompletion(input);
    }

    /**
     * Provides tag auto-completion based on user input.
     *
     * @param input The partial tag entered by the user.
     * @return An Optional containing the completed tag if found.
     */
    public Optional<String> getTagAutoCompletion(String input) {
        return library.getTagAutoCompletion(input);
    }
}