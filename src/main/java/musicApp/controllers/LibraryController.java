package musicApp.controllers;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
import musicApp.exceptions.SettingsFilesException;
import musicApp.models.Library;
import musicApp.models.Song;
import musicApp.services.PlaylistService;
import musicApp.views.LibraryView;
import musicApp.services.LanguageService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

/**
 * The controller for the Main Library view.
 */
public class LibraryController extends SongContainerController<LibraryView, Library>
        implements LibraryView.LibraryViewListener {

    private final int MAX_SUGGESTIONS = 5;
    private final LanguageService LANG = LanguageService.getInstance();
    private final String DEFAULT_ARTIST = LANG.get("metadata.artist");
    private final String DEFAULT_ALBUM  = LANG.get("metadata.album");
    private final String DEFAULT_GENRE  = LANG.get("metadata.genre");

    private int currentIndex;
    private Boolean shuffle;

    private Optional<Song> nextSong;

    /**
     * Instantiates a new Main library controller.
     *
     * @param controller the controller
     */
    public LibraryController(PlayerController controller, Library mainLibrary) {
        super(new LibraryView(), controller);
        view.setListener((LibraryView.LibraryViewListener) this);
        shuffle = false;
        nextSong = Optional.empty();
        initView("/fxml/MainLibrary.fxml");
        loadPlaylist(mainLibrary);
    }

    /**
     * Loads a new playlist into the library and updates the view.
     *
     * @param playlist The Library object representing the new playlist to load.
     */
    public void loadPlaylist(Library playlist) {
        library = playlist;
        view.updateListView();
    }

    /**
     * Skip to the next song in the library.
     */
    public void skip() {
        playSong(getNextSong());
        nextSong = Optional.empty();
    }

    /**
     * Go back to the previous song in the library.
     */
    public void prec() {
        if (currentIndex > 0) {
            playSong(currentIndex - 1);
        }
    }

    /**
     * Search the library for songs that match the query.
     *
     * @param query The query to search for.
     * @return A list of song names that match the query.
     */
    public List<Song> searchLibrary(String query) {
        return library.search(query);
    }

    /**
     * Gets the boolean binding of the selected song.
     *
     * @return the boolean binding
     */
    public BooleanBinding isSelected() {
        return view.isSelected();
    }

    /**
     * Gets selected song.
     *
     * @return the selected song
     */
    public Song getSelectedSong() {
        return getSong(view.getSelectedSongIndex());
    }

    /**
     * Retrieves the index of a given song in the music library.
     *
     * @param song The Song object whose index is to be found.
     * @return The index of the song in the library list, or -1 if the song is not found.
     */
    private int getSongIndex(Song song) {
        return library.toList().indexOf(song);
    }

    /**
     * Plays a song at the specified index in the library.
     *
     * @param index The index of the song to play.
     */
    @Override
    public void playSong(int index) {
        currentIndex = index;
        super.playSong(index);
    }

    /**
     * Plays the specified song.
     *
     * @param song The Song object to play.
     */
    @Override
    public void playSong(Song song) {
        currentIndex = getSongIndex(song);
        super.playSong(song);
    }

    /**
     * Clear queue selection.
     */
    public void handleSelectionChange() {
        playerController.clearQueueSelection();
    }

    /**
     * Toggle shuffle.
     */
    public void toggleShuffle(boolean isShuffle) {
        shuffle = isShuffle;
    }

    /**
     * Gets the currently loaded song.
     *
     * @return the currently loaded song
     */
    public Song getCurrentlyLoadedSong() {
        return playerController.getCurrentlyLoadedSong();
    }

    /**
     * Gets the currently loaded song string property.
     *
     * @return the currently loaded song string property
     */
    public StringProperty getCurrentlyLoadedSongStringProperty() {
        return playerController.getCurrentlyLoadedSongStringProperty();
    }

    /**
     * Returns if the player is currently playing a song.
     *
     * @return the boolean property
     */
    public BooleanProperty getIsPlayingProperty() {
        return playerController.getIsPlayingProperty();
    }

    /**
     * Toggle the favorites status of a song.
     * If the song is already a favorite, it will be removed from the favorites.
     * If the song is not a favorite, it will be added to the favorites.
     *
     * @param song the song
     */
    public void toggleFavorites(Song song) {
        playerController.toggleFavorites(song);
    }

    /**
     * Checks if a song is a favorite.
     *
     * @param song the song
     * @return the boolean
     */
    public boolean isFavorite(Song song) {
        return playerController.isFavorite(song);
    }

    /**
     * Get all the playlists
     *
     * @return the favorites list
     */
    public List<Library> getPlaylists() {
        return playerController.getPlaylists();
    }

    /**
     * Adds a song to the current playlist and refreshes the UI.
     *
     * @param song The song to be added.
     */
    public void addSongToPlaylist(Song song) {
        playerController.addSongToPlaylist(song, library);
        refreshUI();
    }

    /**
     * Add a song to a playlist
     *
     * @param song     the song
     * @param playlist the playlist
     */
    public void addSongToPlaylist(Song song, Library playlist) {
        playerController.addSongToPlaylist(song, playlist);
        refreshUI();
    }

    /**
     * Remove a song from a playlist
     *
     * @param song     the song
     * @param playlist the playlist
     */
    public void removeSongFromPlaylist(Song song, Library playlist) {
        playerController.removeSongFromPlaylist(song, playlist);
        refreshUI();
    }

    /**
     * Remove a song from the main library
     *
     * @param song the song
     */
    public void removeSongFromPlaylist(Song song) {
        playerController.removeSongFromPlaylist(song, library);
        refreshUI();
    }

    /**
     * Refreshes the user interface of the library view.
     */
    public void refreshUI() {
        view.refreshUI();
    }

    /**
     * Checks if the currently displayed library is the main library.
     *
     * @return true if showing main library, false otherwise.
     */
    public boolean isShowingMainLibrary() {
        return playerController.isMainLibrary(library);
    }


    /**
     * Handle add song.
     */
    public void handleAddSong() {
        File audioFile = view.getAudioFile();
        if (audioFile == null) {
            alertService.showAlert("Could not read audio file", Alert.AlertType.WARNING);
            return;
        }
        try {
            PlaylistService playlistService = new PlaylistService();
            Path copiedFilePath = playlistService.addSongToMainLibrary(audioFile);
            addSong(copiedFilePath);
        } catch (IOException e) {
            alertService.showAlert("Could not add song to main library : " + audioFile, Alert.AlertType.ERROR);
        } catch (SettingsFilesException e) {
            alertService.showFatalErrorAlert("Error loading settings files", e);
        }
    }

    /**
     * Adds a song to the library.
     *
     * @param songPath The path to the song file to be added.
     */
    public void addSong(Path songPath) {
        Song song = new Song(songPath);
        library.add(song);
        view.updateListView();
    }

    /**
     * Provides artist name auto-completion based on user input.
     *
     * @param input The partial artist name entered by the user.
     * @return An Optional containing the completed artist name if found.
     */
    public Optional<String> getArtistAutoCompletion(String input) {
        return library.getArtistAutoCompletion(input);
    }

    /**
     * Provides album name auto-completion based on user input.
     *
     * @param input The partial album name entered by the user.
     * @return An Optional containing the completed album name if found.
     */
    public Optional<String> getAlbumAutoCompletion(String input) {
        return library.getAlbumAutoCompletion(input);
    }

    /**
     * Provides tag auto-completion based on user input.
     *
     * @param input The partial tag entered by the user.
     * @return An Optional containing the completed tag if found.
     */
    public Optional<String> getTagAutoCompletion(String input) {
        return library.getTagAutoCompletion(input);
    }

    /**
     * Gets the controller instance.
     *
     * @return the LibraryController instance
     */
    public LibraryController getController() {
        return this;
    }

    public Song getNextSong() {
        if (nextSong.isPresent()) {
            return nextSong.get();
        }
        if (shuffle) {
            Random random = new Random();
            int randomIndex = random.nextInt(library.size());
            nextSong = Optional.of(library.get(randomIndex));
        } else {
            if (currentIndex < library.size() - 1) {
                currentIndex++;
                nextSong = Optional.of(library.get(currentIndex));
            }
        }
        return nextSong.orElse(null);
    }

    /**
     * Checks if the library is modifiable.
     *
     * @return true if the library can be modified, false otherwise.
     */
    public boolean isModifiable(){
        return playerController.isModifiable(library);
    }

    /**
     * Checks if the library contains a given song.
     *
     * @param song The song to check for.
     * @return true if the song exists in the library, false otherwise.
     */
    public boolean containsSong(Song song) {
        return library.contains(song);
    }

    /**
     * Converts the library and suggestions into a list of songs.
     *
     * @return a list containing songs from the library and suggestions (if modifiable).
     */
    @Override
    public List<Song> toList() {
        List<Song> songs = new ArrayList<>(super.toList());
        if (isModifiable()) {
            songs.addAll(getSuggestions(""));
        }
        return songs;
    }

    /**
     * Provides song suggestions based on the current playlist.
     *
     * @return A list of suggested songs based on the query.
     */
    private List<Song> getSuggestions(String query) {
        Set<String> artists = new HashSet<>(), albums = new HashSet<>(), tags = new HashSet<>(), genres = new HashSet<>();
        collectPlaylistData(artists, albums, tags, genres);

        List<Song> candidates = new ArrayList<>(playerController.getMainLibrary().toList()); // get all songs in main library
        candidates.removeAll(library.toList()); // remove songs already in playlist

        if (artists.isEmpty() && albums.isEmpty() && genres.isEmpty()  && tags.isEmpty()) {
            return fallbackCandidates(candidates);
        }

        candidates.sort((a,b) -> score(b, artists, albums, tags, genres) - score(a, artists, albums, tags, genres));

        List<Song> suggestions = new ArrayList<>();
        for (Song s : candidates) {
            if (score(s, artists, albums, tags, genres) <= 0) break;
            suggestions.add(s);
            if (suggestions.size() >= MAX_SUGGESTIONS) break;
        }
        return suggestions;
    }

    /**
     * Collects data from the currentƒ playlist
     */
    private void collectPlaylistData(Set<String> artists, Set<String> albums, Set<String> tags, Set<String> genres) {
        for (Song s : library.toList()) {
            if (DEFAULT_ARTIST.equals(s.getArtist()) && DEFAULT_ALBUM.equals(s.getAlbum()) && DEFAULT_GENRE.equals(s.getGenre())) continue;

            String art = s.getArtist(), alb = s.getAlbum(), g = s.getGenre();

            if (art != null && !art.isBlank() && !art.equals(DEFAULT_ARTIST))
                artists.add(art);
            if (alb != null && !alb.isBlank() && !alb.equals(DEFAULT_ALBUM))
                albums.add(alb);
            if (g != null && !g.isBlank() && !g.equals(DEFAULT_GENRE))
                genres.add(g);
            for (String t : s.getUserTags()) if (t != null && !t.isBlank()) tags.add(t);

        }
    }
    /**
     * Provides a fallback list of songs if the playlist has no data.
     */
    private List<Song> fallbackCandidates(List<Song> candidates) {
        List<Song> first = new ArrayList<>();
        for (int i = 0; i < candidates.size() && i < MAX_SUGGESTIONS; i++) {
            first.add(candidates.get(i));
        }
        return first;
    }

    /**
     * Scores a song based on its attributes and the user's library.
     * @return The score of the song based on its attributes and the user's library.
     */
    private int score(Song s, Set<String> artists, Set<String> albums, Set<String> tags, Set<String> genres) {
        int sc = 0;
        String art = s.getArtist(), alb = s.getAlbum(), g = s.getGenre();

        if (art != null && !art.equals(DEFAULT_ARTIST) && artists.contains(art)) sc++;
        if (alb != null && !alb.equals(DEFAULT_ALBUM)  && albums.contains(alb)) sc++;
        if (g   != null && !g.equals(DEFAULT_GENRE)  && genres.contains(g)) sc++;

        for (String t : s.getUserTags()) if (tags.contains(t)) sc++;
        return sc;
    }

}
