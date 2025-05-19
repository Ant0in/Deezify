package musicApp.controllers;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import musicApp.controllers.playlists.PlaylistNavigatorController;
import musicApp.controllers.songs.LyricsController;
import musicApp.exceptions.BadSongException;
import musicApp.exceptions.EqualizerGainException;
import musicApp.models.Library;
import musicApp.models.Song;
import musicApp.models.dtos.SettingsDTO;
import musicApp.services.LanguageService;
import musicApp.views.PlayerView;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Supplier;

/**
 * Controller class for the music player.
 * <p>
 * This class is responsible for managing the audio player and the song library.
 * It provides methods to play, pause, skip, and go back to the previous song.
 * It also allows to add songs to a queue and play them in the order they were added.
 */
public class PlayerController extends ViewController<PlayerView> implements PlayerView.PlayerViewListener {

    private final MetaController metaController;
    private MediaPlayerController mediaPlayerController;
    private ToolBarController toolBarController;
    private LibraryController libraryController;
    private QueueController queueController;
    private LyricsController lyricsController;
    private PlaylistNavigatorController playlistNavigatorController;

    /**
     * Constructor
     *
     * @param _metaController the meta controller
     * @param primaryStage    the primary stage
     * @param settingsDTO     the settings
     */

    public PlayerController(MetaController _metaController, Stage primaryStage, SettingsDTO settingsDTO) {
        super(new PlayerView(primaryStage));
        view.setListener(this);
        metaController = _metaController;
        initSubControllers(settingsDTO);
        initView("/fxml/MainLayout.fxml");
    }

    /**
     * Initializes all sub-controllers that are part of the player controller.
     */
    private void initSubControllers(SettingsDTO settingsDTO) {
        playlistNavigatorController = new PlaylistNavigatorController(this, settingsDTO.getMusicFolder(), settingsDTO.getUserMusicFolder());
        libraryController = new LibraryController(this, getMainLibrary());
        queueController = new QueueController(this);
        mediaPlayerController = new MediaPlayerController(this, settingsDTO.getBalance(), settingsDTO.getCrossfadeDuration(), settingsDTO.getEqualizerBands());
        toolBarController = new ToolBarController(this);
        lyricsController = new LyricsController(this);
    }

    /**
     * Show the player view.
     */
    public void show() {
        view.show();
    }

    /**
     * Play song.
     *
     * @param song the song
     * @throws BadSongException gets throwm if an error occured when trying to play the file contained byt the Song object
     */
    public void playSong(Song song) throws BadSongException {
        mediaPlayerController.playCurrent(song);
    }

    /**
     * Pause the song
     */
    public void pause() {
        mediaPlayerController.pause();
    }

    /**
     * Unpause the song
     */
    public void unpause() {
        mediaPlayerController.unpause();
    }

    /**
     * Stop the playback of the current song.
     */
    public void stopPlayback() {
        if (mediaPlayerController != null) {
            mediaPlayerController.close();
        }
    }

    /**
     * Skip to the next song in the library or the queue.
     * If the queue is not empty, the next song in the queue is played.
     * Otherwise, the next song in the library is played.
     */
    public void skip() {
        if (queueController.queueIsEmpty()) {
            libraryController.skip();
        } else {
            queueController.playSong(0);
        }
    }

    /**
     * Handle the previous song
     */
    public void handlePreviousSong() {
        libraryController.prec();
    }


    /**
     * Close the audio player.
     */
    public void close() {
        mediaPlayerController.close();
        view.close();
    }

    /**
     * Close the stage.
     */
    public void closeStage() {
        view.closeStage();
    }

    /**
     * Open the settings window.
     */
    public void openSettings() {
        metaController.switchScene(MetaController.Scenes.SETTINGS);
    }

    public void returnToUsersWindow() {
        stopPlayback();
        closeStage();
        LanguageService.getInstance().setLanguage(metaController.getDefaultLanguage());
        metaController.switchScene(MetaController.Scenes.USERSWINDOW);
    }

    /**
     * Refresh the UI.
     */
    public void refreshUI() {
        view.refreshUI();
        lyricsController.refreshUI();
        playlistNavigatorController.refreshUI();
        libraryController.refreshUI();
    }

    /**
     * Toggle the shuffle mode.
     */
    public void toggleShuffle(boolean isShuffle) {
        libraryController.toggleShuffle(isShuffle);
    }

    /**
     * Actions to do when the settings are changed
     *
     * @param newSettingsDTO The new settings.
     * @throws EqualizerGainException if the equalizer gain is invalid
     */
    public void onSettingsChanged(SettingsDTO newSettingsDTO) throws EqualizerGainException {
        mediaPlayerController.setBalance(newSettingsDTO.getBalance());
        mediaPlayerController.setCrossfadeDuration(newSettingsDTO.getCrossfadeDuration());
        mediaPlayerController.setEqualizerBands(newSettingsDTO.getEqualizerBands());
        playlistNavigatorController.updateUserMainLibrary(newSettingsDTO.getUserMusicFolder());
        playlistNavigatorController.updateUserPlaylists(newSettingsDTO.getUserPlaylistPath());
        // Update the music folder in case it changed
        if (newSettingsDTO.isMusicFolderChanged()) {
            playlistNavigatorController.updateMainLibrary(newSettingsDTO.getMusicFolder());
            libraryController.loadPlaylist(getMainLibrary());
        }
    }

    /**
     * Gets control panel root.
     *
     * @return the control panel root
     */
    public Pane getControlPanelRoot() {
        return mediaPlayerController.getRoot();
    }

    /**
     * Gets toolbar root.
     *
     * @return the toolbar root
     */
    public Pane getToolBarRoot() {
        return toolBarController.getRoot();
    }

    /**
     * Gets play list root.
     *
     * @return the play list root
     */
    public Pane getMainLibraryRoot() {
        return libraryController.getRoot();
    }

    /**
     * Gets play lists root.
     *
     * @return the play lists root
     */
    public Pane getPlaylistNavigatorRoot() {
        return playlistNavigatorController.getRoot();
    }

    /**
     * Gets queue root.
     *
     * @return the queue root
     */
    public Pane getQueueRoot() {
        return queueController.getRoot();
    }


    /**
     * Is playlist item selected boolean binding.
     *
     * @return the boolean binding
     */
    public BooleanBinding isPlaylistItemSelected() {
        return libraryController.isSelected();
    }

    /**
     * Clear play list view selection.
     */
    public void clearPlayListViewSelection() {
        libraryController.clearSelection();
    }

    /**
     * Gets selected play list song.
     *
     * @return the selected play list song
     */
    public Song getSelectedPlayListSong() {
        return libraryController.getSelectedSong();
    }

    /**
     * Clear queue selection.
     */
    public void clearQueueSelection() {
        queueController.clearSelection();
    }

    /**
     * Get the currently loaded song.
     *
     * @return The currently loaded song.
     */
    public Song getCurrentlyLoadedSong() {
        return mediaPlayerController.getLoadedSong();
    }

    /**
     * Returns if the player is currently playing a song.
     *
     * @return True if the player is playing a song, false if its paused.
     */
    public boolean isPlaying() {
        return mediaPlayerController.getIsPlayingProperty().get();
    }

    /**
     * Get the currently loaded song string property.
     *
     * @return The currently loaded song string property.
     */
    public StringProperty getCurrentlyLoadedSongStringProperty() {
        return mediaPlayerController.getCurrentSongProperty();
    }

    /**
     * Returns the property representing whether music is currently playing.
     * This can be observed by UI elements to reflect playback state (e.g., play/pause button).
     *
     * @return A BooleanProperty that is true if the music is playing, BooleanProperty false if paused.
     */
    public BooleanProperty getIsPlayingProperty() {
        return mediaPlayerController.getIsPlayingProperty();
    }

    /**
     * Show or hide the lyrics view depending on the given flag.
     *
     * @param show True to show lyrics, false to hide.
     */
    public void toggleLyrics(boolean show) {
        view.toggleLyrics(show);
    }

    /**
     * Returns the root pane of the lyrics section.
     * Useful for embedding or showing the lyrics view in different parts of the UI.
     *
     * @return The Pane containing the lyrics UI.
     */
    public Pane getLyricsRoot() {
        return lyricsController.getRoot();
    }

    /**
     * Retrieves the current playback position of the song.
     *
     * @return A Duration object representing the current timestamp in the playing track.
     */
    public Duration getCurrentSongTime() {
        return mediaPlayerController.getCurrentTime();
    }

    /**
     * Get the main library.
     *
     * @return the main library
     */
    public Library getMainLibrary() {
        return playlistNavigatorController.getMainLibrary();
    }

    /**
     * Get all the playlists of the user
     *
     * @return The list of playlists
     */
    public List<Library> getPlaylists() {
        return playlistNavigatorController.getPlaylists();
    }

    /**
     * Update the playlist shown in the mainLibrary
     */
    public void updateShownPlaylist(Library library) {
        libraryController.loadPlaylist(library);
    }

    /**
     * Append a playlist to the queue
     *
     * @param playlist The playlist to append
     */
    public void appendPlaylistToQueue(Library playlist) {
        queueController.appendPlaylistToQueue(playlist);
    }

    /**
     * Replace the queue with all the songs of a playlist
     *
     * @param playlist The playlist to replace the queue with
     */
    public void replaceQueue(Library playlist) {
        queueController.replaceQueue(playlist);
    }

    /**
     * Toggle the favorite status of a song
     * If the song is already a favorite, it will be removed from the favorites
     * If the song is not a favorite, it will be added to the favorites
     *
     * @param song The song to toggle the favorite status of
     */
    public void toggleFavorites(Song song) {
        playlistNavigatorController.toggleFavorites(song);
    }

    /**
     * Add a song to a playlist
     *
     * @param song     The song to add
     * @param playlist The playlist to add the song to
     */
    public void addSongToPlaylist(Song song, Library playlist) {
        playlistNavigatorController.addSongToPlaylist(song, playlist);
    }

    /**
     * Remove a song from a playlist
     *
     * @param song     The song to remove
     * @param playlist The playlist to remove the song from
     */
    public void removeSongFromPlaylist(Song song, Library playlist) {
        playlistNavigatorController.removeSongFromPlaylist(song, playlist);
    }

    /**
     * Return if a song is a favorite
     *
     * @param song The song to check
     * @return True if the song is a favorite, false otherwise
     */
    public boolean isFavorite(Song song) {
        return playlistNavigatorController.isFavorite(song);
    }

    /**
     * Returns whether the given library is the main library.
     *
     * @param library The library to check.
     * @return True if the library is the main library, false otherwise.
     */
    public boolean isMainLibrary(Library library) {
        return getMainLibrary().equals(library);
    }

    /**
     * Get the next song supplier.
     *
     * @return The next song supplier.
     */
    public Supplier<Song> getNextSongSupplier() {
        if (queueController.queueIsEmpty()) {
            return libraryController::getNextSong;
        } else {
            return queueController::getNextSong;
        }
    }

    /**
     * Checks whether the given library is modifiable.
     * A modifiable library can have songs added or removed.
     *
     * @param library The library to check.
     * @return true if the library is modifiable, false otherwise.
     */
    public boolean isModifiable(Library library) {
        return playlistNavigatorController.isModifiable(library);
    }

    /**
     * Get the UserPlaylistPath
     *
     * @return The path to the user playlist
     */
    public Path getUserPlaylistPath() {
        return metaController.getUserPlaylistPath();
    }

    public boolean isUserLibrary(Library library) {
        return playlistNavigatorController.isUserLibrary(library);
    }

    public Path getUserLibraryPath() {
        return metaController.getUserMusicFolder();
    }

    public Path getMainLibraryPath() {
        return metaController.getMusicFolder();
    }
}