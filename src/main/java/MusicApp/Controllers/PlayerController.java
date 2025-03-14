package MusicApp.Controllers;

import java.io.IOException;
import MusicApp.Models.*;
import MusicApp.Views.PlayerView;
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
    private ControlPanelController controlPanelController;
    private ToolBarController toolBarController;
    private PlayListController playListController;
    private QueueController queueController;

    /**
     * Constructor
     */
    public PlayerController(MetaController metaController) throws IOException {
        super(new PlayerView());
        this.metaController = metaController;
        initSubControllers();
        initView("/fxml/MainLayout.fxml");
        Settings settings = metaController.getSettings();
        this.controlPanelController.setBalance(settings.getBalance());
        this.playListController.loadLibrary(settings.getMusicDirectory());
    }

    private void initSubControllers() {
        this.playListController = new PlayListController(this);
        this.queueController = new QueueController(this);
        this.controlPanelController = new ControlPanelController(this);
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

    public void playSong(Song song) {
        this.controlPanelController.playCurrent(song);
    }

    /**
     * Skip to the next song in the library or the queue.
     * If the queue is not empty, the next song in the queue is played.
     * Otherwise, the next song in the library is played.
     */
    public void skip() {
        if (this.queueController.queueIsEmpty()) {
            this.playListController.skip();
        } else {
            this.queueController.playSong(0);
        }
    }

    /**
     * Handle the previous song
     */
    public void handlePreviousSong() {
        this.playListController.prec();
    }


    /**
     * Close the audio player.
     */
    public void close() {
        this.controlPanelController.close();
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
     *
     * @param isEnabled The shuffle button state.
     */
    public void toggleShuffle(boolean isEnabled) {
        this.playListController.toggleShuffle(isEnabled);
    }

    /**
     * Actions to do when the settings are changed
     *
     * @param newSettings The new settings.
     */
    public void onSettingsChanged(Settings newSettings) {
        this.controlPanelController.setBalance(newSettings.getBalance());
        this.playListController.loadLibrary(newSettings.getMusicDirectory());
    }

    public Pane getControlPanelRoot() {
        return this.controlPanelController.getRoot();
    }

    public Pane getToolBarRoot() {
        return this.toolBarController.getRoot();
    }

    public Pane getPlayListRoot() {
        return this.playListController.getRoot();
    }

    public Pane getQueueRoot() { return this.queueController.getRoot();}


    public BooleanBinding isPlaylistItemSelected() {
        return playListController.isSelected();
    }

    public void clearPlayListViewSelection() {
        playListController.clearSelection();
    }

    public Song getSelectedPlayListSong() {
        return playListController.getSelectedSong();
    }

    public void clearQueueSelection() {
        this.queueController.clearSelection();
    }

}
