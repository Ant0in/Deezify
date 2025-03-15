package musicApp.controllers;

import java.io.IOException;

import javafx.beans.property.StringProperty;
import musicApp.models.*;
import musicApp.views.PlayerView;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Controller class for the music player.
 * <p>
 * This class is responsible for managing the audio player and the song library.
 * It provides methods to play, pause, skip, and go back to the previous song.
 * It also allows to add songs to a queue and play them in the order they were added.
 */
public class PlayerController extends ViewController<PlayerView,PlayerController> {

    private final MetaController metaController;
    private MediaPlayerController mediaPlayerController;
    private ToolBarController toolBarController;
    private MainLibraryController mainLibraryController;
    private QueueController queueController;

    /**
     * Constructor
     *
     * @param metaController the meta controller
     * @throws IOException the io exception
     */
    public PlayerController(MetaController metaController) throws IOException {
        super(new PlayerView());
        this.metaController = metaController;
        initSubControllers();
        initView("/fxml/MainLayout.fxml");
        Settings settings = metaController.getSettings();
        this.mediaPlayerController.setBalance(settings.getBalance());
        this.mainLibraryController.loadLibrary(settings.getMusicDirectory());
    }

    private void initSubControllers() {
        this.mainLibraryController = new MainLibraryController(this);
        this.queueController = new QueueController(this);
        this.mediaPlayerController = new MediaPlayerController(this);
        this.toolBarController = new ToolBarController(this);

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
     * Skip to the next song in the library or the queue.
     * If the queue is not empty, the next song in the queue is played.
     * Otherwise, the next song in the library is played.
     */
    public void skip() {
        if (this.queueController.queueIsEmpty()) {
            this.mainLibraryController.skip();
        } else {
            this.queueController.playSong(0);
        }
    }

    /**
     * Handle the previous song
     */
    public void handlePreviousSong() {
        this.mainLibraryController.prec();
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
        metaController.showSettings();
    }

    /**
     * Refresh the UI.
     */
    public void refreshUI() {
        view.refreshUI();
        this.queueController.refreshUI();
    }

    /**
     * Toggle the shuffle mode.
     */
    public void toggleShuffle() {
        this.mainLibraryController.toggleShuffle();
    }

    /**
     * Actions to do when the settings are changed
     *
     * @param newSettings The new settings.
     */
    public void onSettingsChanged(Settings newSettings) {
        this.mediaPlayerController.setBalance(newSettings.getBalance());
        this.mainLibraryController.loadLibrary(newSettings.getMusicDirectory());
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
    public Pane getPlayListRoot() {
        return this.mainLibraryController.getRoot();
    }

    /**
     * Gets queue root.
     *
     * @return the queue root
     */
    public Pane getQueueRoot() { return this.queueController.getRoot();}


    /**
     * Is playlist item selected boolean binding.
     *
     * @return the boolean binding
     */
    public BooleanBinding isPlaylistItemSelected() {
        return mainLibraryController.isSelected();
    }

    /**
     * Clear play list view selection.
     */
    public void clearPlayListViewSelection() {
        mainLibraryController.clearSelection();
    }

    /**
     * Gets selected play list song.
     *
     * @return the selected play list song
     */
    public Song getSelectedPlayListSong() {
        return mainLibraryController.getSelectedSong();
    }

    /**
     * Clear queue selection.
     */
    public void clearQueueSelection() {
        queueController.clearSelection();
    }

    public Song getCurrentlyPlayingSong() {
        return mediaPlayerController.getCurrentSong();
    }

    public StringProperty getCurrentlyPlayingSongStringProperty() {
        return mediaPlayerController.currentSongProperty();
    }
}
