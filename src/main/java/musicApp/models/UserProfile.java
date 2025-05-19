package musicApp.models;

import com.google.gson.annotations.Expose;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

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
    @Expose
    private String language;

    /**
     * Contstructor of the UserProfile class.
     *
     * @param _username        username of the userProfile.
     * @param _userPicturePath path to the profile picture of the userProfile.
     * @param _userMusicPath   path to the music folder of the userProfile.
     */
    public UserProfile(String _username, Path _userPicturePath, Path _userMusicPath, String _language, double _balance, double _crossfadeDuration) {
        username = _username;
        userPicturePath = _userPicturePath;
        balance = _balance;
        userMusicPath = _userMusicPath;
        equalizer = new Equalizer();
        crossfadeDuration = _crossfadeDuration;
        userPlaylistPath = userMusicPath.resolve("playlists.json");
        language = _language;
    }

    /**
     * Constructor of the UserProfile class.
     *
     * @param _username          username of the userProfile.
     * @param _userPicturePath   path to the profile picture of the userProfile.
     * @param _userMusicPath     path to the music folder of the userProfile.
     * @param _userPlaylistPath  path to the playlist folder of the userProfile.
     * @param _balance           music balance of the userProfile.
     * @param _crossfadeDuration crossfade duration of the userProfile.
     * @param _equalizer         equalizer of the userProfile.
     */
    public UserProfile(String _username, Path _userPicturePath, Path _userMusicPath, String _language, Path _userPlaylistPath, double _balance, double _crossfadeDuration, Equalizer _equalizer) {
        username = _username;
        userPicturePath = _userPicturePath;
        balance = _balance;
        userMusicPath = _userMusicPath;
        crossfadeDuration = _crossfadeDuration;
        userPlaylistPath = Objects.requireNonNullElseGet(_userPlaylistPath, () -> userMusicPath.resolve("playlists.json"));
        equalizer = _equalizer;
        language = _language;
    }

    /**
     * Get the username of the userProfile.
     *
     * @return The username of the userProfile.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the username of the userProfile.
     *
     * @param newUsername new username of the userProfile.
     */
    public void setUsername(String newUsername) {
        username = newUsername;
    }

    /**
     * Get the userPicturePath of the userProfile.
     *
     * @return The userPicturePath of the userProfile.
     */
    public Path getUserPicturePath() {
        return userPicturePath;
    }

    /**
     * Set the userPicturePath of the userProfile.
     *
     * @param newUserPicturePath new userPicturePath of the userProfile.
     */
    public void setUserPicturePath(Path newUserPicturePath) {
        userPicturePath = newUserPicturePath;
    }

    /**
     * Get the balance of the userProfile.
     *
     * @return The balance of the userProfile.
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Set the balance of the userProfile.
     *
     * @param newBalance new balance of the userProfile.
     */
    public void setBalance(double newBalance) {
        balance = newBalance;
    }

    /**
     * Get the userMusicPath of the userProfile.
     *
     * @return String of the userMusicPath of the userProfile.
     */
    public String getUserMusicPathToString() {
        return userMusicPath.toString();
    }

    /**
     * Get the userMusicPath of the userProfile.
     *
     * @return The userMusicPath of the userProfile.
     */
    public Path getUserMusicPath() {
        return userMusicPath;
    }

    /**
     * Set the userMusicPath of the userProfile.
     *
     * @param newUserMusicPath new userMusicPath of the userProfile.
     */
    public void setUserMusicPath(Path newUserMusicPath) {
        userMusicPath = newUserMusicPath;
    }

    /**
     * Get the equalizer of the userProfile.
     *
     * @return The equalizer of the userProfile.
     */
    public Equalizer getEqualizer() {
        return equalizer;
    }

    /**
     * Set the equalizer of the userProfile.
     *
     * @param newEqualizer new equalizer of the userProfile.
     */
    public void setEqualizer(Equalizer newEqualizer) {
        equalizer = newEqualizer;
    }

    /**
     * Get the crossfade duration of the userProfile.
     *
     * @return The crossfade duration of the userProfile.
     */
    public double getCrossfadeDuration() {
        return crossfadeDuration;
    }

    /**
     * Set the crossfade duration of the userProfile.
     *
     * @param newCrossfadeDuration new crossfade duration of the userProfile.
     */
    public void setCrossfadeDuration(double newCrossfadeDuration) {
        crossfadeDuration = newCrossfadeDuration;
    }

    /**
     * Get the equalizer bands of the settings.
     *
     * @return The equalizer bands.
     */
    public List<Double> getEqualizerBands() {
        return equalizer.getBandsGain();
    }

    /**
     * Get the userPlaylistPath of the userProfile.
     *
     * @return The userPlaylistPath of the userProfile.
     */
    public Path getUserPlaylistPath() {
        return userPlaylistPath;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String newLanguage) {
        language = newLanguage;
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
                ", userPlaylistPath=" + userPlaylistPath +
                ", language='" + language +
                '}';
    }

    /**
     * Get the userPlaylistPath as a string.
     *
     * @return The userPlaylistPath as a string.
     */
    public String getUserPicturePathToString() {
        return userPicturePath != null ? userPicturePath.toString() : null;
    }

    /**
     * Equals method for the UserProfile class.
     *
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
     *
     * @param p1 path 1
     * @param p2 path 2
     * @return true if the paths are equal, false otherwise
     */
    private boolean pathToStringEquals(Path p1, Path p2) {
        String s1 = (p1 == null) ? "" : p1.toString();
        String s2 = (p2 == null) ? "" : p2.toString();
        return s1.equals(s2);
    }

    public void setUserPlaylistsPath(Path musicPath) {
        userPlaylistPath = musicPath.resolve("playlists.json");
    }
}
