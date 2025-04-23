package musicApp.views.songs;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import musicApp.models.Song;
import musicApp.services.ViewService;
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

    /**
     * Interface defining listener methods for handling user interaction on the SongCellView.
     */
    public interface SongCellViewListener {
        void toggleFavorites();

        void handlePause();

        void handleUnpause();

        void handlePlay();

        void handleLoadedSongChange(Runnable callback);

        void handlePlayingStatusChange(Runnable callback);

        void showContextMenu(double x, double y);

        boolean isLoaded();

        boolean isPlaying();

        boolean isFavorite();

        Image getSongCoverImage();

        String getSongTitle();

        String getSongArtist();

        String getSongGenre();

        Duration getSongDuration();
    }


    /**
     * Sets listener.
     *
     * @param newListener the listener
     */
    public void setListener(SongCellViewListener newListener) {
        listener = newListener;
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
        ViewService viewService = new ViewService();
        ImageView playIcon = viewService.createIcon(PLAY_ICON);
        ImageView editIcon = viewService.createIcon(EDIT_ICON);

        playButton.setGraphic(playIcon);
        likeButton.setOnAction(_ -> listener.toggleFavorites());
        editButton.setImage(editIcon.getImage());
        editButton.setPickOnBounds(true);


        listener.handleLoadedSongChange(this::updatePlayButtonIcon);
        listener.handlePlayingStatusChange(this::updatePlayButtonIcon);
    }

    /**
     * Setup the context menu for the song cell.
     */
    private void setupContextMenu() {
        // Show context menu on click
        editButton.setOnMouseClicked(e -> listener.showContextMenu(e.getScreenX(), e.getScreenY()));
    }

    /**
     * Update the play button icon.
     */
    private void updatePlayButtonIcon() {
        getRoot().getStyleClass().remove("song-playing");
        ImageView icon;
        ViewService viewService = new ViewService();
        if (listener.isLoaded()) {
            getRoot().getStyleClass().add("song-playing");
            if (listener.isPlaying()) {
                icon = viewService.createIcon(PAUSE_ICON);
                playButton.setOnAction(_ -> listener.handlePause());
            } else {
                icon = viewService.createIcon(PLAY_ICON);
                playButton.setOnAction(_ -> listener.handleUnpause());
            }
        } else {
            icon = viewService.createIcon(PLAY_ICON);
            playButton.setOnAction(_ -> listener.handlePlay());
        }
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

    private String getDurationLabelString() {
        Duration songDuration = listener.getSongDuration();
        return String.format("%d:%02d", (int) songDuration.toMinutes(), (int) songDuration.toSeconds() % 60);
    }

    /**
     * Update the view with the song's information.
     *
     */
    public void update() {
        coverImage.setImage(listener.getSongCoverImage());
        titleLabel.setText(listener.getSongTitle());
        titleLabel.setStyle("-fx-text-fill: rgb(255, 255, 255);");
        artistLabel.setText(listener.getSongArtist());
        genreLabel.setText(listener.getSongGenre());
        genreLabel.setStyle("-fx-text-fill: rgb(255, 255, 255);");
        durationLabel.setText(getDurationLabelString());
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
