package musicApp.controllers;

import javafx.stage.Stage;
import musicApp.models.Equalizer;
import musicApp.views.EqualizerView;

import java.util.List;

public class EqualizerController extends ViewController<EqualizerView, EqualizerController> {
    private final SettingsController settingsController;
    private final Stage stage;
    private final Equalizer equalizer;

    public EqualizerController(SettingsController settingsController, Equalizer equalizer) {
        super(new EqualizerView());
        this.settingsController = settingsController;
        this.stage = new Stage();
        this.equalizer = equalizer;
        initView("/fxml/Equalizer.fxml");
    }

    /**
     * Show the equalizer.
     */
    public void show() {
        this.view.show(stage);
    }

    /**
     * Close the equalizer.
     */
    public void close() {
        stage.close();
        this.settingsController.show();
    }

    /**
     * Update a band in the equalizer.
     *
     * @param bandIndex The index of the band to update.
     * @param value     The new value of the band.
     */
    public void updateEqualizerBand(int bandIndex, double value) {
        this.equalizer.setBandGain(bandIndex, value);
    }

    /**
     * Get the band frequency.
     *
     * @param bandIndex The index of the band.
     */
    public int getBandFrequency(int bandIndex) {
        return this.equalizer.getBandFrequency(bandIndex);
    }

    /**
     * Get the band gain.
     *
     * @param bandIndex The index of the band.
     */
    public double getEqualizerBandGain(int bandIndex) {
        return this.equalizer.getBandGain(bandIndex);
    }

    /**
     * Get the maximum gain in dB.
     */
    public double getMaxGainDB() {
        return this.equalizer.getMaxGainDB();
    }

    /**
     * Get the minimum gain in dB.
     */
    public double getMinGainDB() {
        return this.equalizer.getMinGainDB();
    }

    /**
     * Update the equalizer.
     */
    public void update() {
        List<Double> equalizerBands = this.view.getSlidersValues();
        for (int bandIndex = 0; bandIndex < equalizerBands.size(); bandIndex++) {
            updateEqualizerBand(bandIndex, equalizerBands.get(bandIndex));
        }
        this.view.updateSlidersValues();
    }

    /**
     * Handle the cancel button.
     */
    public void handleCancel() {
        this.view.updateSlidersValues();
    }

}