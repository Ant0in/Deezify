package musicApp.views;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import musicApp.controllers.SongController;
import musicApp.models.Song;

public class SongView  extends  View<SongView,SongController>{

    @FXML
    private Button playButton;
    @FXML
    private Label songName, songArtist, titleLabel, artistLabel, durationLabel;
    @FXML
    private ImageView coverImage;


    public SongView(){
    }

    @Override
    public void init() {
        initComponents();
        setButtonActions();
    }


    private void initComponents() {
        ImageView playIcon = new ImageView(getClass().getResource("/images/play2.png").toExternalForm());
        playIcon.setFitWidth(20);
        playIcon.setFitHeight(20);
        playButton.setGraphic(playIcon);

        viewController.getCurrentlyLoadedSongStringProperty().addListener((obs, oldTitle, newTitle) -> {
            updatePlayButtonIcon();
        });
        viewController.isPlayingProperty().addListener((obs, oldValue, newValue) -> {
            updatePlayButtonIcon();
        });
    }

    private void updatePlayButtonIcon() {
        getRoot().getStyleClass().remove("song-playing");
        ImageView icon;
        if (viewController.isLoaded()) {
            getRoot().getStyleClass().add("song-playing");
            if (viewController.isPlaying()) {
                icon = new ImageView(getClass().getResource("/images/pause.png").toExternalForm());
                playButton.setOnAction(_ -> viewController.handlePause());
            } else {
                icon = new ImageView(getClass().getResource("/images/play2.png").toExternalForm());
                playButton.setOnAction(_ -> viewController.handleUnpause());
            }
        } else {
            icon = new ImageView(getClass().getResource("/images/play2.png").toExternalForm());
            playButton.setOnAction(_ -> viewController.handlePlay());
        }
        icon.setFitWidth(20);
        icon.setFitHeight(20);
        playButton.setGraphic(icon);
    }

    public void update(Song song){
        coverImage.setImage(song.getCoverImage());
        titleLabel.setText(song.getTitle());
        titleLabel.setStyle("-fx-text-fill: rgb(255, 255, 255);");
        artistLabel.setText(song.getArtist());
        durationLabel.setText(String.format("%d:%02d", (int) song.getDuration().toMinutes(), (int) song.getDuration().toSeconds() % 60));
        durationLabel.setStyle("-fx-text-fill: rgb(255, 255, 255); -fx-opacity: 50%;");
        updatePlayButtonIcon();
    }

    private void setButtonActions() {
        playButton.setOnAction(_ -> this.viewController.handlePlay());
    }

}
