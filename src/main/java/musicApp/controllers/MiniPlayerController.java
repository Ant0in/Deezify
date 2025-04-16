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
public class MiniPlayerController extends ViewController<MiniPlayerView> implements MiniPlayerView.MiniPlayerViewListener, AudioSpectrumListener {

    private static final int DEFAULT_BANDS_NUMBER = 128;
    private static final float MIN_DECIBEL_LEVEL = -60f;
    private static final float MAX_DECIBEL_LEVEL = 0f;
    private static final float LOG_SCALE_OFFSET = 1.1f;
    private static final float VISUAL_INTENSITY_MULTIPLIER = 1.5f;

    private final Stage stage;

    /**
     * Instantiates the MiniPlayerController and initializes the MiniPlayer view.
     */
    public MiniPlayerController() {
        super(new MiniPlayerView());
        view.setListener(this);
        initView("/fxml/MiniPlayer.fxml");
        stage = new Stage();
        stage.setScene(view.getScene());
        stage.setTitle("MiniPlayer");
    }

    /**
     * Toggle the visibility of the mini player window.
     */
    public void toggleView() {
        if (stage.isShowing()) {
            stage.close();
        } else {
            stage.show();
        }
    }

    /**
     * Returns the number of audio spectrum bands used for visualization.
     *
     * @return the number of bands
     */
    public int getBandsNumber() {
        return DEFAULT_BANDS_NUMBER;
    }

    /**
     * Loads the given song into the view by updating its title and cover image.
     *
     * @param song the song to load
     */
    public void loadSong(Song song) {
        view.updateSongProperties(song.getTitle(), song.getCoverImage());
    }

    /**
     * Callback method that receives real-time audio spectrum data from the JavaFX media player.
     * This method processes the magnitude values to a visually normalized and corrected form
     * and then updates the view for visualization.
     *
     * @param timestamp  the timestamp of the audio data
     * @param duration   the duration of the audio data
     * @param magnitudes the array of magnitude values
     * @param phases     the array of phase values (not used in this implementation)
     */
    @Override
    public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
        float rangeDb = MAX_DECIBEL_LEVEL - MIN_DECIBEL_LEVEL;

        List<Float> correctedMagnitudes = IntStream.range(0, magnitudes.length)
                .mapToObj(i -> {
                    float clamped = Math.max(MIN_DECIBEL_LEVEL, Math.min(MAX_DECIBEL_LEVEL, magnitudes[i]));
                    float normalized = (clamped - MIN_DECIBEL_LEVEL) / rangeDb;
                    return applyCorrection(normalized, i);
                })
                .collect(Collectors.toList());

        view.draw(correctedMagnitudes);
    }

    /**
     * Applies a visual correction to a normalized magnitude value.
     * This correction enhances the representation by scaling and centering the output visually.
     *
     * @param normalizedValue the normalized magnitude (0.0 to 1.0)
     * @param index           the index of the frequency band
     * @return the corrected value for visual representation
     */
    private float applyCorrection(float normalizedValue, int index) {
        float scaled = normalizedValue * ((float) Math.log10(LOG_SCALE_OFFSET + index));
        float center = DEFAULT_BANDS_NUMBER / 2f;
        float spread = DEFAULT_BANDS_NUMBER / 4f;

        return (float) (scaled * VISUAL_INTENSITY_MULTIPLIER * Math.exp(-Math.abs((index - center) / spread)));
    }
}