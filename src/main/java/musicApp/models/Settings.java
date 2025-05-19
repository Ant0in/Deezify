package musicApp.models;

import com.google.gson.annotations.Expose;
import musicApp.enums.Language;
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
    private Language currentLanguage;

    /**
     * Constructor for Settings.
     *
     * @param _musicFolder The music folder of the settings.
     */
    public Settings(Path _musicFolder, String _language) {
        musicFolderChanged = false;
        musicFolder = _musicFolder;
        currentUserProfile = null;
        currentLanguage = Language.fromCode(_language);
    }

    /**
     * Instantiates a new Settings.
     *
     * @param _musicFolder        the music folder
     * @param _currentUserProfile the current user profile
     */
    public Settings(Path _musicFolder, UserProfile _currentUserProfile) {
        musicFolderChanged = false;
        musicFolder = _musicFolder;
        currentUserProfile = _currentUserProfile;
    }

    public Settings(Path _musicFolder, UserProfile _currentUserProfile, Language _language) {
        musicFolderChanged = false;
        musicFolder = _musicFolder;
        currentUserProfile = _currentUserProfile;
        currentLanguage = _language;
    }

    /**
     * To dto settings dto.
     *
     * @return the settings dto
     */
    public SettingsDTO toDTO() {
        if (currentUserProfile == null) {
            return new SettingsDTO(musicFolder, musicFolderChanged);
        } else {
            return new SettingsDTO(currentUserProfile.getBalance(), currentUserProfile.getEqualizerBands(), musicFolder,
                    currentUserProfile.getUserMusicPath(), currentUserProfile.getUserPlaylistPath(),
                    currentUserProfile.getCrossfadeDuration(), musicFolderChanged);
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
     * Get the music folder of the settings.
     *
     * @return The music folder.
     */
    public Path getMusicFolder() {
        return musicFolder;
    }

    /**
     * Set the global music folder for the app.
     *
     * @param newMusicFolder The music folder.
     */
    public void setMusicFolder(Path newMusicFolder) {
        if (musicFolder != newMusicFolder) {
            musicFolder = newMusicFolder;
            musicFolderChanged = true;
        } else {
            musicFolderChanged = false;
        }
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
     * Gets crossfade duration.
     *
     * @return the crossfade duration
     */
    public double getCrossfadeDuration() {
        if (currentUserProfile == null) {
            return 0;
        } else {
            return currentUserProfile.getCrossfadeDuration();
        }
    }

    /**
     * Gets current user profile.
     *
     * @return the current user profile
     */
    public UserProfile getCurrentUserProfile() {
        return currentUserProfile;
    }

    public void setCurrentUserProfile(UserProfile newCurrentUserProfile) {
        currentUserProfile = newCurrentUserProfile;
    }

    /**
     * Get the settings current language.
     *
     * @return the settings current language.
     */
    public Language getCurrentLanguage() {
        return currentLanguage;
    }

    /**
     * Set the settings current language.
     *
     * @param newCurrentLanguage the new current language.
     */
    public void setCurrentLanguage(Language newCurrentLanguage) {
        currentLanguage = newCurrentLanguage;
    }

    /**
     * Checks if the settings object is equal to another settings object.
     *
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

    /**
     * Gets equalizer bands.
     *
     * @return the equalizer bands
     */
    public List<Double> getEqualizerBands() {
        if (currentUserProfile == null) {
            return new ArrayList<>(java.util.Collections.nCopies(Equalizer.DEFAULT_BANDS_SIZE, 0.0));
        } else {
            return currentUserProfile.getEqualizerBands();
        }
    }

    /**
     * Gets user playlist path.
     *
     * @return the user playlist path
     */
    public Path getUserPlaylistPath() {
        if (currentUserProfile == null) {
            return null;
        } else {
            return currentUserProfile.getUserPlaylistPath();
        }
    }

    /**
     * Returns a string representation of the settings object.
     *
     * @return A string representation of the settings object.
     */
    @Override
    public String toString() {
        return "Settings{musicFolder=" + musicFolder.toString() +
                "currentUserProfile=" + currentUserProfile + "}\n";
    }

    public Path getUserMusicFolder() {
        if (currentUserProfile == null) {
            return null;
        } else {
            return currentUserProfile.getUserMusicPath();
        }
    }
}