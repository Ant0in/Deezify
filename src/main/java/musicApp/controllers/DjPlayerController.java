

package musicApp.controllers;
import java.io.File;

import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import javafx.stage.Stage;
import musicApp.views.DjPlayerView;



public class DjPlayerController extends ViewController<DjPlayerView, DjPlayerController> {
    
    private final Minim minim;
    private final AudioPlayer audioPlayer;
    private final File song;
    private final Stage stage = new Stage();

    // const
    private final int BUFFER_SIZE = 2000000000; // 20MB


    public DjPlayerController(File fd) {
        
        super(new DjPlayerView());
        this.minim = new Minim(this);
        this.song = fd;

        this.audioPlayer = this.minim.loadFile(song.getAbsolutePath(), this.BUFFER_SIZE);

        initView("/fxml/DjPlayer.fxml");
        stage.setScene(view.getScene());
        stage.setTitle("DJ Player");
        stage.setResizable(false);
        stage.show();

    }

    public void play() {
        if (!audioPlayer.isPlaying()) {
            audioPlayer.play();
        }
    }

    public void pause() {
        if (audioPlayer.isPlaying()) {
            audioPlayer.pause();
        }
    }

    public void stop() {
        if (audioPlayer.isPlaying()) {
            audioPlayer.close();
        }
    }

    public void close() {
        if (audioPlayer.isPlaying()) {
            audioPlayer.close();
        }
        minim.stop();
        stage.close();
    }

    public void start() {
        // ...
    }

}
