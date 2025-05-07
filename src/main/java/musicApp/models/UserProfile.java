package musicApp.models;

import java.nio.file.Path;
import java.util.List;

import com.google.gson.annotations.Expose;
import musicApp.models.dtos.UserDTO;

public class UserProfile {
    @Expose
    private String username;
    @Expose
    private Path userPicturePath;
    @Expose
    private double balance;
    @Expose
    private Path userMusicPath;
    @Expose
    private double crossfadeDuration;
    private Equalizer equalizer;
    private List<Library> playlists;

    public UserProfile(String _username, Path _userPicturePath, Path _userMusicPath) {
        username = _username;
        userPicturePath = _userPicturePath;
        balance = 0.0;
        userMusicPath = _userMusicPath;
        equalizer = new Equalizer();
    }

    public UserProfile(String _username, Path _userPicturePath, double _balance, Path _userMusicPath, Equalizer _equalizer, double _crossfadeDuration) {
        username = _username;
        userPicturePath = _userPicturePath;
        balance = _balance;
        userMusicPath = _userMusicPath;
        equalizer = _equalizer;
        crossfadeDuration = _crossfadeDuration;
    }
    
    public void setUsername(String newUsername) {
        username = newUsername;
    }

    public String getUsername() {
        return username;
    }

    public void setUserPicturePath(Path newUserPicturePath) {
        userPicturePath = newUserPicturePath;
    }

    public Path getUserPicturePath() {
        return userPicturePath;
    }

    public void setPlaylists(List<Library> newPlaylists) {
        playlists = newPlaylists;
    }

    public List<Library> getPlaylists() {
        return playlists;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double newBalance) {
        balance = newBalance;
    }

    public String getUserMusicPathToString() {
        return userMusicPath.toString();
    }

    public Path getUserMusicPath() {
        return userMusicPath;
    }

    public void setUserMusicPath(Path newUserMusicPath) {
        userMusicPath = newUserMusicPath;
    }

    public Equalizer getEqualizer() {
        return equalizer;
    }

    public void setEqualizer(Equalizer newEqualizer) {
        equalizer = newEqualizer;
    }

    public double getCrossfadeDuration() {
        return crossfadeDuration;
    }

    public void setCrossfadeDuration(double newCrossfadeDuration) {
        crossfadeDuration = newCrossfadeDuration;
    }

    /**
     * Get the equalizer bands of the settings.
     * @return The equalizer bands.
     */
    public List<Double> getEqualizerBands() {
        return equalizer.getBandsGain();
    }

    public UserDTO toDTO() {
        return new UserDTO(username, userPicturePath, balance, equalizer.getBandsGain());
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "username='" + username + '\'' +
                ", userPicturePath=" + userPicturePath +
                ", balance=" + balance +
                ", userMusicPath=" + userMusicPath +
                ", equalizer=" + equalizer +
                ", playlists=" + playlists +
                '}';
    }

    public String getUserPicturePathToString() {
        return userPicturePath != null ? userPicturePath.toString() : null;
    }
}
