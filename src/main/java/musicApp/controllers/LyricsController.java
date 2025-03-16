package musicApp.controllers;

import javafx.beans.property.StringProperty;
import musicApp.models.Song;
import musicApp.utils.JsonOpener;
import musicApp.views.LyricsView;

import java.util.ArrayList;
import java.util.List;

public class LyricsController extends ViewController <LyricsView, LyricsController>{
    private final PlayerController playerController;

    public LyricsController(PlayerController playerController) {
        super(new LyricsView());
        this.playerController = playerController;
        initView("/fxml/Lyrics.fxml");
    }


    public StringProperty getCurrentlyLoadedSongStringProperty(){
        return playerController.getCurrentlyLoadedSongStringProperty();
    }

    public List<String> getCurrentSongLyrics(){
        Song song = playerController.getCurrentlyLoadedSong();
        return JsonOpener.getSongLyrics(song.getFilePath());
    }
}