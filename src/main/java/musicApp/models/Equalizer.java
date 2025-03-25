package musicApp.models;

import com.google.gson.annotations.Expose;

import java.util.*;

public class Equalizer {
    @Expose
    private List<Double> bandsGain = new ArrayList<>();
    // default javafx frequencies
    private final ArrayList<Integer> BANDS_FREQUENCY = new ArrayList<>(Arrays.asList(32, 64, 125, 250, 500, 1000, 2000, 4000, 8000, 16000));


    public final double MAX_GAIN_DB = 12;
    public final double MIN_GAIN_DB = -24;

    public Equalizer() {
        List<Double> defaultBandsGain = Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        setBandsGain(defaultBandsGain);
    }

    public Equalizer(List<Double> bandsGain) {
        checkBandsGain(bandsGain);
        setBandsGain(bandsGain);
    }

    private int getBandsSize() {
        return BANDS_FREQUENCY.size();
    }

    private void setBandsGain(List<Double> bandsGain) {
        this.bandsGain = bandsGain;
    }


    private void checkBand(int bandIndex, double gain) {
        checkBandIndex(bandIndex);
        if (gain < MIN_GAIN_DB || gain > MAX_GAIN_DB) {
            throw new IllegalArgumentException("Equalizer band value for band " + bandIndex + " (" + gain + ") is out of range. Must be between " + MIN_GAIN_DB + " and " + MAX_GAIN_DB + ".");
        }
    }

    private void checkBandIndex(int bandIndex) {
        if (bandIndex < 0 || bandIndex >= getBandsSize()) {
            throw new IllegalArgumentException("Invalid band index: " + bandIndex);
        }
    }

    private void checkBandsGain(List<Double> bandsGain) {
        if (bandsGain == null || bandsGain.size() != getBandsSize()) {
            throw new IllegalArgumentException("Equalizer bands must have exactly " + getBandsSize() + " values.");
        }
        for (int i = 0; i < bandsGain.size(); i++) {
            checkBand(i, bandsGain.get(i));
        }
    }

    public double getBandGain(int bandIndex) {
        return bandsGain.get(bandIndex);
    }

    public void setBandGain(int bandIndex, double gain) {
        checkBand(bandIndex, gain);
        bandsGain.set(bandIndex, gain);
    }

    public List<Double> getBandsGain() {
        return bandsGain;
    }

    public double getMaxGainDB() {
        return MAX_GAIN_DB;
    }

    public double getMinGainDB() {
        return MIN_GAIN_DB;
    }

    public int getBandFrequency(int bandIndex) {
        return BANDS_FREQUENCY.get(bandIndex);
    }

    @Override
    public String toString() {
        return String.join(",",
                getBandsGain().stream()
                        .map(String::valueOf)
                        .toArray(String[]::new)
        );
    }
}