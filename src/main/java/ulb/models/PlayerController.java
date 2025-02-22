package ulb.models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;

public class PlayerController {

    public interface SongChangeListener {
        void onSongChanged();
    }

    private SongChangeListener songChangeListener;

    public void setSongChangeListener(SongChangeListener listener) {
        this.songChangeListener = listener;
    }

    private ArrayList <String> library;
    private MediaPlayer mediaPlayer;
    private final DoubleProperty progress = new SimpleDoubleProperty(0.0);

    private int currentSongIndexLibrary;
    private int currentSongIndexQueue;

    private ArrayList<String> queue;

    boolean isPlayingFromLibrary = false;
    boolean isPlayingFromQueue = false;

    public PlayerController(){
        initialize();
    }

    private void initialize() {
        initializeLibrary();
        initializeQueue();
        updateMediaPlayer(getCurrentSongPathLibrary());
    }

    private void initializeLibrary() {
        library = new ArrayList<>();
        library.add("src/main/resources/songs/song1.mp3");
        library.add("src/main/resources/songs/song2.mp3");
        library.add("src/main/resources/songs/song3.mp3");
        library.add("src/main/resources/songs/song4.mp3");
        library.add("src/main/resources/songs/song5.mp3");
        library.add("src/main/resources/songs/song6.mp3");
        currentSongIndexLibrary=0;
    }

    private void initializeQueue() {
        queue = new ArrayList<>();
        queue.add("src/main/resources/songs/song1.mp3");
        currentSongIndexQueue=0;
    }

    private void notifyListner(){
        if (songChangeListener != null) {
            songChangeListener.onSongChanged();
        }
    }

    private String getCurrentSongPathLibrary(){
        return library.get(currentSongIndexLibrary);
    }

    private String getCurrentSongPathQueue(){
        return queue.get(currentSongIndexQueue);
    }

    private boolean checkFileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    private void updateMediaPlayer(String filePath) {
        // String filePath = getCurrentSongPathLibrary();
        if (!validateFilePath(filePath)) {
            return;
        }

        String fileUri = new File(filePath).toURI().toString();

        try {
            Media media = new Media(fileUri);
            if (mediaPlayer != null) {
                mediaPlayer.dispose();
            }
            this.mediaPlayer = new MediaPlayer(media);
            attachMediaListeners(media);
        } catch (Exception e) {
            System.err.println("Failed to load media: " + e.getMessage());
        }
    }

    private boolean validateFilePath(String filePath) {
        if (filePath == null) {
            System.err.println("Error: File path is not initialized.");
            return false;
        }
        if (!checkFileExists(filePath)) {
            System.err.println("Error: File does not exist at " + filePath);
            return false;
        }
        return true;
    }

    private void attachMediaListeners(Media media) {

        // Optionally set an onReady handler if needed.
        mediaPlayer.setOnError(() -> System.err.println("Error playing media: " + media.getSource()));

        mediaPlayer.setOnEndOfMedia(() -> {
            System.out.println("End of media");
            next();
        });

        mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            Duration total = mediaPlayer.getTotalDuration();
            if (total != null && total.toSeconds() > 0) {
                progress.set(newTime.toSeconds() / total.toSeconds());
            }
        });
    }



    public void setVolume(double volume) {
        this.mediaPlayer.setVolume(volume);
    }



    public DoubleProperty progressProperty() {
        return progress;
    }

    // Get total duration of the song
    public Duration getTotalDuration() {
        if (mediaPlayer != null) {
            return mediaPlayer.getTotalDuration();
        }
        return Duration.ZERO;
    }

    // Get current playback time
    public Duration getCurrentTime() {
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentTime();
        }
        return Duration.ZERO;
    }

    public ArrayList<String> getLibrary() {
        return library;
    }

    public ArrayList<String> getQueue() {
        return queue;
    }

    public void turnOffFlags(){
        isPlayingFromLibrary = false;
        isPlayingFromQueue = false;
    }

    public void stop() {
        System.out.println("stopped.");
        turnOffFlags();
        this.mediaPlayer.stop();
    }

    public void playFromQueue (int index){
        if(index<0 || index>=queue.size()){
            System.err.println("Error: Invalid index");
            return;
        }
        this.mediaPlayer.pause();
        currentSongIndexQueue=index;
        updateMediaPlayer(getCurrentSongPathQueue());
        this.mediaPlayer.play();
        turnOnPlayFromQueue();
        System.out.println("playing from queue...");
        notifyListner();
    }

    public void playFromLibrary (int index){
        if(index<0 || index>=library.size()){
            System.err.println("Error: Invalid index");
            return;
        }
        this.mediaPlayer.pause();
        currentSongIndexLibrary=index;
        updateMediaPlayer(getCurrentSongPathLibrary());
        this.mediaPlayer.play();
        turnOnPlayFromLibrary();
        System.out.println("playing from library...");
        notifyListner();
    }

    public void playNextFromQueue(){
        if(currentSongIndexQueue+1<queue.size()){
            playFromQueue(currentSongIndexQueue+1);
        }
    }

    public void playNextFromLibrary(){
        if(currentSongIndexLibrary+1<library.size()){
            playFromLibrary(currentSongIndexLibrary+1);
        }
    }

    private void turnOnPlayFromLibrary() {
        isPlayingFromLibrary = true;
        isPlayingFromQueue = false;
    }

    private void turnOnPlayFromQueue() {
        isPlayingFromQueue = true;
        isPlayingFromLibrary = false;
    }

    public void next(){
        if(isPlayingFromLibrary){
            if (!queue.isEmpty()) {
                playFromQueue(0);
            } else {
                playNextFromLibrary();
            }
        } else if(isPlayingFromQueue){
            playNextFromQueue();
        }
    }

    public void previous(){
        if(isPlayingFromQueue){
            if(currentSongIndexQueue-1>=0){
                playFromQueue(currentSongIndexQueue-1);
            }
        }
    }

    public void addToQueue(int index){
        if(index<0 || index>=library.size()){
            System.err.println("Error: Invalid index");
            return;
        }
        queue.add(library.get(index));
    }

    public void removeFromQueue(int index){
        if(index<0 || index>=queue.size()){
            System.err.println("Error: Invalid index");
            return;
        }
        queue.remove(index);
    }

    public void clearQueue(){
        queue.clear();
    }
}
