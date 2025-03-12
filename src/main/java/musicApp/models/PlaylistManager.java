package musicApp.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * PlaylistManager
 * Class that manages the playlist of songs.
 */

public class PlaylistManager {
    private Library library;
    private List<Song> originalLibrary;
    private boolean isShuffleMode;

    /**
     * Constructor
     *
     * @param library The list of songs in the library.
     */
    public PlaylistManager(Library library) {
        this.library = library;
        this.originalLibrary = new ArrayList<>(library.toList()); // Save original state
        this.isShuffleMode = false;
    }

    /**
     * Return whenever the shuffle mode is enabled.
     *
     * @return True if the shuffle mode is enabled, false otherwise.
     */
    public boolean isShuffleMode() {
        return isShuffleMode;
    }

    /**
     * Toggle the shuffle mode.
     *
     * @param isEnabled   The shuffle button state.
     * @param currentSong The current song.
     */
    public void toggleShuffle(boolean isEnabled, Song currentSong) {
        isShuffleMode = isEnabled;

        if (isShuffleMode) {
            shuffleLibrary(currentSong);
        } else {
            restoreLibrary(currentSong);
        }
    }

    /**
     * Shuffle the library.
     *
     * @param currentSong The current song.
     */
    private void shuffleLibrary(Song currentSong) {
        if (originalLibrary.isEmpty()) {
            originalLibrary = new ArrayList<>(library.toList());
        }
        List<Song> shuffledLibrary = new ArrayList<>(originalLibrary);
        Collections.shuffle(shuffledLibrary);
        if (currentSong != null) {
            shuffledLibrary.remove(currentSong);
            shuffledLibrary.add(0, currentSong);
        }
        library.clear();
        library.addSongs(shuffledLibrary);
    }

    /**
     * Restore the library to its original state.
     *
     * @param currentSong The current song.
     */
    private void restoreLibrary(Song currentSong) {
        if (originalLibrary != null) {
            library.clear();
            library.addSongs(originalLibrary);
            if (currentSong != null) {
                // Find original index of the current song
                int originalIndex = originalLibrary.indexOf(currentSong);
                if (originalIndex != -1) {
                    // Remove the current song from the shuffled library and add it back to the original index
                    library.remove(currentSong);
                    library.add(originalIndex, currentSong);
                }
            }
        }
    }

    /**
     * Get the original index of the song in the library.
     *
     * @param song The song to get the original index.
     * @return The original index of the song in the library.
     */
    public int getOriginalIndex(Song song) {
        return originalLibrary.indexOf(song);
    }
}
