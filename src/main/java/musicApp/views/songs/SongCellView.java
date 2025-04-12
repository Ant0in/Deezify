package musicApp.views.songs;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import musicApp.controllers.songs.SongCellController;
import musicApp.controllers.songs.SongContextMenuController;
import musicApp.models.Song;
import musicApp.views.View;

import java.util.Objects;

/**
 * The type Song cell view.
 * This class is responsible for displaying a song in the library.
 * It contains the play button, like button, and other song information.
 */
public class SongCellView extends View<SongCellView, SongCellController> {

    @FXML
    private Button playButton, likeButton;
    @FXML
    private Label titleLabel, artistLabel, genreLabel, durationLabel;
    @FXML
    private ImageView coverImage;
    @FXML
    private ImageView editButton;


    public SongCellView() {
    }

    @Override
    public void init() {
        initComponents();
        setButtonActions();
        setupContextMenu();
    }

    /**
     * Initialize the components.
     */
    private void initComponents() {
        ImageView playIcon = new ImageView(Objects.requireNonNull(getClass().getResource("/images/play2.png")).toExternalForm());
        ImageView editIcon = new ImageView(Objects.requireNonNull(getClass().getResource("/images/edit.png")).toExternalForm());

        playButton.setGraphic(playIcon);
        likeButton.setOnAction(event -> {
            viewController.toggleFavorites();
        });
        editButton.setImage(editIcon.getImage());
        editButton.setPickOnBounds(true);


        viewController.getCurrentlyLoadedSongStringProperty().addListener((_, _, _) -> {
            updatePlayButtonIcon();
        });
        viewController.isPlayingProperty().addListener((_, _, _) -> {
            updatePlayButtonIcon();
        });
    }

    /**
     * Setup the context menu for the song cell.
     */
    private void setupContextMenu() {
        // Show context menu on click
        editButton.setOnMouseClicked(e -> {
            viewController.showContextMenu(e.getScreenX(), e.getScreenY());
        });
    }

    /**
     * Update the play button icon.
     */
    private void updatePlayButtonIcon() {
        getRoot().getStyleClass().remove("song-playing");
        ImageView icon;
        if (viewController.isLoaded()) {
            getRoot().getStyleClass().add("song-playing");
            if (viewController.isPlaying()) {
                icon = new ImageView(Objects.requireNonNull(getClass().getResource("/images/pause.png")).toExternalForm());
                playButton.setOnAction(_ -> viewController.handlePause());
            } else {
                icon = new ImageView(Objects.requireNonNull(getClass().getResource("/images/play2.png")).toExternalForm());
                playButton.setOnAction(_ -> viewController.handleUnpause());
            }
        } else {
            icon = new ImageView(Objects.requireNonNull(getClass().getResource("/images/play2.png")).toExternalForm());
            playButton.setOnAction(_ -> viewController.handlePlay());
        }
        icon.setFitWidth(20);
        icon.setFitHeight(20);
        playButton.setGraphic(icon);
    }

    /**
     * Update the like button.
     */
    private void updateLikeButton() {
        if (!viewController.isFavorite()) {
            likeButton.setText("❤");
        } else {
            likeButton.setText("X");
        }
    }

    /**
     * Update the view with the song's information.
     *
     * @param song to update the view with
     */
    public void update(Song song) {
        coverImage.setImage(song.getCoverImage());
        titleLabel.setText(song.getTitle());
        titleLabel.setStyle("-fx-text-fill: rgb(255, 255, 255);");
        artistLabel.setText(song.getArtist());
        genreLabel.setText(song.getGenre());
        genreLabel.setStyle("-fx-text-fill: rgb(255, 255, 255);");
        durationLabel.setText(String.format("%d:%02d", (int) song.getDuration().toMinutes(), (int) song.getDuration().toSeconds() % 60));
        durationLabel.setStyle("-fx-text-fill: rgb(255, 255, 255); -fx-opacity: 50%;");
        updatePlayButtonIcon();
        updateLikeButton();
    }

    /**
     * Set the actions for the buttons.
     */
    private void setButtonActions() {
        playButton.setOnAction(_ -> viewController.handlePlay());
    }

}
