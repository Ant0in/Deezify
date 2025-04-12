package musicApp.controllers;

import java.util.ArrayList;
import java.util.List;

import javafx.stage.Stage;
import musicApp.controllers.settings.EqualizerController;
import musicApp.models.Song;
import musicApp.views.DjPlayerView;


public class DjPlayerController extends ViewController<DjPlayerView, DjPlayerController> {
    
    private final Stage stage = new Stage();
    private final PlayerController player;
    private final MediaPlayerController mediaPlayerController;

    public DjPlayerController(PlayerController player) {
 
        super(new DjPlayerView());
        this.player = player;
        this.mediaPlayerController = player.getMediaPlayerController();

    }

    public void init_view() {

        initView("/fxml/DjPlayer.fxml");
        stage.setScene(view.getScene());
        stage.setTitle("DJ Player");
        stage.setResizable(false);
        stage.show();

    }

    private void resetBands(ArrayList<Integer> bands) {

        List<Double> bandsGain = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            // if i is in the bands aray list, set the band to 0.0
            if (bands.contains(i)) {
                bandsGain.set(i, 0.0);
            } else {
                // set the band to -24.0
                bandsGain.set(i, equalizerController.getEqualizerBandGain(i));
            }
        }

    }

    public void toggleBassBoostedMode() {
        
        double high = 12.0;
        double low = -3.0;
        List<Double> bandsGain = List.of(0.0, high, high, low, low, low, low, low, low, low);
        mediaPlayerController.setEqualizerBands(bandsGain);

    }

    public void toggleEarrapeMode() {

        double high = 12.0;
        List<Double> bandsGain = List.of(high, high, high, high, high, high, high, high, high, high);
        mediaPlayerController.setEqualizerBands(bandsGain);
    }

    public void play(Song song) {
        player.playSong(song);
    }

    public void pause() {
        player.pause();
    }

    public void unpause() {
        player.unpause();
    }

    public void start() {
        // ...
    }

}
