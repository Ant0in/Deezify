package musicApp.models;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Equalizer {
    @Expose
    private static final List<Double> DEFAULT_BANDS = Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
    private List<Double> equalizerBands = new ArrayList<>(DEFAULT_BANDS);


    private final ArrayList<Integer> bandFrequencies = new ArrayList<>(Arrays.asList(32, 64, 125, 250, 500, 1000, 2000, 4000, 8000, 16000));

    public final int EQ_BAND_COUNT = bandFrequencies.size();
    public final double MAX_GAIN_DB = 12;
    public final double MIN_GAIN_DB = -24;

    public Equalizer() { }

    public Equalizer(List<Double> equalizerBands) {
        checkEqualizerBands(equalizerBands);
        this.equalizerBands = equalizerBands;
    }

    private void checkEqualizerBand(int bandIndex, double gain) {
        checkBandIndex(bandIndex);
        if (gain < MIN_GAIN_DB || gain > MAX_GAIN_DB) {
            throw new IllegalArgumentException("Equalizer band value for band " + bandIndex + " (" + gain + ") is out of range. Must be between " + MIN_GAIN_DB + " and " + MAX_GAIN_DB + ".");
        }
    }

    private void checkBandIndex(int bandIndex) {
        if (bandIndex < 0 || bandIndex >= EQ_BAND_COUNT) {
            throw new IllegalArgumentException("Invalid band index: " + bandIndex);
        }
    }

    private void checkEqualizerBands(List<Double> equalizerBands) {
        if (equalizerBands == null || equalizerBands.size() != EQ_BAND_COUNT) {
            throw new IllegalArgumentException("Equalizer bands must have exactly " + EQ_BAND_COUNT + " values.");
        }
        for (int i = 0; i < equalizerBands.size(); i++) {
            checkEqualizerBand(i, equalizerBands.get(i));
        }
    }

    public double getEqualizerBand(int bandIndex) {
        return equalizerBands.get(bandIndex);
    }

    public void setEqualizerBand(int bandIndex, double gain) {
        checkEqualizerBand(bandIndex, gain);
        equalizerBands.set(bandIndex, gain);
    }

    public List<Double> getEqualizerBands() {
        return equalizerBands;
    }

    public double getMaxGainDB() {
        return MAX_GAIN_DB;
    }

    public double getMinGainDB() {
        return MIN_GAIN_DB;
    }

    public int getBandFrequency(int bandIndex){
        checkBandIndex(bandIndex);
        return bandFrequencies.get(bandIndex);
    }

    @Override
    public String toString() {
        return String.join(",",
                equalizerBands.stream()
                        .map(String::valueOf)
                        .toArray(String[]::new)
        );
    }
}
