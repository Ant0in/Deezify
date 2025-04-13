package musicApp.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Duration;
import musicApp.controllers.playlists.PlaylistNavigatorController;
import musicApp.controllers.songs.LyricsController;
import musicApp.utils.FileDialogHelper;
import musicApp.utils.FileManager;
import musicApp.views.PlayerView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import musicApp.models.Library;
import musicApp.models.Settings;
import musicApp.models.Song;

import java.util.List;

/**
 * Controller class for the music player.
 * <p>
 * This class is responsible for managing the audio player and the song library.
 * It provides methods to play, pause, skip, and go back to the previous song.
 * It also allows to add songs to a queue and play them in the order they were added.
 */
public class PlayerController extends ViewController<PlayerView, PlayerController> {

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
     * @param metaController the meta controller
     * @throws IOException the io exception
     */
    public PlayerController(MetaController metaController, Settings settings, Library mainLibrary) throws IOException {
        super(new PlayerView());
        this.metaController = metaController;
        initSubControllers();
        initView("/fxml/MainLayout.fxml");

        libraryController.loadPlaylist(mainLibrary);
        mediaPlayerController.setBalance(settings.getBalance());
        mediaPlayerController.setEqualizerBands(settings.getEqualizerBands());
    }

    /**
     * Initializes all sub-controllers that are part of the player controller.
     */
    private void initSubControllers() {
        libraryController = new LibraryController(this);
        queueController = new QueueController(this);
        mediaPlayerController = new MediaPlayerController(this);
        toolBarController = new ToolBarController(this);
        lyricsController = new LyricsController(this);
        playlistNavigatorController = new PlaylistNavigatorController(this);

    }

    /**
     * Show the player view.
     *
     * @param stage The stage to show the view on.
     */
    public void show(Stage stage) {
        view.show(stage);
    }

    /**
     * Play song.
     *
     * @param song the song
     */
    public void playSong(Song song) {
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
    }

    /**
     * Open the settings window.
     */
    public void openSettings() {
        metaController.switchScene(MetaController.Scenes.SETTINGS);
    }

    /**
     * Refresh the UI.
     */
    public void refreshUI() {
        view.refreshUI();
        queueController.refreshUI();
        lyricsController.refreshUI();
        playlistNavigatorController.refreshUI();
        libraryController.refreshUI();
    }

    /**
     * Toggle the shuffle mode.
     */
    public void toggleShuffle() {
        libraryController.toggleShuffle();
    }

    /**
     * Actions to do when the settings are changed
     *
     * @param newSettings The new settings.
     */
    public void onSettingsChanged(Settings newSettings) {
        mediaPlayerController.setBalance(newSettings.getBalance());
        mediaPlayerController.setEqualizerBands(newSettings.getEqualizerBands());
        if (newSettings.isMusicFolderChanged())
            libraryController.loadPlaylist(metaController.getMainLibrary());
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
        return mediaPlayerController.isPlaying().get();
    }

    /**
     * Get the currently loaded song string property.
     *
     * @return The currently loaded song string property.
     */
    public StringProperty getCurrentlyLoadedSongStringProperty() {
        return mediaPlayerController.currentSongProperty();
    }

    /**
     * Returns the property representing whether music is currently playing.
     * This can be observed by UI elements to reflect playback state (e.g., play/pause button).
     *
     * @return A BooleanProperty that is true if the music is playing, BooleanProperty false if paused.
     */
    public BooleanProperty isPlayingProperty() {
        return mediaPlayerController.isPlayingProperty();
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
     * Get all the playlists of the user
     *
     * @return The list of playlists
     */
    public List<Library> getPlaylists() {
        return metaController.getPlaylists();
    }

    /**
     * Update the playlist shown in the mainLibrary
     */
    public void updateShownPlaylist(Library library) {
        libraryController.loadPlaylist(library);
    }

    /**
     * Get the main library
     *
     * @return The main library
     */
    public Library getLibrary() {
        return libraryController.getLibrary();
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
     * Handle add song to main library.
     * This method opens a file dialog to select an audio file and adds it to the main library.
     */
    public void handleAddSongToMainLibrary() {
        File selectedFile = FileDialogHelper.chooseAudioFile(null, "Select Music File");
        if (selectedFile != null) {
            Path mainLibraryPath = metaController.getMusicDirectory();
            try {
                Path copiedFilePath = FileManager.copyFileToDirectory(selectedFile, mainLibraryPath);
                System.out.println("File copied with succes : " + copiedFilePath);
                libraryController.addSong(copiedFilePath);
            } catch (IOException e) {
                alertService.showExceptionAlert(e);
            }
        }
    }
}