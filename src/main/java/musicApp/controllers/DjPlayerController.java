package musicApp.controllers;
import java.io.File;

import ddf.minim.*;
import javafx.stage.Stage;
import musicApp.views.DjPlayerView;
import musicApp.utils.MinimContextImpl;

import java.io.ByteArrayInputStream;

//
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.io.PipeDecoder;
import be.tarsos.dsp.io.PipedAudioStream;
import be.tarsos.dsp.io.TarsosDSPAudioFloatConverter;
import be.tarsos.dsp.io.TarsosDSPAudioInputStream;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
//

import java.io.IOException;


public class DjPlayerController extends ViewController<DjPlayerView, DjPlayerController> {
    
    //private final Minim minim;
    //private final AudioPlayer audioPlayer;
    private final File song;
    private final Stage stage = new Stage();

    // const
    private final int BUFFER_SIZE = 2000000000; // 20MB


    public DjPlayerController(File fd) {
        
        super(new DjPlayerView());
        //MinimContextImpl fileSystem = new MinimContextImpl();
        //this.minim = new Minim(fileSystem);

        this.song = fd;

        try {
            System.out.println("SONG: " + fd);


            AudioInputStream stream = AudioSystem.getAudioInputStream(fd);
            AudioFormat format = stream.getFormat();
            System.out.println("Format audio: " + format);
            //TarsosDSPAudioInputStream audioStream = new JVMAudioInputStream(stream);

            AudioDispatcher dispatcher = AudioDispatcherFactory.fromFile(fd, 1024, 0);
        } catch (IOException e) {
            System.err.println("An error occurred while file: " + e.getMessage());
        } catch (UnsupportedAudioFileException e) {
            System.err.println("An error occurred while file: " + e.getMessage());
        }

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
