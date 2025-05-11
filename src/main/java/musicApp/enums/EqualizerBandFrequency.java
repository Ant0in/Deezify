package musicApp.enums;

public enum EqualizerBandFrequency {
    BAND_32_HZ(32),
    BAND_64_HZ(64),
    BAND_125_HZ(125),
    BAND_250_HZ(250),
    BAND_500_HZ(500),
    BAND_1000_HZ(1000),
    BAND_2000_HZ(2000),
    BAND_4000_HZ(4000),
    BAND_8000_HZ(8000),
    BAND_16000_HZ(16000);

    private final int frequencyHz;

    EqualizerBandFrequency(int frequencyHz) {
        this.frequencyHz = frequencyHz;
    }

    public int getFrequencyHz() {
        return frequencyHz;
    }

    public static int getBandsSize() {
        return values().length;
    }

    public static int getIndex(EqualizerBandFrequency band) {
        return band.ordinal();
    }

    public static EqualizerBandFrequency fromIndex(int index) throws IndexOutOfBoundsException {
        EqualizerBandFrequency[] values = values();
        if (index < 0 || index >= values.length) {
            throw new IndexOutOfBoundsException("Invalid band index: " + index);
        }
        return values[index];
    }
}
