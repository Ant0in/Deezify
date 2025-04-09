package musicApp.controllers.songs;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import musicApp.controllers.LibraryController;
import musicApp.controllers.ViewController;
import musicApp.models.Library;
import musicApp.models.Metadata;
import musicApp.models.Song;
import musicApp.utils.MetadataUtils;
import musicApp.views.songs.SongCellView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SongCellController extends ViewController<SongCellView, SongCellController> {

    private final LibraryController LibraryController;
    private Song song;

    public SongCellController(LibraryController LibraryController) {
        super(new SongCellView());
        this.LibraryController = LibraryController;
        initView("/fxml/SongCell.fxml");
    }

    /**
     * Update the song in the view.
     *
     * @param newSong the new song
     */
    public void update(Song newSong) {
        if (!newSong.equals(getSong())) {
            this.song = newSong;
            this.view.update(song);
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

    public void toggleFavorites() {
        LibraryController.toggleFavorites(song);
        view.update(song);
    }

    public boolean isFavorite() {
        return LibraryController.isFavorite(song);
    }


    public void handleEditMetadata(String title, String artist, String genre, ArrayList<String> userTags, String coverPath) {

        if (song == null) {
            view.displayError("No song to edit");
            return;
        }
        try {
            Metadata newMetadata = song.getMetadata();
            newMetadata.setTitle(title);
            newMetadata.setArtist(artist);
            newMetadata.setGenre(genre);
            newMetadata.setUserTags(userTags);
            newMetadata.loadCoverFromPath(coverPath);
            MetadataUtils util = new MetadataUtils();

            util.setMetadata(newMetadata, song.getFilePath().toFile());
        } catch (Exception e) {
            e.printStackTrace();
            view.displayError(e.getMessage());
            return;
        }

        // update the view
        song.reloadMetadata();
        view.update(song);
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

    public void refreshSong() {
        view.update(song);
    }

    public Optional<String> getArtistAutoCompletion(String input) {
        return LibraryController.getArtistAutoCompletion(input);
    }

    public Optional<String> getTagAutoCompletion(String input) {
        return LibraryController.getTagAutoCompletion(input);
    }
}
