package musicApp.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.stage.Stage;
import javafx.util.Duration;
import musicApp.exceptions.BadSongException;
import musicApp.exceptions.EqualizerGainException;
import musicApp.models.Song;
import musicApp.views.DjPlayerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DjPlayerController extends ViewController<DjPlayerView> implements DjPlayerView.DjPlayerViewListener {

    private final MediaPlayerController mediaPlayerController;
    private List<Double> bandsGainBackup;
    private Timeline timeline;
    
    // parameters for the effects
    private double timelineSpeed;
    private double bassBoostGain;
    private double gainValue;
    private double pressureStrength;
    List<Double> gainModeBands = new ArrayList<>(Collections.nCopies(10, 0.0));
    List<Double> bassBoostModeBands = new ArrayList<>(Collections.nCopies(10, 0.0));
    List<Double> pressureModeBands = new ArrayList<>(Collections.nCopies(10, 0.0));

    // Effects ON/OFF
    private boolean gainModeOn = false;
    private boolean bassBoostModeOn = false;
    private boolean pressureModeOn = false;

    // Range of values
    private static final double MAX_DB = 12.0;
    private static final double MIN_DB = -24.0;
    private static final double NUM_BANDS = 10;
    private static final double MAX_STRENGTH = 18.0;
    private static final double MIN_BASS_BOOST = 6.0;


    public DjPlayerController(MediaPlayerController _mediaPlayerController) {
        super(new DjPlayerView());
        view.setListener(this);
        mediaPlayerController = _mediaPlayerController;
        initView("/fxml/DjPlayer.fxml");
        bandsGainBackup = mediaPlayerController.getEqualizerBands();
    }


    /**
     * Toggles the bass boost mode.
     */
    public void toggleBassBoostMode() {

        double low = -3.0;
        bassBoostModeBands = List.of(bassBoostGain, bassBoostGain, bassBoostGain, low, low, low, low, low, low, low);

    }

    /**
     * Toggles the gain mode.
     */
    public void toggleBoostGainMode() {

        gainModeBands = List.of(
            gainValue, gainValue, gainValue, gainValue, gainValue,
            gainValue, gainValue, gainValue, gainValue, gainValue
        );

    }

    /**
     * Toggles the pressure mode.
     */
    public void togglePressureMode() {

        double abs = Math.abs(MIN_DB) + Math.abs(MAX_DB);
        double middleFrequency = MAX_DB - (abs / 2.0);

        double low = middleFrequency - pressureStrength;
        double high = middleFrequency + pressureStrength;

        pressureModeBands = List.of(low, low, low, low, low, high, high, high, high, high);

    }

    /**
     * Toggles the wave gain mode.
     */
    public void toggleWaveGainMode() {

        if (timeline != null && timeline.getStatus() == javafx.animation.Animation.Status.RUNNING) {
            timeline.stop();
            timeline = null;
            return;
        }

        // centers the function (amplitude = 18 db, offset = -6 db)
        double amplitude = (MAX_DB - MIN_DB) / 2.0;
        double offset = MIN_DB + amplitude;

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

            try {
                mediaPlayerController.setEqualizerBands(bandsGain);
            } catch (EqualizerGainException e) {
                // cannot happen
            }
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

    }

    /**
     * Applies the mixed effects to the media player.
     */
    public void applyMixedEffects() throws EqualizerGainException {

        if (timeline != null) { timeline.stop(); }

        List<Double> mixedBands = new ArrayList<>(Collections.nCopies(10, 0.0));

        for (int band = 0; band < NUM_BANDS; band++) {
            double sum = 0.0;
            int count = 0;

            if (gainModeOn) {
                double value = gainModeBands.get(band);
                if (value != 0) {
                    sum += value;
                    count++;
                }
            }
            if (bassBoostModeOn) {
                double value = bassBoostModeBands.get(band);
                if (value != 0) {
                    sum += value;
                    count++;
                }
            }
            if (pressureModeOn) {
                double value = pressureModeBands.get(band);
                if (value != 0) {
                    sum += value;
                    count++;
                }
            }
            if (count != 0) { mixedBands.set(band, sum / count); }
            else { mixedBands.set(band, sum); }
        }

        mediaPlayerController.setEqualizerBands(mixedBands);
    }

    /**
     * Changes the wave speed.
     * @param speed the speed of the wave (0 to 100)
     */
    public void changeWaveSpeed(double speed) throws EqualizerGainException {
        // From 1 to 10 wave speed
        if (speed == 0.0) {
            if (timeline != null) {
                timeline.stop();
                timeline = null;
                applyMixedEffects();
            }
        } else {
            timelineSpeed = speed / (100/3);
            if (timeline == null) {
                toggleWaveGainMode();
            }
        }
    }

    /**
     * Changes the bass boost gain.
     * @param gain the gain of the bass boost (0 to 100)
     */
    public void changeBassBoostGain(double gain) throws EqualizerGainException {
        // From 6 to 12 db
        if (gain > 0.0) {
            bassBoostModeOn = true;
            double newGain = (gain / 100) * (MAX_DB - MIN_BASS_BOOST) + MIN_BASS_BOOST;
            bassBoostGain = newGain;
        } else {
            bassBoostModeOn = false;
        }
        toggleBassBoostMode();
        applyMixedEffects();
    }

    /**
     * Changes the gain mode.
     * @param gain the gain of the gain mode (0 to 100)
     */
    public void changeGainMode (double gain) throws EqualizerGainException {
        // From -24 to 12 db
        if (gain > 0.0) {
            gainModeOn = true;
            double newGain = (gain / 100) * (MAX_DB - MIN_DB) + MIN_DB;
            gainValue = newGain;
        } else {
            gainModeOn = false;
        }
        toggleBoostGainMode();
        applyMixedEffects();
    }

    /**
     * Changes the pressure strength.
     * @param strength the strength of the pressure (0 to 100)
     */
    public void changePressureStrength(double strength) throws EqualizerGainException {
        // strength must be bewteen 0 and abs(MAX_LOW) + abs(MAX_HIGH) / 2
        // From 0 to 18
        if (strength > 0.0) {
            pressureModeOn = true;
            double newStrength = (strength / 100) * MAX_STRENGTH;
            pressureStrength = newStrength;
        } else {
            pressureModeOn = false;
        }
        togglePressureMode();
        applyMixedEffects();
    }

    /**
     * Plays the song.
     * @param song the song to play
     */
    public void play(Song song) throws BadSongException {
        mediaPlayerController.playCurrent(song);
        view.show();
    }

    /**
     * On quit called method. Destroy all timelines, restore the old config and close the stage.
     */
    public void handleClose() {
        if (timeline != null) { timeline.stop(); }
        try {
            mediaPlayerController.setEqualizerBands(bandsGainBackup);
        } catch (EqualizerGainException e) {
            e.printStackTrace();
        }
        view.close();
    }

}
