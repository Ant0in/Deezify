package musicApp.views.songs;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import musicApp.services.ViewService;
import musicApp.views.View;

/**
 * The type Song cell view.
 * This class is responsible for displaying a song in the library.
 * It contains the play button, like button, and other song information.
 */
public class SongCellView extends View {

    private static final String PLAY_ICON = "/images/play2.png";
    private static final String PAUSE_ICON = "/images/pause.png";
    private static final String EDIT_ICON = "/images/edit.png";
    private static final String ADD_ICON = "/images/add.png";
    private SongCellViewListener listener;
    @FXML
    private Button playButton, likeButton, addButton;
    @FXML
    private Label titleLabel, artistLabel, genreLabel, durationLabel;
    @FXML
    private ImageView coverImage;
    @FXML
    private ImageView editButton;

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
        bindLabelsWidth();
    }

    /**
     * Initialize the components.
     */
    private void initComponents() {
        addButton = new Button();
        ViewService viewService = new ViewService();
        ImageView playIcon = viewService.createIcon(PLAY_ICON);
        ImageView editIcon = viewService.createIcon(EDIT_ICON);
        ImageView addIcon = viewService.createIcon(ADD_ICON);

        playButton.setGraphic(playIcon);
        likeButton.setOnAction(_ -> listener.toggleFavorites());
        editButton.setImage(editIcon.getImage());
        editButton.setPickOnBounds(true);

        addButton.setGraphic(addIcon);
        addButton.setStyle(playButton.getStyle());
        addButton.setOnAction(e -> listener.handleAdd());

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

    private void bindLabelsWidth() {
        ReadOnlyDoubleProperty listWidth = rootPane.widthProperty();
        titleLabel.prefWidthProperty().bind(listWidth.multiply(0.15));
        artistLabel.prefWidthProperty().bind(listWidth.multiply(0.1));
        genreLabel.prefWidthProperty().bind(listWidth.multiply(0.1));
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
     */
    public void update() {
        rootPane.setOpacity(1);
        updateCommonFields();
        updatePlayButtonIcon();
        updateLikeButton();
        rootPane.getChildren().set(0, playButton);
        likeButton.setVisible(true);
        editButton.setVisible(true);
    }

    /**
     * Update the view with the song's information as a suggestion.
     */
    public void updateSuggestion() {
        rootPane.setOpacity(0.5);
        updateCommonFields();
        rootPane.getChildren().set(0, addButton);
        likeButton.setVisible(false);
        editButton.setVisible(false);
    }

    /**
     * Shared logic for updating UI elements with song info.
     */
    private void updateCommonFields() {
        coverImage.setImage(listener.getSongCoverImage());
        titleLabel.setText(listener.getSongTitle());
        titleLabel.setStyle("-fx-text-fill: rgb(255, 255, 255);");
        titleLabel.setTooltip(new Tooltip(titleLabel.getText()));
        artistLabel.setText(listener.getSongArtist());
        artistLabel.setTooltip(new Tooltip(artistLabel.getText()));
        genreLabel.setText(listener.getSongGenre());
        genreLabel.setStyle("-fx-text-fill: rgb(255, 255, 255);");
        genreLabel.setTooltip(new Tooltip(genreLabel.getText()));
        durationLabel.setText(getDurationLabelString());
        durationLabel.setStyle("-fx-text-fill: rgb(255, 255, 255); -fx-opacity: 50%;");
    }

    /**
     * Set the actions for the buttons.
     */
    private void setButtonActions() {
        playButton.setOnAction(_ -> listener.handlePlay());
    }

    /**
     * Interface defining listener methods for handling user interaction on the SongCellView.
     */
    public interface SongCellViewListener {
        void toggleFavorites();

        void handlePause();

        void handleUnpause();

        void handlePlay();

        void handleAdd();

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

}