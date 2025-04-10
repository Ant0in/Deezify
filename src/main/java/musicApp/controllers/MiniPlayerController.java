package musicApp.controllers;

import javafx.scene.media.AudioSpectrumListener;
import javafx.stage.Stage;
import musicApp.models.Song;
import musicApp.views.MiniPlayerView;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Controller managing the MiniPlayerView and processing audio spectrum data received on `spectrumDataUpdate`
 */
public class MiniPlayerController extends ViewController< MiniPlayerView , MiniPlayerController> implements AudioSpectrumListener {
    private final Stage stage = new Stage();
    private final int bandsNumber = 128;

    /**
     * Instantiates a new View controller.
     */
    public MiniPlayerController( ) {
        super(new MiniPlayerView());

        initView("/fxml/MiniPlayer.fxml");

        stage.setScene(view.getScene());
        stage.setTitle("MiniPlayer");
    }

    /**
     * Toggle view.
     */
    public void toggleView() {
        if (stage.isShowing()) {
            stage.close();
        } else {
            stage.show();
        }
    }

    /**
     * Gets bands number.
     *
     * @return the bands number
     */
    public int getBandsNumber() {
        return bandsNumber;
    }

    /**
     * Load song.
     *
     * @param song the song
     */
    public void loadSong(Song song) {
        view.updateSongProperties(song.getTitle(), song.getCoverImage());
    }

    /**
     * Callback method receiving audio spectrum data from the currently playing song
     * This method is called by the JavaFX media player
     *
     * The spectrum data is received as an array of magnitudes and phases, which are
     * processed to create a visual representation of the audio spectrum.
     *
     * @param timestamp The timestamp of the audio data
     * @param duration The duration of the audio data
     * @param magnitudes The array of magnitudes representing the audio spectrum
     * @param phases The array of phases representing the audio spectrum
     */
    @Override
    public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
        // AudioSpectrumThreshold is usually -60 dB by default in JavaFX
        final float minDb = -60f;
        final float maxDb = 0f;
        final float rangeDb = maxDb - minDb; // should be 60f

        // Apply normalization and correction in a single stream pipeline
        List<Float> correctedMagnitudes = IntStream.range(0, magnitudes.length)
                .mapToObj(i -> {
                    float clamped = Math.max(minDb, Math.min(maxDb, magnitudes[i]));
                    float normalized = (clamped - minDb) / rangeDb;

                    // Apply correction function here
                    return applyCorrection(normalized, i) ;
                })
                .collect(Collectors.toList());

        // Visualize
        view.draw(correctedMagnitudes);
    }

    /*
     * Method use to process the magnitude data and flatten it for better visuals.
     */
    private Float applyCorrection( float f, int i) {
        float f1 = f * ( (float) Math.log10( 1.1 + i ));
        return (float) (f1 * 1.5 * Math.exp(-Math.abs((i - bandsNumber/2)/(bandsNumber/4))));
    }
}
