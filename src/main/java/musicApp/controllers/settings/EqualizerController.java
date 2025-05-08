package musicApp.controllers.settings;

import musicApp.controllers.EditUserProfileController;
import musicApp.controllers.ViewController;
import musicApp.enums.EqualizerBandFrequency;
import musicApp.models.Equalizer;
import musicApp.views.settings.EqualizerView;

import java.util.List;


/**
 * The type Equalizer controller.
 */
public class EqualizerController extends ViewController<EqualizerView> implements EqualizerView.EqualizerViewListener {
    
    private final EditUserProfileController editUserProfileController;
    private final Equalizer equalizer;

    /**
     * Instantiates a new Equalizer controller.
     *
     * @param _controller the controller
     * @param _equalizer  the equalizer
     */
    public EqualizerController(EditUserProfileController _controller, Equalizer _equalizer) {
        super(new EqualizerView());
        view.setListener(this);
        editUserProfileController = _controller;
        equalizer = _equalizer;
        initView("/fxml/Equalizer.fxml");
    }

    /**
     * Show the equalizer.
     */
    public void show() {
        view.show();
    }

    /**
     * Close the equalizer.
     */
    public void handleClose() {
        view.close();
        editUserProfileController.show();
    }

    /**
     * Update a band in the equalizer.
     *
     * @param bandIndex The index of the band to update.
     * @param value     The new value of the band.
     */
    public void updateEqualizerBand(int bandIndex, double value) {
        equalizer.setBandGain(bandIndex, value);
    }

    /**
     * Get the band gain.
     *
     * @param frequency The frequency of the band.
     * @return the equalizer band gain
     */
    public double getEqualizerBandGain(EqualizerBandFrequency frequency) {
        return equalizer.getBandGain(EqualizerBandFrequency.getIndex(frequency));
    }

    /**
     * Get the band gain.
     *
     * @param frequency The frequency of the band.
     * @return the equalizer band gain
     */
    public List<Double> getEqualizerBandsGain() {
        return equalizer.getBandsGain();
    }

    /**
     * Get the maximum gain in dB.
     *
     * @return the max gain db
     */
    public double getMaxGainDB() {
        return equalizer.getMaxGainDB();
    }

    /**
     * Get the minimum gain in dB.
     *
     * @return the min gain db
     */
    public double getMinGainDB() {
        return equalizer.getMinGainDB();
    }

    /**
     * Update the equalizer.
     */
    public void update() {
        List<Double> equalizerBands = view.getSlidersValues();
        for (int bandIndex = 0; bandIndex < equalizerBands.size(); bandIndex++) {
            updateEqualizerBand(bandIndex, equalizerBands.get(bandIndex));
        }
        view.updateSlidersValues();
    }

    /**
     * Handle the cancel button.
     */
    public void handleCancel() {
        view.updateSlidersValues();
    }

}