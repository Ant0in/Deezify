package musicApp.models;

import com.google.gson.annotations.Expose;
import javafx.scene.media.EqualizerBand;
import musicApp.enums.EqualizerBandFrequency;

import java.util.ArrayList;
import java.util.List;

public class Equalizer {
    public static final double MAX_GAIN_DB = EqualizerBand.MAX_GAIN;
    public static final double MIN_GAIN_DB = EqualizerBand.MIN_GAIN;
    public static final int DEFAULT_BANDS_SIZE = 10;
    @Expose
    private List<Double> bandsGain;

    /**
     * Default constructor for Equalizer.
     */
    public Equalizer() {
        bandsGain = new ArrayList<>();
        List<Double> defaultBandsGain = new ArrayList<>(java.util.Collections.nCopies(DEFAULT_BANDS_SIZE, 0.0));
        setBandsGain(defaultBandsGain);
    }

    /**
     * Constructor for Equalizer with specified bands gain.
     * @param bandsGain bandsGains
     */
    public Equalizer(List<Double> bandsGain) {
        checkBandsGain(bandsGain);
        setBandsGain(bandsGain);
    }

    /**
     * Get the number of bands in the equalizer.
     * @return the number of bands
     */
    private int getBandsSize() {
        return EqualizerBandFrequency.getBandsSize();
    }

    /**
     * Check if the band index and gain are valid.
     * @param bandIndex Index of the band to check
     * @param gain Gain value to check
     */
    private void checkBand(int bandIndex, double gain) throws IllegalArgumentException {
        checkBandIndex(bandIndex);
        if (gain < MIN_GAIN_DB || gain > MAX_GAIN_DB) {
            throw new IllegalArgumentException(
                    "Equalizer band value for band " + bandIndex +
                            " (" + gain + ") is out of range. Must be between " + MIN_GAIN_DB + " and " + MAX_GAIN_DB + ".");
        }
    }

    /**
     * Check if the band index is valid.
     * @param bandIndex Index of the band to check
     */
    private void checkBandIndex(int bandIndex) throws IllegalArgumentException {
        if (bandIndex < 0 || bandIndex >= getBandsSize()) {
            throw new IllegalArgumentException("Invalid band index: " + bandIndex);
        }
    }

    /**
     * Check if the bands gain list is valid.
     * @param bandsGain List of bands gain to check
     */
    private void checkBandsGain(List<Double> bandsGain) throws IllegalArgumentException {
        if (bandsGain == null || bandsGain.size() != getBandsSize()) {
            throw new IllegalArgumentException("Equalizer bands must have exactly " + getBandsSize() + " values.");
        }
        for (int i = 0; i < bandsGain.size(); i++) {
            checkBand(i, bandsGain.get(i));
        }
    }
    
    /**
     * Get the gain of a specific band.
     * @param bandIndex Index of the band to get the gain for
     * @return The gain of the band at the specified index
     */
    public double getBandGain(int bandIndex) {
        return bandsGain.get(bandIndex);
    }

    /**
     * Set the gain of a specific band.
     * @param bandIndex Index of the band to set the gain for
     * @param gain The gain value to set for the band at the specified index
     */
    public void setBandGain(int bandIndex, double gain) {
        checkBand(bandIndex, gain);
        bandsGain.set(bandIndex, gain);
    }

    /**
     * Get the list of bands gain.
     * @return the list of bands gain
     */
    public List<Double> getBandsGain() {
        return bandsGain;
    }

    /**
     * Set the list of bands gain.
     * @param newBandsGain the new list of bands gain
     */
    public void setBandsGain(List<Double> newBandsGain) {
        checkBandsGain(newBandsGain);
        bandsGain = newBandsGain;
    }

    /**
     * Get the list of bands frequency.
     * @return the list of bands frequency
     */
    @Override
    public String toString() {
        return bandsGain.stream()
                .map(String::valueOf)
                .collect(java.util.stream.Collectors.joining(","));
    }
}