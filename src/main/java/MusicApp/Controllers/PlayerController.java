package MusicApp.Controllers;

import MusicApp.Models.AudioPlayer;
import MusicApp.Models.Song;
import javafx.beans.property.StringProperty;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;


public class PlayerController {

    public interface SongChangeListener {
        void onSongChanged();
    }
    
    private SongChangeListener songChangeListener;

    public void setSongChangeListener(SongChangeListener listener) {
        this.songChangeListener = listener;
    }

    
    private final AudioPlayer audioPlayer;
    private final List<Song> library;
    private final List<Song> queue;
    private int currentIndex;

    public PlayerController() {
        this.audioPlayer = new AudioPlayer();
        this.library = new ArrayList<>();
        this.queue = new ArrayList<>();
        loadLibrary();
    }

    private void loadLibrary() {
        library.add(new Song("Song1", "Artist1", "Pop", "src/main/resources/songs/song1.mp3", Duration.minutes(3)));
        library.add(new Song("Song2", "Artist2", "Rock", "src/main/resources/songs/song2.mp3", Duration.minutes(4)));
        library.add(new Song("Song3", "Artist3", "Jazz", "src/main/resources/songs/song3.mp3", Duration.minutes(5)));
    }

    public void skip() {
        if (queue.isEmpty()) {
            if (currentIndex < library.size() - 1) {
                currentIndex++;
                playCurrent();
            }
        } else {
            playFromQueue(0);
            queue.removeFirst();
        }
    }

    public void prec() {
        if (currentIndex > 0) {
            currentIndex--;
            playCurrent();
        }
    }

    public void pause() {
        audioPlayer.pause();
    }

    public void unpause() {
        audioPlayer.play();
    }

    public void goTo(int index) {
        if (index >= 0 && index < library.size()) {
            currentIndex = index;
            playCurrent();
        }
    }

    private void playCurrent() {
        if (currentIndex >= 0 && currentIndex < library.size()) {
            Song song = library.get(currentIndex);
            audioPlayer.loadSong(song);
            audioPlayer.play();
            System.out.println("Playing: " + song.getSongName());
    
            if (songChangeListener != null) {
                songChangeListener.onSongChanged();
            }
        }
    }
    

    public AudioPlayer getAudioPlayer() { return audioPlayer; }


    public void setVolume(double volume) {
        getAudioPlayer().setVolume(volume);
    }


    public List<String> getLibrary() {
        List<String> songNames = new ArrayList<>();
        for (Song song : library) {
            songNames.add(song.getSongName());
        }
        return songNames;
    }

    public List<String> getQueue() {
        List<String> songNames = new ArrayList<>();
        for (Song song : queue) {
            songNames.add(song.getSongName());
        }
        return songNames;
    }

    public void addToQueue(int index) {
        if (index >= 0 && index < library.size()) {
            queue.add(library.get(index));
        }
    }

    public void removeFromQueue(int index) {
        if (index >= 0 && index < queue.size()) {
            queue.remove(index);
        }
    }

    public void clearQueue() {
        queue.clear();
    }
    
    
    public void playFromLibrary(int index) {
        if (index >= 0 && index < library.size()) {
            currentIndex = index;
            playCurrent();
        }
    }
    
    public void playFromQueue(int index) {
        if (index >= 0 && index < queue.size()) {
            Song song = queue.get(index);
            audioPlayer.loadSong(song);
            audioPlayer.play();
            System.out.println("Playing from queue: " + song.getSongName());
        }
    }
    
    public Duration getCurrentTime() {
        return getAudioPlayer().getCurrentTime();
    }
    
    public Duration getTotalDuration() {
        return getAudioPlayer().getTotalDuration();
    }
    
    public javafx.beans.property.DoubleProperty progressProperty() {
        return getAudioPlayer().progressProperty();
    }

    public Boolean isPlaying() {
        return getAudioPlayer().isPlaying();
    }

    public StringProperty currentSongProperty() {
        return getAudioPlayer().currentSongProperty();
    }
    

}

