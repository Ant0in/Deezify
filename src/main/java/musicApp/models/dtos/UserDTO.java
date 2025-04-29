package musicApp.models.dtos;

import java.nio.file.Path;
import java.util.List;

public class UserDTO {
    private final String username;
    private final Path profilePicturePath;
    private final double balance;
    private final List<Double> equalizerBands;

    public UserDTO(String _username, Path _profilePicturePath, double _balance, List<Double> _equalizerBands) {
        username = _username;
        profilePicturePath = _profilePicturePath;
        balance = _balance;
        equalizerBands = _equalizerBands;
    }

    public String getUsername() {
        return username;
    }

    public Path getProfilePicturePath() {
        return profilePicturePath;
    }

    public double getBalance() {
        return balance;
    }
    public List<Double> getEqualizerBands() {
        return equalizerBands;
    }
}
