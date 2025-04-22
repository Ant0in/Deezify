package musicApp.controllers.songs;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.util.Duration;
import musicApp.controllers.LibraryController;
import musicApp.controllers.ViewController;
import musicApp.models.Library;
import musicApp.models.Song;
import musicApp.views.songs.SongCell;
import musicApp.views.songs.SongCellView;

import java.util.List;
import java.util.Optional;


/**
 * The type Song cell controller.
 */
public class SongCellController extends ViewController<SongCellView> implements SongCellView.SongCellViewListener, SongCell.SongCellListener {

    private final LibraryController libraryController;
    private Song song;
    private final SongContextMenuController contextMenuController;


    /**
     * Instantiates a new Song cell controller.
     *
     * @param controller the controller
     */
    public SongCellController(LibraryController controller) {
        super(new SongCellView());
        view.setListener(this);
        libraryController = controller;
        contextMenuController = new SongContextMenuController(this);
        initView("/fxml/SongCell.fxml");

    }

    /**
     * Update the song in the view.
     *
     * @param newSong the new song
     */
    public void update(Song newSong) {
        if (!newSong.equals(song)) {
            song = newSong;
        }
        view.update();
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
        Song playingSong = libraryController.getCurrentlyLoadedSong();
        return playingSong != null && playingSong.equals(song);
    }

    /**
     * Is the song playing.
     *
     * @return True if the song is playing, false otherwise.
     */
    public boolean isPlaying() {
        return libraryController.isPlaying();
    }

    /**
     * Handle when the user wants to play the song.
     */
    public void handlePlay() {
        if (song == null) {
            view.displayError("No song to play");
            return;
        }
        libraryController.playSong(song);
    }

    /**
     * Handle when the user wants to pause the song.
     */
    public void handlePause() {
        libraryController.pause();
    }

    /**
     * Handle when the user wants to unpause the song.
     */
    public void handleUnpause() {
        libraryController.unpause();
    }

    public void handleLoadedSongChange(Runnable callback) {
        libraryController.getCurrentlyLoadedSongStringProperty().
            addListener((_, _, _) -> callback.run());
    }

    public void handlePlayingStatusChange(Runnable callback) {
        libraryController.getIsPlayingProperty().
            addListener((_, _, _) -> callback.run());
    }

    public Image getSongCoverImage() {
        return song.getCoverImage();
    }

    public String getSongTitle() {
        return song.getTitle();
    }

    public String getSongArtist() {
        return song.getArtist();
    }

    public String getSongGenre() {
        return song.getGenre();
    }

    public Duration getSongDuration() {
        return song.getDuration();
    }

    /**
     * Get the currently loaded song string property.
     *
     * @return The currently loaded song string property.
     */
    public StringProperty getCurrentlyLoadedSongStringProperty() {
        return libraryController.getCurrentlyLoadedSongStringProperty();
    }

    /**
     * Get the isPlaying property.
     * True if unpaused, false if paused.
     *
     * @return The is playing property.
     */
    public BooleanProperty isPlayingProperty() {
        return libraryController.getIsPlayingProperty();
    }

    /**
     * Toggle favorites.
     */
    public void toggleFavorites() {
        libraryController.toggleFavorites(song);
        view.update();
    }

    /**
     * Is favorite boolean.
     *
     * @return the boolean
     */
    public boolean isFavorite() {
        return libraryController.isFavorite(song);
    }

    /**
     * Get all available playlists.
     *
     * @return List of all playlists
     */
    public List<Library> getPlaylists() {
        return libraryController.getPlaylists();
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
        libraryController.addSongToPlaylist(song, playlist);
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
        libraryController.removeSongFromPlaylist(song, playlist);
    }

    /**
     * remove the song from current playlist.
     */
    public void removeSongFromPlaylist() {
        if (song == null) {
            view.displayError("No song to remove");
            return;
        }
        libraryController.removeSongFromPlaylist(song);
    }

    /**
     * Is showing main library boolean.
     *
     * @return the boolean
     */
    public boolean isShowingMainLibrary() {
        return libraryController.isShowingMainLibrary();
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
        view.update();
    }

    /**
     * Gets artist auto-completion.
     *
     * @param input the input
     * @return the artist auto-completion
     */
    public Optional<String> getArtistAutoCompletion(String input) {
        return libraryController.getArtistAutoCompletion(input);
    }

    /**
     * Gets album auto completion.
     *
     * @param input the input
     * @return the album auto-completion
     */
    public Optional<String> getAlbumAutoCompletion(String input) {
        return libraryController.getAlbumAutoCompletion(input);
    }

    /**
     * Gets tag auto completion.
     *
     * @param input the input
     * @return the tag auto completion
     */
    public Optional<String> getTagAutoCompletion(String input) {
        return libraryController.getTagAutoCompletion(input);
    }
}
