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
    /**
     * Contstructor of the UserProfile class.
     * @param _username
     * @param _userPicturePath
     * @param _userMusicPath
     */
    public UserProfile(String _username, Path _userPicturePath, Path _userMusicPath) {
        username = _username;
        userPicturePath = _userPicturePath;
        balance = 0.0;
        userMusicPath = _userMusicPath;
        equalizer = new Equalizer();
        crossfadeDuration = 0.0;
        userPlaylistPath = userMusicPath.resolve("playlists.json");
    }

    /**
     * Constructor of the UserProfile class.
     * @param _username
     * @param _userPicturePath
     * @param _userMusicPath
     * @param _userPlaylistPath
     * @param _balance
     * @param _crossfadeDuration
     * @param _equalizer
     */
    public UserProfile(String _username, Path _userPicturePath, Path _userMusicPath, Path _userPlaylistPath, double _balance, double _crossfadeDuration, Equalizer _equalizer) {
        username = _username;
        userPicturePath = _userPicturePath;
        balance = _balance;
        userMusicPath = _userMusicPath;
        crossfadeDuration = _crossfadeDuration;
        userPlaylistPath = Objects.requireNonNullElseGet(_userPlaylistPath, () -> userMusicPath.resolve("playlists.json"));
        equalizer = _equalizer;
    }
    
    /**
     * Set the username of the userProfile.
     * @param newUsername
     */
    public void setUsername(String newUsername) {
        username = newUsername;
    }

    /**
     * Get the username of the userProfile.
     * @return The username of the userProfile.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the userPicturePath of the userProfile.
     * @param newUserPicturePath
     */
    public void setUserPicturePath(Path newUserPicturePath) {
        userPicturePath = newUserPicturePath;
    }

    /**
     * Get the userPicturePath of the userProfile.
     * @return The userPicturePath of the userProfile.
     */
    public Path getUserPicturePath() {
        return userPicturePath;
    }

    /**
     * Get the balance of the userProfile.
     * @return The balance of the userProfile.
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Set the balance of the userProfile.
     * @param newBalance
     */
    public void setBalance(double newBalance) {
        balance = newBalance;
    }

    /**
     * Get the userMusicPath of the userProfile.
     * @return String of the userMusicPath of the userProfile.
     */
    public String getUserMusicPathToString() {
        return userMusicPath.toString();
    }

    /**
     * Get the userMusicPath of the userProfile.
     * @return The userMusicPath of the userProfile.
     */
    public Path getUserMusicPath() {
        return userMusicPath;
    }

    /**
     * Set the userMusicPath of the userProfile.
     * @param newUserMusicPath
     */
    public void setUserMusicPath(Path newUserMusicPath) {
        userMusicPath = newUserMusicPath;
    }

    /**
     * Get the equalizer of the userProfile.
     * @return The equalizer of the userProfile.
     */
    public Equalizer getEqualizer() {
        return equalizer;
    }

    /**
     * Set the equalizer of the userProfile.
     * @param newEqualizer
     */
    public void setEqualizer(Equalizer newEqualizer) {
        equalizer = newEqualizer;
    }

    /**
     * Get the crossfade duration of the userProfile.
     * @return The crossfade duration of the userProfile.
     */
    public double getCrossfadeDuration() {
        return crossfadeDuration;
    }

    /**
     * Set the crossfade duration of the userProfile.
     * @param newCrossfadeDuration
     */
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

    /**
     * Get the userPlaylistPath of the userProfile.
     * @return The userPlaylistPath of the userProfile.
     */
    public Path getUserPlaylistPath() {
        return userPlaylistPath;
    }

    /**
     * To string method for the UserProfile class.
     */
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
                '}';
    }

    /**
     * Get the userPlaylistPath as a string.
     * @return The userPlaylistPath as a string.
     */
    public String getUserPicturePathToString() {
        return userPicturePath != null ? userPicturePath.toString() : null;
    }

    /**
     * Equals method for the UserProfile class.
     * @param obj The object to compare to.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UserProfile other) {
            return this.username.equals(other.username) &&
                    pathToStringEquals(userPicturePath, other.userPicturePath) &&
                    this.balance == other.balance &&
                    this.userMusicPath.equals(other.userMusicPath) &&
                    this.crossfadeDuration == other.crossfadeDuration &&
                    this.userPlaylistPath.equals(other.userPlaylistPath);
        }
        return false;
    }
    
    /**
     * Check if two paths are equal.
     * Null and "" are considered equal.
     * @param p1
     * @param p2
     * @return
     */
    private boolean pathToStringEquals(Path p1, Path p2) {
        String s1 = (p1 == null) ? "" : p1.toString();
        String s2 = (p2 == null) ? "" : p2.toString();
        return s1.equals(s2);
    }

    public void setEqualizerBandsGain(List<Double> newEqualizerBandsGain) {
        equalizer.setBandsGain(newEqualizerBandsGain);
    }
}
