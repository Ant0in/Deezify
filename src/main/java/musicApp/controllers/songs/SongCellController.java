package musicApp.controllers.songs;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import musicApp.controllers.LibraryController;
import musicApp.controllers.ViewController;
import musicApp.models.Library;
import musicApp.models.Metadata;
import musicApp.models.Song;
import musicApp.services.MetadataService;
import musicApp.views.songs.SongCellView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The type Song cell controller.
 */
public class SongCellController extends ViewController<SongCellView, SongCellController> {

    private final LibraryController LibraryController;
    private Song song;
    private final SongContextMenuController contextMenuController;


    /**
     * Instantiates a new Song cell controller.
     *
     * @param controller the controller
     */
    public SongCellController(LibraryController controller) {
        super(new SongCellView());
        LibraryController = controller;
        contextMenuController = new SongContextMenuController(this);
        initView("/fxml/SongCell.fxml");

    }

    /**
     * Update the song in the view.
     *
     * @param newSong the new song
     */
    public void update(Song newSong) {
        if (!newSong.equals(getSong())) {
            song = newSong;
            view.update(song);
        }
    }

    /**
     * Get the song.
     *
     * @return the song
     */
    public Song getSong() {
        return song;
    }

    public void showContextMenu(double x, double y) {
        contextMenuController.showAt(x,y);
    }

    /**
     * Is the song loaded.
     *
     * @return True if the song is loaded, false otherwise.
     */
    public boolean isLoaded() {
        Song playingSong = LibraryController.getCurrentlyLoadedSong();
        return playingSong != null && playingSong.equals(song);
    }

    /**
     * Is the song playing.
     *
     * @return True if the song is playing, false otherwise.
     */
    public boolean isPlaying() {
        return LibraryController.isPlaying();
    }

    /**
     * Handle when the user wants to play the song.
     */
    public void handlePlay() {
        if (song == null) {
            view.displayError("No song to play");
            return;
        }
        LibraryController.playSong(song);
    }

    /**
     * Handle when the user wants to pause the song.
     */
    public void handlePause() {
        LibraryController.pause();
    }

    /**
     * Handle when the user wants to unpause the song.
     */
    public void handleUnpause() {
        LibraryController.unpause();
    }

    /**
     * Get the currently loaded song string property.
     *
     * @return The currently loaded song string property.
     */
    public StringProperty getCurrentlyLoadedSongStringProperty() {
        return LibraryController.getCurrentlyLoadedSongStringProperty();
    }

    /**
     * Get the isPlaying property.
     * True if unpaused, false if paused.
     *
     * @return The is playing property.
     */
    public BooleanProperty isPlayingProperty() {
        return LibraryController.isPlayingProperty();
    }

    /**
     * Toggle favorites.
     */
    public void toggleFavorites() {
        LibraryController.toggleFavorites(song);
        view.update(song);
    }

    /**
     * Is favorite boolean.
     *
     * @return the boolean
     */
    public boolean isFavorite() {
        return LibraryController.isFavorite(song);
    }

    /**
     * Get all available playlists.
     *
     * @return List of all playlists
     */
    public List<Library> getPlaylists() {
        return LibraryController.getPlaylists();
    }

    /**
     * Add the current song to a playlist.
     *
     * @param playlist The playlist to add the song to
     */
    public void addSongToPlaylist(Library playlist) {
        if (song == null) {
            view.displayError("No song to add");
            return;
        }
        LibraryController.addSongToPlaylist(song, playlist);
    }

    /**
     * remove the song from specified playlist.
     *
     * @param playlist The playlist to remove the song from
     */
    public void removeSongFromPlaylist(Library playlist) {
        if (song == null) {
            view.displayError("No song to remove");
            return;
        }
        LibraryController.removeSongFromPlaylist(song, playlist);
    }

    /**
     * remove the song from current playlist.
     */
    public void removeSongFromPlaylist() {
        if (song == null) {
            view.displayError("No song to remove");
            return;
        }
        LibraryController.removeSongFromPlaylist(song);
    }

    /**
     * Is showing main library boolean.
     *
     * @return the boolean
     */
    public boolean isShowingMainLibrary() {
        return LibraryController.isShowingMainLibrary();
    }

    /**
     * Check if the song is in the playlist.
     *
     * @param playlist The playlist to check
     * @return True if the song is in the playlist, false otherwise.
     */
    public boolean containsSong(Library playlist) {
        return playlist.toList().contains(song);
    }

    /**
     * Open the metadata editor.
     */
    public void openMetadataEditor() {
        new EditMetadataController(this);
    }

    /**
     * Refresh song.
     */
    public void refreshSong() {
        view.update(song);
    }

    /**
     * Gets artist auto-completion.
     *
     * @param input the input
     * @return the artist auto-completion
     */
    public Optional<String> getArtistAutoCompletion(String input) {
        return LibraryController.getArtistAutoCompletion(input);
    }

    /**
     * Gets album auto completion.
     *
     * @param input the input
     * @return the album auto-completion
     */
    public Optional<String> getAlbumAutoCompletion(String input) {
        return LibraryController.getAlbumAutoCompletion(input);
    }

    /**
     * Gets tag auto completion.
     *
     * @param input the input
     * @return the tag auto completion
     */
    public Optional<String> getTagAutoCompletion(String input) {
        return LibraryController.getTagAutoCompletion(input);
    }
}
