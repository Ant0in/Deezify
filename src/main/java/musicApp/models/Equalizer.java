package musicApp.models;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Equalizer {
    @Expose
    private List<Double> savedEqualizerBands = new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0));;
    private final List<Double> currentEqualizerBands = new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0));


    public final int EQ_BAND_COUNT = 10;
    public final double MAX_GAIN_DB = 12;
    public final double MIN_GAIN_DB = -24;

    public Equalizer() { }

    public Equalizer(List<Double> equalizerBands) {
        checkEqualizerBands(equalizerBands);
        this.savedEqualizerBands = equalizerBands;
    }

    private void checkEqualizerBand(int bandIndex, double gain) {
        if (gain < MIN_GAIN_DB || gain > MAX_GAIN_DB) {
            throw new IllegalArgumentException("Equalizer band value for band " + bandIndex + " (" + gain + ") is out of range. Must be between " + MIN_GAIN_DB + " and " + MAX_GAIN_DB + ".");
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

    public List<Double> parseEqualizerBands(String value) {
        String[] values = value.split(",");
        if (values.length != EQ_BAND_COUNT) {
            System.err.println("Invalid equalizerBands length. Using default values.");
            return new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
        }

        List<Double> bands = new ArrayList<>();
        for (String v : values) {
            try {
                bands.add(Double.parseDouble(v.trim()));
            } catch (NumberFormatException e) {
                System.err.println("Invalid equalizer band value: " + v + ". Using 0.0.");
                bands.add(0.0);
            }
        }
        return bands;
    }

    public void setCurrentBand(int bandIndex, double gain) {
        checkEqualizerBand(bandIndex, gain);
        currentEqualizerBands.set(bandIndex, gain);
    }

    public double getCurrentBand(int bandIndex) {
        return currentEqualizerBands.get(bandIndex);
    }

    public void setEqualizerBand(int bandIndex, double gain) {
        if (bandIndex < 0 || bandIndex >= EQ_BAND_COUNT) {
            throw new IllegalArgumentException("Invalid band index: " + bandIndex);
        }
        checkEqualizerBand(bandIndex, gain);
        savedEqualizerBands.set(bandIndex, gain);
    }

    public void setSavedEqualizerBands(List<Double> savedEqualizerBands) {
        try {
            checkEqualizerBands(savedEqualizerBands);
            this.savedEqualizerBands = new ArrayList<>(savedEqualizerBands);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            this.savedEqualizerBands = new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
        }
    }

    public List<Double> getSavedEqualizerBands() {
        return savedEqualizerBands;
    }

    public double getMaxGainDB() {
        return MAX_GAIN_DB;
    }

    public double getMinGainDB() {
        return MIN_GAIN_DB;
    }

    @Override
    public String toString() {
        return String.join(",",
                savedEqualizerBands.stream()
                        .map(String::valueOf)
                        .toArray(String[]::new)
        );
    }
}
