package musicApp.models.dtos;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class SettingsDTO {
    private final double balance;
    private final List<Double> equalizerBands;
    private final Path musicFolder;
    private final boolean isMusicFolderChanged;

    public SettingsDTO(double _balance, List<Double> _equalizerBands, Path _musicFolder, boolean _isMusicFolderChanged) {
        balance = _balance;
        equalizerBands = Collections.unmodifiableList(_equalizerBands);
        musicFolder = _musicFolder;
        isMusicFolderChanged = _isMusicFolderChanged;
    }

    public SettingsDTO(Path _musicFolder, boolean _isMusicFolderChanged) {
        balance = 0;
        equalizerBands = Collections.unmodifiableList(List.of(0.0, 0.0, 0.0, 0.0, 0.0));
        musicFolder = _musicFolder;
        isMusicFolderChanged = _isMusicFolderChanged;
    }

    public double getBalance() {
        return balance;
    }

    public List<Double> getEqualizerBands() {
        return equalizerBands;
    }

    public Path getMusicFolder() {
        return musicFolder;
    }

    public boolean isMusicFolderChanged() {
        return isMusicFolderChanged;
    }

}
