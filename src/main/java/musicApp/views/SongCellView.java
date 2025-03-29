package musicApp.views;

import javafx.scene.control.*;
import musicApp.models.Song;
import musicApp.utils.LanguageManager;
import musicApp.controllers.SongCellController;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.scene.image.ImageView;

import java.io.File;
import java.util.Objects;

public class SongCellView extends  View<SongCellView, SongCellController>{

    @FXML
    private Button playButton, likeButton;
    @FXML
    private Label titleLabel, artistLabel, genreLabel, durationLabel;
    @FXML
    private ImageView coverImage;
    @FXML
    private ImageView editButton;

    private ContextMenu contextMenu;
    private MenuItem menuItem;
    private MenuItem addToPlaylist;



    public SongCellView() {
    }

    @Override
    public void init() {
        initComponents();
        setButtonActions();
        setupContextMenu();
    }


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

    private void setupContextMenu() {

        // create the context menu (opened on right click)
        contextMenu = new ContextMenu();

        // Edit metadata menu item
        MenuItem editMetadataItem = new MenuItem(LanguageManager.getInstance().get("button.edit_metadata"));
        editMetadataItem.setOnAction(e -> {
            viewController.openMetadataEditor();
        });

        // Create a submenu for playlists
        Menu addToPlaylistMenu = new Menu(LanguageManager.getInstance().get("button.add_to_playlist"));

        MenuItem removeFromPlaylistMenu;

        if (viewController.isShowingMainLibrary()){
            removeFromPlaylistMenu = new Menu(LanguageManager.getInstance().get("button.remove_from_playlist"));
        } else {
            removeFromPlaylistMenu = new MenuItem(LanguageManager.getInstance().get("button.remove_from_playlist"));
        }

        // Update playlists menu items whenever the context menu is shown
        contextMenu.setOnShowing(e -> {
            // Clear previous items
            addToPlaylistMenu.getItems().clear();
            if(removeFromPlaylistMenu instanceof Menu) {
                ((Menu) removeFromPlaylistMenu).getItems().clear();
            }

            // Get all playlists from controller
            viewController.getPlaylists().stream().skip(2).forEach(playlist -> {
                // Skip favorites playlist which typically has a special name
                MenuItem playlistItem = new MenuItem(playlist.getName());
                playlistItem.setOnAction(event -> {
                    viewController.addSongToPlaylist(playlist);
                });
                addToPlaylistMenu.getItems().add(playlistItem);

                if (viewController.isShowingMainLibrary() && viewController.containsSong(playlist)) {
                    MenuItem removeItem = new MenuItem(playlist.getName());
                    removeItem.setOnAction(event -> {
                        viewController.removeSongFromPlaylist(playlist);
                    });
                    if (removeFromPlaylistMenu instanceof Menu) {
                        ((Menu) removeFromPlaylistMenu).getItems().add(removeItem);
                    }
                }
            });

            if (!viewController.isShowingMainLibrary()){
                removeFromPlaylistMenu.setOnAction(event -> {
                    viewController.removeSongFromPlaylist();
                });
            }

            // Add "Create new playlist" option at the bottom
            if (!addToPlaylistMenu.getItems().isEmpty()) {
                addToPlaylistMenu.getItems().add(new javafx.scene.control.SeparatorMenuItem());
            }
            MenuItem newPlaylistItem = new MenuItem(LanguageManager.getInstance().get("create_playlist.create"));
            newPlaylistItem.setOnAction(event -> {
                viewController.createNewPlaylistWithSong();
            });
            addToPlaylistMenu.getItems().add(newPlaylistItem);
        });

        // Add items to context menu
        contextMenu.getItems().addAll(editMetadataItem, addToPlaylistMenu, removeFromPlaylistMenu);

        // Show context menu on click
        editButton.setOnMouseClicked(e -> {
            contextMenu.show(editButton, e.getScreenX(), e.getScreenY());
        });
    }

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

    private void updateLikeButton() {
        if (!viewController.isFavorite()) {
            likeButton.setText("❤");
        } else {
            likeButton.setText("X");
        }
    }

    public void update(Song song){
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
        playButton.setOnAction(_ -> this.viewController.handlePlay());
    }

}
