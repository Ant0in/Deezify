package musicApp.models;

import com.google.gson.annotations.Expose;

import java.util.*;

public class Equalizer {
    @Expose
    // Map frequency:gain
    private final Map<Integer, Double> bandMap = new HashMap<>();
    private final ArrayList<Integer> BAND_FREQUENCIES = new ArrayList<>(Arrays.asList(32, 64, 125, 250, 500, 1000, 2000, 4000, 8000, 16000));


    public final double MAX_GAIN_DB = 12;
    public final double MIN_GAIN_DB = -24;

    public Equalizer() {
        List<Double> defaultBandsGain = Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        setBandMap(defaultBandsGain);
    }

    public Equalizer(List<Double> bandsGain) {
        checkBandsGain(bandsGain);
        setBandMap(bandsGain);
    }

    private int getBandsSize(){
        return BAND_FREQUENCIES.size();
    }

    private void setBandMap(List<Double> bandsGain) {
        for (int i = 0; i < getBandsSize(); i++) {
            bandMap.put(BAND_FREQUENCIES.get(i), bandsGain.get(i));
        }
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

    private int getBandMapKey(int bandIndex) {
        return BAND_FREQUENCIES.get(bandIndex);
    }

    public double getBandGain(int bandIndex) {
        return bandMap.get(getBandMapKey(bandIndex));
    }

    public void setBandGain(int bandIndex, double gain) {
        checkBand(bandIndex, gain);
        int frequency = getBandMapKey(bandIndex);
        bandMap.put(frequency, gain);
    }

    public List<Double> getBandsGain() {
        List<Double> bandsGain = new ArrayList<>();
        for (int i = 0; i < getBandsSize(); i++) {
            bandsGain.add(getBandGain(i));
        }
        return bandsGain;
    }

    public double getMaxGainDB() {
        return MAX_GAIN_DB;
    }

    public double getMinGainDB() {
        return MIN_GAIN_DB;
    }

    public int getBandFrequency(int bandIndex){
        checkBandIndex(bandIndex);
        return getBandMapKey(bandIndex);
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