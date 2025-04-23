package musicApp.models.dtos;

import java.nio.file.Path;
import java.util.List;

public class SettingsDTO {
    private double balance;
    private List<Double> equalizerBands;
    private Path musicFolder;
    
    public SettingsDTO(double _balance, List<Double> _equalizerBands, Path _musicFolder) {
        balance = _balance;
        equalizerBands = _equalizerBands;
        musicFolder = _musicFolder;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double newBalance) {
        balance = newBalance;
    }

    public List<Double> getEqualizerBands() {
        return equalizerBands;
    }

    public void setEqualizerBands(List<Double> newEqualizerBands) {
        equalizerBands = newEqualizerBands;
    }

    public Path getMusicFolder() {
        return musicFolder;
    }

    public void setMusicFolder(Path newMusicFolder) {
        musicFolder = newMusicFolder;
    }
}
