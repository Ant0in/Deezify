package musicApp.models;

import com.google.gson.annotations.Expose;
import musicApp.models.dtos.SettingsDTO;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Settings class to store the balance and music folder.
 */
public class Settings {
    @Expose
    private Path musicFolder;
    private UserProfile currentUserProfile;
    private boolean musicFolderChanged;

    /**
     * Constructor for Settings.
     *
     * @param _musicFolder  The music folder of the settings.
     */
    public Settings(Path _musicFolder) {
            musicFolderChanged = false;
            musicFolder = _musicFolder;
            currentUserProfile = null;
        }

    public Settings(Path _musicFolder, UserProfile _currentUserProfile) {
        musicFolderChanged = false;
        musicFolder = _musicFolder;
        currentUserProfile = _currentUserProfile;
    }

    public SettingsDTO toDTO() {
        if (currentUserProfile == null) {
            return new SettingsDTO(musicFolder, musicFolderChanged);
        } else {
            return new SettingsDTO(currentUserProfile.getBalance(), currentUserProfile.getEqualizerBands(), musicFolder, currentUserProfile.getCrossfadeDuration(), musicFolderChanged);
        }
    }

    /**
     * Get the balance of the settings.
     *
     * @return The balance.
     */
    public double getBalance() {
        if (currentUserProfile != null) {
            return currentUserProfile.getBalance();
        } else {
            throw new IllegalStateException("Cannot get balance when currentUserProfile is null.");
        }
    }

    /**
     * Set the balance of the settings.
     *
     * @param newBalance The balance.
     */
    public void setBalance(double newBalance) {
        if (currentUserProfile != null) {
            currentUserProfile.setBalance(newBalance);
        } else {
            throw new IllegalStateException("Cannot set balance when currentUserProfile is null.");
        }
    }

    /**
     * Get the music folder of the settings.
     *
     * @return The music folder.
     */
    public Path getMusicFolder() {
        return musicFolder;
    }

    /**
     * Get the music folder of the settings as a string.
     *
     * @return The music folder as a string.
     */
    public String getMusicFolderString() {
        return musicFolder.toString();
    }

    /**
     * Set the global music folder for the app.
     *
     * @param newMusicFolder The music folder.
     */
    public void setMusicFolder(Path newMusicFolder) {
        if (musicFolder != newMusicFolder){
            musicFolder = newMusicFolder;
            musicFolderChanged = true;
        } else {
            musicFolderChanged = false;
        }
    }

    /**
     * returns the user's music folder
     *
     * @return The music folder.
     */
    public Path getUserMusicFolder() {
        if (currentUserProfile != null) {
            return currentUserProfile.getUserMusicPath();
        } else {
            throw new IllegalStateException("Cannot get user music folder when currentUserProfile is null.");
        }
    }

    /**
     * returns the user's music folder as a string
     *
     * @return The music folder as a string.
     */
    public String getUserMusicFolderString() {
        if (currentUserProfile != null) {
            return currentUserProfile.getUserMusicPathToString();
        } else {
            throw new IllegalStateException("Cannot get user music folder when currentUserProfile is null.");
        }
    }


    public double getCrossfadeDuration() {
        if (currentUserProfile == null) {
            return 0;
        } else {
            return currentUserProfile.getCrossfadeDuration();
        }
    }

    public void setCrossfadeDuration(double _crossfadeDuration) {
        if (currentUserProfile != null) {
            currentUserProfile.setCrossfadeDuration(_crossfadeDuration);
        } else {
            throw new IllegalStateException("Cannot set crossfade duration when currentUserProfile is null.");
        }
    }


    public UserProfile getCurrentUser() {
        return currentUserProfile;
    }

    public void setCurrentUserProfile(UserProfile newCurrentUserProfile) {
        currentUserProfile = newCurrentUserProfile;
    }

    public void removeCurrentUser() {
        currentUserProfile = null;
    }

    /**
     * Checks if the settings object is equal to another settings object.
     * @param obj The object to compare with.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Settings otherSettings)) {
            return false;
        }
        return Double.compare(otherSettings.getBalance(), getBalance()) == 0 &&
                otherSettings.getMusicFolder().equals(getMusicFolder()) &&
                otherSettings.getEqualizerBands().equals(getEqualizerBands());
    }

    public Equalizer getEqualizer() {
        if (currentUserProfile == null) {
            return new Equalizer();
        } else {
            return currentUserProfile.getEqualizer();
        }
    }

    public List<Double> getEqualizerBands() {
        if (currentUserProfile == null) {
            return new ArrayList<>(java.util.Collections.nCopies(Equalizer.DEFAULT_BANDS_SIZE, 0.0));
        } else {
            return currentUserProfile.getEqualizerBands();
        }
    }

    /**
     * Returns a string representation of the settings object.
     * @return A string representation of the settings object.
     */
    @Override
    public String toString() {
        return "Settings{musicFolder=" + musicFolder.toString() +
                "currentUserProfile=" + currentUserProfile + "}\n";
    }
}