package musicApp.controllers;

import java.util.ArrayList;
import java.util.List;

import be.tarsos.dsp.io.jvm.AudioPlayer;
import javafx.stage.Stage;
import musicApp.controllers.settings.EqualizerController;
import musicApp.models.Song;
import musicApp.views.DjPlayerView;


public class DjPlayerController extends ViewController<DjPlayerView, DjPlayerController> {
    
    //private final Minim minim;
    //private final AudioPlayer audioPlayer;
    
    private final Song song;
    private final Stage stage = new Stage();
    private final PlayerController player;
    private final EqualizerController equalizerController;
    private final MediaPlayerController mediaPlayerController;

    public DjPlayerController(Song song, PlayerController player) {
 
        super(new DjPlayerView());
        this.song = song;
        this.player = player;
        this.equalizerController = player.getEqualizerController();
        this.mediaPlayerController = player.getMediaPlayerController();

        play();
        toggleBassBoostedMode();

        initView("/fxml/DjPlayer.fxml");
        stage.setScene(view.getScene());
        stage.setTitle("DJ Player");
        stage.setResizable(false);
        stage.show();

    }

    private void changeFrequency(int bandIndex, double value) {
        // ...
    }

    private void resetBands(ArrayList<Integer> bands) {
        // ...
    }

    public void toggleBassBoostedMode() {
        
        double minimumGain = -24.0;
        double maximumGain = 12.0;
        List<Double> bandsGain = List.of(
            maximumGain, maximumGain, minimumGain, minimumGain, 
            minimumGain, maximumGain, maximumGain, maximumGain, 
            maximumGain, maximumGain
        );

        mediaPlayerController.setEqualizerBands(bandsGain);
    }

    public void play() {
        player.playSong(song);
    }

    public void start() {
        // ...
    }

}
