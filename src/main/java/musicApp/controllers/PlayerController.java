package musicApp.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
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
    private LibraryController LibraryController;
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

        this.LibraryController.loadPlaylist(mainLibrary);
        this.mediaPlayerController.setBalance(settings.getBalance());
        this.mediaPlayerController.setEqualizerBands(settings.getEqualizerBands());
    }


    private void initSubControllers() {
        this.LibraryController = new LibraryController(this);
        this.queueController = new QueueController(this);
        this.mediaPlayerController = new MediaPlayerController(this);
        this.toolBarController = new ToolBarController(this);
        this.lyricsController = new LyricsController(this);
        this.playlistNavigatorController = new PlaylistNavigatorController(this);

    }

    /**
     * Show the player view.
     *
     * @param stage The stage to show the view on.
     */
    public void show(Stage stage) {
        this.view.show(stage);
    }

    /**
     * Play song.
     *
     * @param song the song
     */
    public void playSong(Song song) {
        this.mediaPlayerController.playCurrent(song);
    }

    /**
     * Pause the song
     */
    public void pause() {
        this.mediaPlayerController.pause();
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
        if (this.queueController.queueIsEmpty()) {
            this.LibraryController.skip();
        } else {
            this.queueController.playSong(0);
        }
    }

    /**
     * Handle the previous song
     */
    public void handlePreviousSong() {
        this.LibraryController.prec();
    }


    /**
     * Close the audio player.
     */
    public void close() {
        this.mediaPlayerController.close();
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
        this.queueController.refreshUI();
        this.lyricsController.refreshUI();
        this.playlistNavigatorController.refreshUI();
        this.LibraryController.refreshUI();
    }

    /**
     * Toggle the shuffle mode.
     */
    public void toggleShuffle() {
        this.LibraryController.toggleShuffle();
    }

    /**
     * Actions to do when the settings are changed
     *
     * @param newSettings The new settings.
     */
    public void onSettingsChanged(Settings newSettings) {
        this.mediaPlayerController.setBalance(newSettings.getBalance());
        this.mediaPlayerController.setEqualizerBands(newSettings.getEqualizerBands());
        if (newSettings.isMusicFolderChanged())
            this.LibraryController.loadPlaylist(metaController.getMainLibrary());
    }

    /**
     * Gets control panel root.
     *
     * @return the control panel root
     */
    public Pane getControlPanelRoot() {
        return this.mediaPlayerController.getRoot();
    }

    /**
     * Gets tool bar root.
     *
     * @return the tool bar root
     */
    public Pane getToolBarRoot() {
        return this.toolBarController.getRoot();
    }

    /**
     * Gets play list root.
     *
     * @return the play list root
     */
    public Pane getMainLibraryRoot() {
        return this.LibraryController.getRoot();
    }

    /**
     * Gets play lists root.
     *
     * @return the play lists root
     */
    public Pane getPlaylistNavigatorRoot() {
        return this.playlistNavigatorController.getRoot();
    }

    /**
     * Gets queue root.
     *
     * @return the queue root
     */
    public Pane getQueueRoot() {
        return this.queueController.getRoot();
    }


    /**
     * Is playlist item selected boolean binding.
     *
     * @return the boolean binding
     */
    public BooleanBinding isPlaylistItemSelected() {
        return LibraryController.isSelected();
    }

    /**
     * Clear play list view selection.
     */
    public void clearPlayListViewSelection() {
        LibraryController.clearSelection();
    }

    /**
     * Gets selected play list song.
     *
     * @return the selected play list song
     */
    public Song getSelectedPlayListSong() {
        return LibraryController.getSelectedSong();
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
     * Get the isPlaying property.
     * True if unpaused, false if paused.
     *
     * @return The is playing property.
     */
    public BooleanProperty isPlayingProperty() {
        return mediaPlayerController.isPlayingProperty();
    }

    public void toggleLyrics(boolean show) {
        this.view.toggleLyrics(show);
    }

    public Pane getLyricsRoot() {
        return this.lyricsController.getRoot();
    }

    public Song getSongByPathInMainLibrary(Path path) {
        return LibraryController.getSongByPath(path);
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
        LibraryController.loadPlaylist(library);
    }

    /**
     * Get the main library
     *
     * @return The main library
     */
    public Library getLibrary() {
        return LibraryController.getLibrary();
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
                FileManager fileManager = new FileManager();
                Path copiedFilePath = fileManager.copyFileToDirectory(selectedFile, mainLibraryPath);
                System.out.println("File copied with succes : " + copiedFilePath);
                LibraryController.addSong(copiedFilePath);
            } catch (IOException e) {
                System.err.println("Error while copying : " + e.getMessage());
            }
        }
    }


}
