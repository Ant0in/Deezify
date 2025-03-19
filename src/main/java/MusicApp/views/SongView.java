package MusicApp.views;

import MusicApp.controllers.SongController;
import MusicApp.models.Song;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class SongView  extends  View<SongView,SongController>{

    @FXML
    private Button playButton;
    @FXML
    private Label titleLabel, artistLabel, genreLabel, durationLabel;
    @FXML
    private ImageView coverImage;

    private ContextMenu contextMenu;
    private MenuItem menuItem;



    public SongView(){
    }

    @Override
    public void init() {
        initComponents();
        setButtonActions();
        setupContextMenu();
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

    private void setupContextMenu() {
        
        // create the context menu (opened on right click)

        contextMenu = new ContextMenu();
        menuItem = new MenuItem("Edit Metadata");
        menuItem.setOnAction(e -> {
            openEditPopup();
        });
        contextMenu.getItems().add(menuItem);
        coverImage.setOnContextMenuRequested(e -> {
            contextMenu.show(coverImage, e.getScreenX(), e.getScreenY());
        });

    }

    private void openEditPopup() {
        
        // Edit popup code here
        Stage stage = new Stage();
        stage.setTitle("Edit Metadata");

        // create the edit fields
        TextField titleField = new TextField();
        TextField artistField = new TextField();
        TextField genreField = new TextField();

        titleField.setText(titleLabel.getText());
        artistField.setText(artistLabel.getText());
        genreField.setText(genreLabel.getText());
        
        // few buttons
        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        saveButton.setOnAction(e -> {
            // !! update the song metadata here
            stage.close();
        });

        cancelButton.setOnAction(e -> {
            stage.close();
        });

        stage.close();

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
