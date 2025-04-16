package musicApp.views.songs;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import musicApp.models.Song;
import musicApp.views.View;

import java.util.Objects;

/**
 * The type Song cell view.
 * This class is responsible for displaying a song in the library.
 * It contains the play button, like button, and other song information.
 */
public class SongCellView extends View {
    private SongCellViewListener listener;

    @FXML
    private Button playButton, likeButton;
    @FXML
    private Label titleLabel, artistLabel, genreLabel, durationLabel;
    @FXML
    private ImageView coverImage;
    @FXML
    private ImageView editButton;

    private static final String PLAY_ICON = "/images/play2.png";
    private static final String PAUSE_ICON = "/images/pause.png";
    private static final String EDIT_ICON = "/images/edit.png";

    public SongCellView() {
    }
    
    public interface SongCellViewListener {        
        void toggleFavorites();
        StringProperty getCurrentlyLoadedSongStringProperty();
        BooleanProperty isPlayingProperty();
        void showContextMenu(double x, double y);
        boolean isLoaded();
        boolean isPlaying();
        void handlePause();
        void handleUnpause();
        void handlePlay();
        boolean isFavorite();
    }

    /**
     * Sets listener.
     *
     * @param listener the listener
     */
    public void setListener(SongCellViewListener listener) {
        this.listener = listener;
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
        ImageView playIcon = new ImageView(Objects.requireNonNull(getClass().getResource(PLAY_ICON)).toExternalForm());
        ImageView editIcon = new ImageView(Objects.requireNonNull(getClass().getResource(EDIT_ICON)).toExternalForm());

        playButton.setGraphic(playIcon);
        likeButton.setOnAction(event -> {
            listener.toggleFavorites();
        });
        editButton.setImage(editIcon.getImage());
        editButton.setPickOnBounds(true);


        listener.getCurrentlyLoadedSongStringProperty().addListener((_, _, _) -> {
            updatePlayButtonIcon();
        });
        listener.isPlayingProperty().addListener((_, _, _) -> {
            updatePlayButtonIcon();
        });
    }

    /**
     * Setup the context menu for the song cell.
     */
    private void setupContextMenu() {
        // Show context menu on click
        editButton.setOnMouseClicked(e -> {
            listener.showContextMenu(e.getScreenX(), e.getScreenY());
        });
    }

    /**
     * Update the play button icon.
     */
    private void updatePlayButtonIcon() {
        getRoot().getStyleClass().remove("song-playing");
        ImageView icon;
        if (listener.isLoaded()) {
            getRoot().getStyleClass().add("song-playing");
            if (listener.isPlaying()) {
                icon = new ImageView(Objects.requireNonNull(getClass().getResource(PAUSE_ICON)).toExternalForm());
                playButton.setOnAction(_ -> listener.handlePause());
            } else {
                icon = new ImageView(Objects.requireNonNull(getClass().getResource(PLAY_ICON)).toExternalForm());
                playButton.setOnAction(_ -> listener.handleUnpause());
            }
        } else {
            icon = new ImageView(Objects.requireNonNull(getClass().getResource(PLAY_ICON)).toExternalForm());
            playButton.setOnAction(_ -> listener.handlePlay());
        }
        icon.setFitWidth(20);
        icon.setFitHeight(20);
        playButton.setGraphic(icon);
    }

    /**
     * Update the like button.
     */
    private void updateLikeButton() {
        if (!listener.isFavorite()) {
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
        playButton.setOnAction(_ -> listener.handlePlay());
    }

}
