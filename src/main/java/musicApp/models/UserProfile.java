package musicApp.models;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import com.google.gson.annotations.Expose;

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
    @Expose
    private Path userPlaylistPath;
    private Equalizer equalizer;

    public UserProfile(String _username, Path _userPicturePath, Path _userMusicPath) {
        username = _username;
        userPicturePath = _userPicturePath;
        balance = 0.0;
        userMusicPath = _userMusicPath;
        equalizer = new Equalizer();
        crossfadeDuration = 0.0;
        userPlaylistPath = userMusicPath.resolve("playlists.json");
    }

    public UserProfile(String _username, Path _userPicturePath, Path _userMusicPath, Path _userPlaylistPath, double _balance, double _crossfadeDuration, Equalizer _equalizer) {
        username = _username;
        userPicturePath = _userPicturePath;
        balance = _balance;
        userMusicPath = _userMusicPath;
        crossfadeDuration = _crossfadeDuration;
        userPlaylistPath = Objects.requireNonNullElseGet(_userPlaylistPath, () -> userMusicPath.resolve("playlists.json"));
        equalizer = _equalizer;
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

//    public List<Library> getPlaylists() {
//        return playlists;
//    }

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

    public Path getUserPlaylistPath() {
        return userPlaylistPath;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "username='" + username + '\'' +
                ", userPicturePath=" + userPicturePath +
                ", balance=" + balance +
                ", userMusicPath=" + userMusicPath +
                ", equalizer=" + equalizer +
                ", crossfadeDuration=" + crossfadeDuration +
                ", userPlaylistPath=" + userPlaylistPath +
//                ", playlists=" + playlists +
                '}';
    }

    public String getUserPicturePathToString() {
        return userPicturePath != null ? userPicturePath.toString() : null;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UserProfile other) {
            return this.username.equals(other.username) &&
                    this.userPicturePath.equals(other.userPicturePath) &&
                    this.balance == other.balance &&
                    this.userMusicPath.equals(other.userMusicPath) &&
                    this.crossfadeDuration == other.crossfadeDuration &&
                    this.userPlaylistPath.equals(other.userPlaylistPath);
        }
        return false;
    }
}
