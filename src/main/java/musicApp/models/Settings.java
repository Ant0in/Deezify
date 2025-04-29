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
    private User currentUser;
    private boolean musicFolderChanged;
    
    /**
     * Constructor for Settings.
     *
     * @param _musicFolder  The music folder of the settings.
     */
    public Settings(Path _musicFolder) {
        musicFolderChanged = false;
        musicFolder = _musicFolder;
        currentUser = null;
    }

    public Settings(Path _musicFolder, User _currentUser) {
        musicFolderChanged = false;
        musicFolder = _musicFolder;
        currentUser = _currentUser;
    }

    public SettingsDTO toDTO() {
        if (currentUser == null) {
            return new SettingsDTO(musicFolder, musicFolderChanged);
        } else {
            return new SettingsDTO(currentUser.getBalance(), currentUser.getEqualizerBands(), musicFolder, musicFolderChanged);
        }
    }

    /**
     * Get the balance of the settings.
     *
     * @return The balance.
     */
    public double getBalance() {
        if (currentUser == null) {
            return 0;
        } else {
            return currentUser.getBalance();
        }
    }

    /**
     * Set the balance of the settings.
     *
     * @param newBalance The balance.
     */
    public void setBalance(double newBalance) {
        if (currentUser != null) {
            currentUser.setBalance(newBalance);
        } else {
            throw new IllegalStateException("Cannot set balance when currentUser is null.");
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
     * Set the music folder of the settings.
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

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User newCurrentUser) {
        currentUser = newCurrentUser;
    }

    public void removeCurrentUser() {
        currentUser = null;
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

    public List<Double> getEqualizerBands() {
        if (currentUser == null) {
            return new ArrayList<>(java.util.Collections.nCopies(Equalizer.DEFAULT_BANDS_SIZE, 0.0));
        } else {
            return currentUser.getEqualizerBands();
        }
    }

    /**
     * Returns a string representation of the settings object.
     * @return A string representation of the settings object.
     */
    @Override
    public String toString() {
        return "Settings{musicFolder=" + musicFolder.toString() +
                "currentUser=" + currentUser + "}\n";
    }
}