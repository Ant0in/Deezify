package musicApp.models;

import java.nio.file.Path;
import java.util.List;

import com.google.gson.annotations.Expose;
import musicApp.models.dtos.UserDTO;

public class User {
    @Expose
    private String username;
    @Expose
    private Path userPicturePath;
    @Expose
    private double balance;
    @Expose
    private Path userMusicPath;
    private Equalizer equalizer;
    private List<Library> playlists;

    public User(String _username, Path _userPicturePath,Path _userMusicPath) {
        username = _username;
        userPicturePath = _userPicturePath;
        balance = 0;
        userMusicPath = _userMusicPath;
        equalizer = new Equalizer();
    }

    public User(String _username, Path _userPicturePath, double _balance, Path _userMusicPath, Equalizer _equalizer) {
        username = _username;
        userPicturePath = _userPicturePath;
        balance = _balance;
        userMusicPath = _userMusicPath;
        equalizer = _equalizer;
    }
    
    public void setUsername(String newUsername) {
        username = newUsername;
    }

    public String getUsername() {
        return username;
    }

    public void setProfilePicturePath(Path newProfilePicturePath) {
        userPicturePath = newProfilePicturePath;
    }

    public Path getProfilePicturePath() {
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

    public void setUserMusicPath(Path newUserMusicPath) {
        userMusicPath = newUserMusicPath;
    }

    public Equalizer getEqualizer() {
        return equalizer;
    }

    public void setEqualizer(Equalizer newEqualizer) {
        equalizer = newEqualizer;
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
        return "User{" +
                "username='" + username + '\'' +
                ", userPicturePath=" + userPicturePath +
                ", balance=" + balance +
                ", userMusicPath=" + userMusicPath +
                ", equalizer=" + equalizer +
                ", playlists=" + playlists +
                '}';
    }

    public String getProfilePicturePathToString() {
        return userPicturePath != null ? userPicturePath.toString() : null;
    }
}
