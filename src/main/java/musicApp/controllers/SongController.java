
package musicApp.controllers;


import javafx.beans.property.StringProperty;
import musicApp.models.Song;
import musicApp.views.SongView;

public class SongController extends ViewController<SongView, SongController>{

    private final MainLibraryController mainLibraryController;
    private Song song;

    public SongController(MainLibraryController mainLibraryController) {
        super(new SongView());
        this.mainLibraryController = mainLibraryController;
        initView("/fxml/Song.fxml");
    }

    public void update(Song newSong){
        if (!newSong.equals(getSong())) {
            this.song = newSong;
            this.view.update(song);
        }
    }

    public Song getSong(){
        return song;
    }

    public boolean isPlaying(){
        Song playingSong = mainLibraryController.getCurrentlyPlayingSong();
        return playingSong != null && playingSong.equals(song);
    }

    public void handlePlay(){
        if (song == null) {
            view.displayError("No song to play");
            return;
        }
        mainLibraryController.playSong(song);
    }

    public StringProperty getCurrentlyPlayingSongStringProperty(){
        return mainLibraryController.getCurrentlyPlayingSongStringProperty();
    }
}
