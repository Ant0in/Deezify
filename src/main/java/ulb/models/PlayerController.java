package ulb.models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PlayerController {

    private String filePath;
    private ArrayList <String> library;
    private MediaPlayer mediaPlayer;
    private DoubleProperty progress = new SimpleDoubleProperty(0.0);

    private Timeline timeline;

    public PlayerController(){
        initialize();
    }

    private void initialize() {
        initializeLibrary();
        initializeFilePath();
        initializeMediaPlayer();
    }

    private void initializeLibrary() {
        library = new ArrayList<>();
        library.add("src/main/resources/songs/song1.mp3");
        library.add("src/main/resources/songs/song2.mp3");
        library.add("src/main/resources/songs/song3.mp3");
    }
    
    private void initializeFilePath() {
        File file = new File("src/main/resources/songs/song1.mp3");
        if (!file.exists()) {
            System.err.println("Error: File does not exist at " + file.getAbsolutePath());
            return;
        }
        this.filePath = file.toURI().toString();
    }

    private void initializeMediaPlayer() {
        if (filePath == null) {
            System.err.println("Error: File path is not initialized.");
            return;
        }

        try {
            Media media = new Media(filePath);
            this.mediaPlayer = new MediaPlayer(media);

            this.mediaPlayer.setOnReady(() -> System.out.println("Media loaded: " + filePath));
            this.mediaPlayer.setOnError(() -> System.err.println("Error playing media: " + media.getSource()));

            mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
                progress.set(newTime.toSeconds() / mediaPlayer.getTotalDuration().toSeconds());
            });

        } catch (Exception e) {
            System.err.println("Failed to load media: " + e.getMessage());
        }
    }

    private void updateMediaPlayer(){
        initializeMediaPlayer();
    }

    private void setFilePath(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println("Error: File does not exist at " + file.getAbsolutePath());
            return;
        }
        this.filePath = file.toURI().toString();
        updateMediaPlayer();
    }

    public void play (String songPath) {
        this.mediaPlayer.stop();
        setFilePath(songPath);        
        this.mediaPlayer.play();
        System.out.println("playing...");
    }
    public void stop() {
        System.out.println("stopped.");
        this.mediaPlayer.pause();
    }
    public void next(){
        System.out.println("playing next song...");
    }

    public void previous(){
        System.out.println("playing previous song...");
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
    /*private void initTimer() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> updateProgress()));
        timeline.setCycleCount(Timeline.INDEFINITE); // Runs indefinitely
        timeline.play(); // Starts the timer
    }

    private void updateProgress() {
        // Update UI components, e.g., progress bar
        songProgressBar.setProgress(playerController.getCurrentProgress());
    }*/

}
