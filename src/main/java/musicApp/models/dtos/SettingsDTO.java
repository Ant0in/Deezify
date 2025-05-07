package musicApp.models.dtos;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class SettingsDTO {
    private final Path musicFolder;
    private final List<Double> equalizerBands;
    private final double balance;
    private final double crossfadeDuration;
    private final boolean isMusicFolderChanged;

    public SettingsDTO(double _balance, List<Double> _equalizerBands, Path _musicFolder, double _crossfadeDuration, boolean _isMusicFolderChanged) {
        balance = _balance;
        equalizerBands = Collections.unmodifiableList(_equalizerBands);
        musicFolder = _musicFolder;
        crossfadeDuration = _crossfadeDuration;
        isMusicFolderChanged = _isMusicFolderChanged;
    }

    public SettingsDTO(Path _musicFolder, boolean _isMusicFolderChanged) {
        balance = 0;
        equalizerBands = Collections.unmodifiableList(List.of(0.0, 0.0, 0.0, 0.0, 0.0));
        musicFolder = _musicFolder;
        crossfadeDuration = 0;
        isMusicFolderChanged = _isMusicFolderChanged;
    }

    public double getBalance() {
        return balance;
    }

    public double getCrossfadeDuration() {
        return crossfadeDuration;
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
