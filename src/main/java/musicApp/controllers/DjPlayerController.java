package musicApp.controllers;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import javafx.stage.Stage;
import musicApp.models.Song;
import musicApp.views.DjPlayerView;


public class DjPlayerController extends ViewController<DjPlayerView, DjPlayerController> {
    
    private final Stage stage = new Stage();
    private final MediaPlayerController mediaPlayerController;
    private List<Double> bandsGainBackup;
    private Timeline timeline;
    
    // parameters for the effects
    private double timelineSpeed = 2.0;
    private double bassBoostGain = 6.0;
    private double pressureStrength = 0.5;

    private static final double MAX_HIGH = 12.0;
    private static final double MAX_LOW = -24.0;
    private static final double NUM_BANDS = 10;


    public DjPlayerController(PlayerController player) {
 
        super(new DjPlayerView());
        this.mediaPlayerController = player.getMediaPlayerController();
        this.bandsGainBackup = mediaPlayerController.getEqualizerBands();

    }

    public void init() {

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
        
        double low = -3.0;
        List<Double> bandsGain = List.of(bassBoostGain, bassBoostGain, bassBoostGain, low, low, low, low, low, low, low);
        mediaPlayerController.setEqualizerBands(bandsGain);

    }

    public void toggleBoostGainMode() {

        List<Double> bandsGain = List.of(
            bassBoostGain, bassBoostGain, bassBoostGain, bassBoostGain, bassBoostGain,
            bassBoostGain, bassBoostGain, bassBoostGain, bassBoostGain, bassBoostGain
        );
        mediaPlayerController.setEqualizerBands(bandsGain);

    }

    public void togglePressureMode() {

        double abs = Math.abs(MAX_LOW) + Math.abs(MAX_HIGH);
        double middleFrequency = MAX_HIGH - (abs / 2.0);

        double low = middleFrequency - pressureStrength;
        double high = middleFrequency + pressureStrength;

        List<Double> bandsGain = List.of(low, low, low, low, low, high, high, high, high, high);
        System.out.println("bandsGain = " + bandsGain.toString());
        mediaPlayerController.setEqualizerBands(bandsGain);

    }

    public void toggleWaveGainMode() {
        
        // centers the function (amplitude = 18 db, offset = -6 db)
        
        double amplitude = MAX_HIGH - MAX_LOW / 2.0;
        double offset = MAX_LOW + amplitude;

        // Timeline for sequential update on the bands gain
        timeline = new Timeline(new KeyFrame(Duration.millis(50), _ -> {
            double currentTime = System.currentTimeMillis() / 1000.0;
            List<Double> bandsGain = new ArrayList<>();

            // Gain of each band, dephased depending on the band
            for (int i = 0; i < NUM_BANDS; i++) {
                double phase = currentTime * timelineSpeed + (i * Math.PI / NUM_BANDS);
                double gain = offset + amplitude * Math.sin(phase);
                bandsGain.add(gain);
            }

            mediaPlayerController.setEqualizerBands(bandsGain);

        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

    }

    public void changeWaveSpeed(double speed) {
        timelineSpeed = speed;
    }

    public void changeBassBoostGain(double gain) {
        bassBoostGain = gain;
    }

    public void changePressureStrength(double strength) {
        // strength must be bewteen 0 and abs(MAX_LOW) + abs(MAX_HIGH) / 2
        if (strength < 0 || strength > (Math.abs(MAX_LOW) + Math.abs(MAX_HIGH)) / 2.0) {
            throw new IllegalArgumentException("Strength must be between 0 and " + (Math.abs(MAX_LOW) + Math.abs(MAX_HIGH)) / 2.0);
        }
        pressureStrength = strength;
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
