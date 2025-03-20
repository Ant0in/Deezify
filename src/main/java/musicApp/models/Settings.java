package musicApp.models;

import musicApp.utils.DataProvider;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Settings class to store the balance and music folder.
 */
public class Settings {
    private double balance;
    private Path musicFolder;
    private List<Double> equalizerBands;

    public static final int EQ_BAND_COUNT = 10;
    public static final double MAX_GAIN_DB = 12;
    public static final double MIN_GAIN_DB = -24;


    /**
     * Constructor
     *
     * @param balance     The balance of the application.
     * @param musicFolder The path to the music folder.
     */
    public Settings(double balance, Path musicFolder, List<Double> equalizerBands) {
        this.balance = balance;
        this.musicFolder = musicFolder;
        setEqualizerBands(equalizerBands);
    }

    /**
     * Parses a settings string to a Settings object.
     * The settings string must be in the following format:
     * balance=0.0
     * musicFolder=/path/to/music/folder
     * if the settings string is null or empty, an IllegalArgumentException is thrown.
     * if the settings string does not contain one of the required fields, it will take the default value.
     *
     * @param settings The settings string.
     */
    public Settings(String settings) throws IllegalArgumentException {
        this.balance = 0.0;
        DataProvider dataProvider = new DataProvider();
        this.musicFolder = dataProvider.getDefaultMusicFolder();
        this.equalizerBands = new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0));

        if (settings == null || settings.isEmpty()) {
            System.err.println("Settings string is null or empty");
            return;
        }

        parseConfigString(settings);
    }

    /**
     * Parses the settings string to set the balance and music folder.
     *
     * @param settings The settings string.
     */
    private void parseConfigString(String settings) {
        String[] lines = settings.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }

            int equalsPos = line.indexOf('=');
            if (equalsPos <= 0 || equalsPos == line.length() - 1) {
                // Invalid line
                continue;
            }

            String key = line.substring(0, equalsPos);
            String value = line.substring(equalsPos + 1);

            switch (key) {
                case "balance":
                    this.balance = this.parseBalance(value);
                    break;
                case "musicFolder":
                    this.musicFolder = this.parseMusicFolder(value);
                    break;
                case "equalizerBands":
                    this.equalizerBands = this.parseEqualizerBands(value);
                    break;
            }
        }
    }

    /**
     * Parse the balance from a string.
     *
     * @param unparsedBalance The balance as a string.
     * @return The balance as a double.
     */
    private double parseBalance(String unparsedBalance) {
        final double MAX_BALANCE = 1.0;
        final double MIN_BALANCE = -1.0;
        final double DEFAULT_BALANCE = 0.0;
        try {
            double balance = Double.parseDouble(unparsedBalance);
            if (balance < MIN_BALANCE || balance > MAX_BALANCE) {
                System.err.println("Balance must be between " + MIN_BALANCE + " and " + MAX_BALANCE);
                return DEFAULT_BALANCE;
            }
            return balance;
        } catch (NumberFormatException e) {
            System.err.println("Balance must be a number");
            return DEFAULT_BALANCE;
        }
    }

    private Path parseMusicFolder(String unparsedMusicFolder) {
        try {
            Path musicFolder = Path.of(unparsedMusicFolder);
            if (!Files.exists(musicFolder) || !Files.isDirectory(musicFolder)) {
                System.err.println("Music folder does not exist");
                DataProvider dataProvider = new DataProvider();
                return dataProvider.getDefaultMusicFolder();
            }
            return musicFolder;
        } catch (Exception e) {
            System.err.println("Invalid music folder path");
            DataProvider dataProvider = new DataProvider();
            return dataProvider.getDefaultMusicFolder();
        }
    }

    private List<Double> parseEqualizerBands(String value) {
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

    /**
     * Get the balance of the settings.
     *
     * @return The balance.
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Set the balance of the settings.
     *
     * @param balance The balance.
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * Get the music folder of the settings.
     *
     * @return The music folder.
     */
    public Path getMusicDirectory() {
        return this.musicFolder;
    }

    /**
     * Set the music folder of the settings.
     *
     * @param musicFolder The music folder.
     */
    public void setMusicFolder(Path musicFolder) {
        this.musicFolder = musicFolder;
    }


    public List<Double> getEqualizerBands() {
        return equalizerBands;
    }

    private void checkEqualizerBands(List<Double> equalizerBands) {
        if (equalizerBands == null || equalizerBands.size() != EQ_BAND_COUNT) {
            throw new IllegalArgumentException("Equalizer bands must have exactly " + EQ_BAND_COUNT + " values.");
        }
        for (int i = 0; i < equalizerBands.size(); i++) {
            checkEqualizerBand(i, equalizerBands.get(i));
        }
    }

    public void setEqualizerBand(int bandIndex, double gain) {
        if (bandIndex < 0 || bandIndex >= EQ_BAND_COUNT) {
            throw new IllegalArgumentException("Invalid band index: " + bandIndex);
        }
        checkEqualizerBand(bandIndex, gain);
        equalizerBands.set(bandIndex, gain);
    }

    private void checkEqualizerBand(int bandIndex, double gain) {
        if (gain < MIN_GAIN_DB || gain > MAX_GAIN_DB) {
            throw new IllegalArgumentException("Equalizer band value for band " + bandIndex + " (" + gain + ") is out of range. Must be between " + MIN_GAIN_DB + " and " + MAX_GAIN_DB + ".");
        }
    }

    public void setEqualizerBands(List<Double> equalizerBands) {
        try {
            checkEqualizerBands(equalizerBands);
            this.equalizerBands = new ArrayList<>(equalizerBands);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            this.equalizerBands = new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
        }
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Settings)) {
            return false;
        }
        Settings settings = (Settings) obj;
        return Double.compare(settings.getBalance(), this.getBalance()) == 0 &&
                settings.getMusicDirectory().equals(this.getMusicDirectory()) &&
                settings.getEqualizerBands().equals(this.getEqualizerBands());
    }

    @Override
    public String toString() {
        String bandsString = String.join(",",
                equalizerBands.stream()
                        .map(String::valueOf)
                        .toArray(String[]::new)
        );

        return "balance=" + balance + "\n" +
                "musicFolder=" + musicFolder.toString() + "\n" +
                "equalizerBands=" + bandsString;
    }
}
