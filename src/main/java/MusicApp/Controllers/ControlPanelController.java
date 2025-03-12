package MusicApp.Controllers;

import MusicApp.Models.Song;
import MusicApp.Views.ControlPanelView;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.IOException;

public class ControlPanelController {
    private final PlayerController playerController;
    private ControlPanelView controlPanelView;
    

    public ControlPanelController(PlayerController playerController){
        this.playerController = playerController;
        initControlPanelView();
    }

    private void initControlPanelView(){
        this.controlPanelView = new ControlPanelView();
        this.controlPanelView.setControlPanelController(this);
        try {
            this.controlPanelView.initializeScene("/fxml/control_panel.fxml");
            this.controlPanelView.initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Pane getRoot() {
        return controlPanelView.getRoot();
    }

    public BooleanProperty isPlaying(){
        return this.playerController.isPlaying();
    }

    public DoubleProperty progressProperty() {
        return this.playerController.progressProperty();
    }

    public Duration getCurrentTime(){
        return this.playerController.getCurrentTime();
    }

    public Duration getTotalDuration(){
        return this.playerController.getTotalDuration();
    }

    public void seek(double duration){
        this.playerController.seek(duration);
    }

    public double getSpeedValue(String speedLabel) {
        return this.playerController.getSpeedValue(speedLabel);
    }

    public void changeSpeed(double speed) {
        this.playerController.changeSpeed(speed);
    }

    public DoubleProperty volumeProperty() {
        return this.playerController.volumeProperty();
    }

    public Song getCurrentSong() {
        return this.playerController.getCurrentSong();
    }

    public StringProperty currentSongProperty() {
        return this.playerController.currentSongProperty();
    }

    public void toggleShuffle(boolean isEnabled) {
        this.playerController.toggleShuffle(isEnabled);
    }




    /**
     * Handle the pause song button.
     */
    public void handlePauseSong() {
        this.playerController.handlePauseSong();
    }

    /**
     * Handle the next song button.
     */
    public void handleNextSong() {
       this.playerController.handleNextSong();
    }

    /**
     * Handle the previous song button.
     */
    public void handlePreviousSong() {
        playerController.handlePreviousSong();
    }


}
