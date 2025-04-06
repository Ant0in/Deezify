

package musicApp.controllers;
import java.io.File;

import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import musicApp.views.DjPlayerView;



public class DjPlayerController extends ViewController<DjPlayerView, DjPlayerController> {
    
    private final Minim minim;
    private final AudioPlayer audioPlayer;
    private final File song;

    // const
    private final int BUFFER_SIZE = 1024;


    public DjPlayerController(File fd) {
        super(new DjPlayerView());
        this.minim = new Minim(this);
        this.song = fd;
        this.audioPlayer = this.minim.loadFile(song.getAbsolutePath(), this.BUFFER_SIZE);
        initView("/fxml/DjPlayer.fxml");
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
        if (audioPlayer != null) {
            audioPlayer.close();
        }
        if (minim != null) {
            minim.stop();
        }
    }

    public void start() {
        // ...
    }

}
