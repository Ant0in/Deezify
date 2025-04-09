package musicApp.controllers;

import musicApp.controllers.PlayerController;
import musicApp.controllers.settings.EqualizerController;
import musicApp.models.Song;

import java.io.File;

import ddf.minim.*;
import javafx.stage.Stage;
import musicApp.views.DjPlayerView;
import musicApp.utils.MinimContextImpl;
import javafx.scene.media.*;
import java.nio.file.Paths;

import java.io.ByteArrayInputStream;

import java.io.IOException;


public class DjPlayerController extends ViewController<DjPlayerView, DjPlayerController> {
    
    //private final Minim minim;
    //private final AudioPlayer audioPlayer;
    private final Song song;
    private final Stage stage = new Stage();
    private final PlayerController player;
    private final EqualizerController equalizerController;


    public DjPlayerController(Song song, PlayerController player) {
        
        super(new DjPlayerView());
        //MinimContextImpl fileSystem = new MinimContextImpl();
        //this.minim = new Minim(fileSystem);

        this.song = song;
        this.player = player;
        this.equalizerController = player.getEqualizerController();
        player.pause();
        player.playSong(song);


        //mediaPlayer.setRate(2);


        //this.audioPlayer = minim.loadFile(song.getAbsolutePath());

        initView("/fxml/DjPlayer.fxml");
        stage.setScene(view.getScene());
        stage.setTitle("DJ Player");
        stage.setResizable(false);
        stage.show();

    }

    public void start() {
        // ...
    }

}
