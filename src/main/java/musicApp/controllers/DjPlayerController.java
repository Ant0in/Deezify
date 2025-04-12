package musicApp.controllers;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import javafx.stage.Stage;
import musicApp.models.Song;
import musicApp.views.DjPlayerView;
import musicApp.controllers.PlayerController;


public class DjPlayerController extends ViewController<DjPlayerView, DjPlayerController> {
    
    private final Stage stage = new Stage();
    private final MediaPlayerController mediaPlayerController;
    private List<Double> bandsGainBackup;
    private Timeline timeline;

    private static final double MAX_HIGH = 12.0;
    private static final double MAX_LOW = -24.0;
    private static final double NUM_BANDS = 10;


    public DjPlayerController(PlayerController player) {
 
        super(new DjPlayerView());
        this.mediaPlayerController = player.getMediaPlayerController();
        this.bandsGainBackup = mediaPlayerController.getEqualizerBands();

    }

    public void init_dj_player() {

        // initialize the view and then make a copy of the current equalizer bands
        init_view();  
        bandsGainBackup = mediaPlayerController.getEqualizerBands();

    }

    public void init_view() {

        initView("/fxml/DjPlayer.fxml");
        stage.setScene(view.getScene());
        stage.setOnCloseRequest(_ -> onQuit());
        stage.setTitle("DJ Player");
        stage.setResizable(false);
        stage.show();

    }

    public void toggleBassBoostMode() {
        
        double high = 12.0;
        double low = -3.0;
        List<Double> bandsGain = List.of(0.0, high, high, low, low, low, low, low, low, low);
        mediaPlayerController.setEqualizerBands(bandsGain);

    }

    public void toggleBoostGainMode() {

        double high = 12.0;
        List<Double> bandsGain = List.of(high, high, high, high, high, high, high, high, high, high);
        mediaPlayerController.setEqualizerBands(bandsGain);
    }

    public void togglePressureMode() {

        List<Double> bandsGain = List.of(MAX_LOW, MAX_LOW, MAX_LOW, MAX_LOW, MAX_LOW,
                MAX_HIGH, MAX_HIGH, MAX_HIGH, MAX_HIGH, MAX_HIGH);
        mediaPlayerController.setEqualizerBands(bandsGain);
    }

    public void toggleWaveGainMode() {
        
        // centers the function (amplitude = 18 db, offset = -6 db)
        
        double amplitude = MAX_HIGH - MAX_LOW / 2.0;
        double offset = MAX_LOW + amplitude;
        double speed = 2.0;

        // Timeline for sequential update on the bands gain
        timeline = new Timeline(new KeyFrame(Duration.millis(50), event -> {
            double currentTime = System.currentTimeMillis() / 1000.0;
            List<Double> bandsGain = new ArrayList<>();

            // Gain of each band, dephased depending on the band
            for (int i = 0; i < NUM_BANDS; i++) {
                double phase = currentTime * speed + (i * Math.PI / NUM_BANDS);
                double gain = offset + amplitude * Math.sin(phase);
                bandsGain.add(gain);
            }

            mediaPlayerController.setEqualizerBands(bandsGain);

        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

    }

    public void play(Song song) {
        mediaPlayerController.playCurrent(song);
    }

    public void pause() {
        mediaPlayerController.pause();
    }

    public void unpause() {
        mediaPlayerController.unpause();
    }

    public void start() {
        // ...
    }

    public void onQuit() {
        mediaPlayerController.setEqualizerBands(bandsGainBackup);
        if (timeline != null) { timeline.stop(); }
        stage.close();
    }

}
