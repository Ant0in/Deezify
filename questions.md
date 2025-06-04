Discutez de la qualité de  la méthode `musicApp.models.Song#getCoverImage` et de `musicApp.views.MediaPlayerView#loadDefaultCoverImage` ?

```java
public Image getCoverImage() {
    String defaultCover = getClass().getResource("/images/song.png").toExternalForm();
    if (getCover() == null) {
        return new Image(Objects.requireNonNull(defaultCover));
    }
    try {
        return new Image(new ByteArrayInputStream(getCover()));
    } catch (Exception e) {
        return new Image(Objects.requireNonNull(defaultCover));
    }
}
```

```java
private Image loadDefaultCoverImage() {
    try {
        return new Image(Objects.requireNonNull(
                getClass().getResourceAsStream("/images/song.png"),
                "Default cover image not found"
        ));
    } catch (Exception e) {
        System.err.println("Failed to load default cover image");
        return null;
    }
}
```

---

Vous utilisez à plusieurs endroits des `Optionals`. Quel est leur rôle dans votre code ? (cf par exemple `KaraokeController#handleImportKaraokeLyrics`)

```java
public void handleImportKaraokeLyrics() {
    Song currentSong = playerController.getCurrentlyLoadedSong();
    if (currentSong == null) return;

    Optional<Path> selectedFile = view.getLrcFile();
    if (selectedFile.isEmpty()) return;

    Optional<Boolean> evaluation = evaluateOverwriteTxt(currentSong);
    if (evaluation.isEmpty()) return;
    boolean overwriteTxt = evaluation.get();
    try {
        lyricsManager.importLrc(currentSong, selectedFile.get(), overwriteTxt);
    } catch (Exception e) {
        AlertService as = new AlertService();
        as.showExceptionAlert(e);
    }
    view.updateKaraokeLyrics();
    handleShowKaraoke();
}
```

---

Comment fonctionne algorithmiquement la méthode `models.Library#getAutoCompletion` ? Vous utilisez un `Math.random()`, pourquoi ?

```java
private Optional<String> getAutoCompletion(String input, Function<Song, List<String>> extractor) {
    if (input == null || input.isEmpty() || songList.isEmpty()) {
        return Optional.empty();
    }

    String lowerInput = input.toLowerCase();
    int size = songList.size();
    int randomIndex = (int) (Math.random() * size);

    // Search for an autocompletion starting at a random index
    for (int offset = 0; offset < size; offset++) {
        int i = (randomIndex + offset) % size;
        List<String> fields = extractor.apply(songList.get(i));

        for (String field : fields) {
            if (field == null) continue;
            String lowerField = field.toLowerCase();
            if (lowerField.startsWith(lowerInput) && lowerField.length() > lowerInput.length()) {
                return Optional.of(field);
            }
        }
    }
    return Optional.empty();
}
```

---

Vous avez un unique DTO : `SettingsDTO`. Pourquoi utiliser un DTO spécifiquement pour cette classe ?

```java
package musicApp.models.dtos;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class SettingsDTO {
    private final Path musicFolder;
    private final List<Double> equalizerBands;
    private final double balance;
    private final double crossfadeDuration;
    private final boolean isMusicFolderChanged;
    private Path userMusicFolder;
    private Path userPlaylistPath;

    public SettingsDTO(double _balance, List<Double> _equalizerBands, Path _musicFolder, Path _userMusicFolder, Path _userPlaylistPath, double _crossfadeDuration, boolean _isMusicFolderChanged) {
        balance = _balance;
        equalizerBands = Collections.unmodifiableList(_equalizerBands);
        musicFolder = _musicFolder;
        userMusicFolder = _userMusicFolder;
        userPlaylistPath = _userPlaylistPath;
        crossfadeDuration = _crossfadeDuration;
        isMusicFolderChanged = _isMusicFolderChanged;
    }

    public SettingsDTO(Path _musicFolder, boolean _isMusicFolderChanged) {
        balance = 0;
        equalizerBands = List.of(0.0, 0.0, 0.0, 0.0, 0.0);
        musicFolder = _musicFolder;
        crossfadeDuration = 0;
        isMusicFolderChanged = _isMusicFolderChanged;
    }

    public double getBalance() {
        return balance;
    }

    public double getCrossfadeDuration() {
        return crossfadeDuration;
    }

    public List<Double> getEqualizerBands() {
        return equalizerBands;
    }

    public Path getMusicFolder() {
        return musicFolder;
    }

    public Path getUserMusicFolder() {
        return userMusicFolder;
    }

    public boolean isMusicFolderChanged() {
        return isMusicFolderChanged;
    }

    public Path getUserPlaylistPath() {
        return userPlaylistPath;

    }
}

```

---

Dans `musicApp.models.AudioPlayer`, vous utilisez des `beans properties` et pas dans les autres modèles non. Justifiez ce choix.

```java
package musicApp.models;

import javafx.beans.property.*;
import musicApp.exceptions.BadSongException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * AudioPlayer
 * Class that manages the audio playing.
 */
public class AudioPlayer {

    private final DoubleProperty progress;
    private final StringProperty currentSongString;
    private final BooleanProperty isPlaying;
    private final BooleanProperty isLoaded;
    private final DoubleProperty volume;
    private final Double audioSpectrumInterval;
    private List<Double> equalizerBandsGain;
    private Song loadedSong;
    private double balance;
    private double crossfadeDuration;
    private double speed;
    private Supplier<Song> nextSongSupplier;

    private Boolean isTransitioning;


    public AudioPlayer() {
        // Initialize properties in the constructor
        progress = new SimpleDoubleProperty(0.0);
        currentSongString = new SimpleStringProperty("None");
        isPlaying = new SimpleBooleanProperty(false);
        isLoaded = new SimpleBooleanProperty(false);
        volume = new SimpleDoubleProperty(1.0);
        equalizerBandsGain = new ArrayList<>(Collections.nCopies(10, 0.0));
        loadedSong = null;
        isTransitioning = false;
        balance = 0.0;
        speed = 1.0;
        audioSpectrumInterval = 0.05; // Optimal Speed (lower is blinky, higher is laggy)
    }

    /**
     * Load a song into the player.
     *
     * @param song The song to load.
     */
    public void loadSong(Song song) throws BadSongException {
        loadedSong = song;
        currentSongString.set(song.getSource());
    }

    /**
     * Returns the list of equalizer band gains.
     *
     * @return list of gains for each equalizer band
     */
    public List<Double> getEqualizerBandsGain() {
        return equalizerBandsGain;
    }

    /**
     * Replaces the entire list of equalizer band gains.
     *
     * @param newEqualizerBandsGain list of new gain values
     */
    public void setEqualizerBandsGain(List<Double> newEqualizerBandsGain) {
        equalizerBandsGain = newEqualizerBandsGain;
    }

    /**
     * Get the progress of the song.
     *
     * @return The progress of the song.
     */
    public DoubleProperty getProgressProperty() {
        return progress;
    }

    /**
     * Check if the song is playing.
     *
     * @return True if the song is playing, False otherwise.
     */
    public BooleanProperty getIsPlayingProperty() {
        return isPlaying;
    }

    /**
     * Get the current song property.
     *
     * @return The current song property.
     */
    public StringProperty getCurrentSongStringProperty() {
        return currentSongString;
    }

    /**
     * Get the loaded song.
     *
     * @return The loaded song.
     */
    public Song getLoadedSong() {
        return loadedSong;
    }

    /**
     * Get the volume property.
     *
     * @return The volume property.
     */
    public DoubleProperty getVolumeProperty() {
        return volume;
    }

    /**
     * Gets the current balance setting.
     *
     * @return balance value
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Set the balance of the AudioPlayer.
     *
     * @param newBalance The balance of the AudioPlayer.
     */
    public void setBalance(double newBalance) {
        balance = newBalance;
    }

    /**
     * Retrieves the current playback speed multiplier.
     *
     * @return speed multiplier
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Change speed of the loaded song.
     */
    public void setSpeed(double newSpeed) {
        speed = newSpeed;
    }

    /**
     * Gets the interval for audio spectrum analysis.
     *
     * @return spectrum interval in seconds
     */
    public double getAudioSpectrumInterval() {
        return audioSpectrumInterval;
    }

    /**
     * Marks the player state as loaded.
     */
    public void setLoaded() {
        isLoaded.set(true);
    }

    /**
     * Returns the number of equalizer bands configured.
     *
     * @return count of bands
     */
    public int getEqualizerBandsGainSize() {
        return equalizerBandsGain.size();
    }

    /**
     * Retrieves the gain value for a specific equalizer band.
     *
     * @param bandIndex index of the band (0-based)
     * @return gain value of that band
     */
    public double getEqualizerBandGain(int bandIndex) {
        return equalizerBandsGain.get(bandIndex);
    }

    /**
     * Checks if a transition is currently in progress.
     *
     * @return true if transitioning, false otherwise
     */
    public boolean isTransitioning() {
        return isTransitioning;
    }

    /**
     * Sets the transitioning flag to manage crossfade state.
     *
     * @param transitioning new transitioning state
     */
    public void setTransitioning(boolean transitioning) {
        isTransitioning = transitioning;
    }

    /**
     * Gets the configured crossfade duration.
     *
     * @return crossfade duration in seconds
     */
    public double getCrossfadeDuration() {
        return crossfadeDuration;
    }

    /**
     * Set the duration of the crossfade.
     *
     * @param _crossfadeDuration The duration of the crossfade in seconds.
     */
    public void setCrossfadeDuration(double _crossfadeDuration) {
        crossfadeDuration = _crossfadeDuration;
    }

    /**
     * Retrieves the next song from the supplier.
     *
     * @return next Song instance
     */
    public Song getNextSongSupplier() {
        return nextSongSupplier.get();
    }

    /**
     * Set the next song supplier.
     *
     * @param _nextSongSupplier The next song supplier, which is a function that returns a Song.
     */
    public void setNextSongSupplier(Supplier<Song> _nextSongSupplier) {
        nextSongSupplier = _nextSongSupplier;
    }

    /**
     * Updates the playback progress value.
     *
     * @param newProgress progress ratio (0.0 to 1.0)
     */
    public void setProgress(double newProgress) {
        progress.set(newProgress);
    }

    /**
     * Sets the playing flag to update playback status.
     *
     * @param playing new playing state
     */
    public void setPlaying(boolean playing) {
        isPlaying.set(playing);
    }
}

```

---

Que pensez-vous de l'instanciation et l'utilisation de vos services comme dans `musicApp.models.Song#Song` ?

```java
public Song(Path _filePath) {
    filePath = _filePath;
    lyricsEntry = Optional.empty();
    MetadataService metadataReader = new MetadataService();
    try {
        metadata = metadataReader.getMetadata(filePath.toFile());
        refreshLyrics();
    } catch (ID3TagException | BadFileTypeException | RuntimeException e) {
        metadata = new Metadata(); // Default metadata on error
    } catch (SettingsFilesException e) {
        lyricsEntry = Optional.empty();
    }
}
```

---

Le nombre de méthodes dans `musicApp.views.MediaPlayerView.MediaPlayerViewListener` paraît énorme. Qu'en pensez-vous ? Qu'indique cette métrique ?

```java
public interface MediaPlayerViewListener extends ViewService.ViewServiceListener {
    void handlePauseSong();

    void handleNextSong();

    void handlePreviousSong();

    void handleLaunchDjMode();

    void seek(double duration);

    void changeSpeed(double speed);

    void toggleLyrics(boolean show);

    void toggleShuffle(boolean isShuffle);

    void toggleMiniPlayer(boolean show);

    void handlePlayingStatusChange(Consumer<Boolean> callback);

    void bindProgressProperty(DoubleProperty property);

    void handleProgressChange(Runnable callback);

    void bindVolumeProperty(DoubleBinding divide);

    void handleCurrentSongTitleChange(Consumer<String> callback);

    void handleCurrentSongArtistChange(Consumer<String> callback);

    void handleCurrentImageCoverChange(Consumer<Image> callback);

    void handleLoadedSongStatusChange(Consumer<Boolean> callback);

    Duration getCurrentTime();

    Duration getTotalDuration();
}

```

---

Tous les modèles ne sont pas testés. Pourquoi ?

---

Comment avez-vous géré vos intégrations via git ? Quelle méthodologie d'organisation du git avez-vous mis en place ?

---

Si nous vous laissions une itération de plus pour augmenter la qualité de votre code, que feriez-vous ? Quelles sont les pistes d'amélioration que vous avez identifiées ?