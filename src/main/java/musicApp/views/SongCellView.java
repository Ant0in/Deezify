package musicApp.views;

import musicApp.models.Song;
import musicApp.utils.LanguageManager;
import musicApp.controllers.SongCellController;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ContextMenu;

import java.io.File;

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



    public SongCellView() {
    }

    @Override
    public void init() {
        initComponents();
        setButtonActions();
        setupContextMenu();
    }


    private void initComponents() {
        ImageView playIcon = new ImageView(getClass().getResource("/images/play2.png").toExternalForm());
        ImageView editIcon = new ImageView(getClass().getResource("/images/edit.png").toExternalForm());

        playButton.setGraphic(playIcon);
        likeButton.setOnAction(event -> {
            viewController.toggleFavorites();
        });
        editButton.setImage(editIcon.getImage());
        editButton.setPickOnBounds(true);


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
        menuItem = new MenuItem(LanguageManager.getInstance().get("button.editmetadata"));
        menuItem.setOnAction(e -> {
            openEditPopup();
        });

        contextMenu.getItems().add(menuItem);
        editButton.setOnMouseClicked(e -> {
            contextMenu.show(editButton, e.getScreenX(), e.getScreenY());
        });

    }

    private void openEditPopup() {

        // Edit popup code here
        Stage stage = new Stage();
        stage.setTitle(LanguageManager.getInstance().get("editmetadata.title"));

        // create the edit fields
        TextField titleField = new TextField();
        TextField artistField = new TextField();
        TextField genreField = new TextField();
        final File[] selectedFile = new File[1];
        Button chooseCoverButton = new Button("Choose Cover Image");

        titleField.setText(titleLabel.getText());
        artistField.setText(artistLabel.getText());
        genreField.setText(genreLabel.getText());

        // few buttons
        Button saveButton = new Button(LanguageManager.getInstance().get("settings.save"));
        Button cancelButton = new Button(LanguageManager.getInstance().get("settings.cancel"));

        chooseCoverButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose Cover Image");

            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg")
            );

            File file = fileChooser.showOpenDialog(stage);
            if ( file != null ) {
                selectedFile[0] = file;
            }

        });

        saveButton.setOnAction(e -> {
            this.viewController.handleEditMetadata(
                titleField.getText(),
                artistField.getText(),
                genreField.getText(),
                null,
                    selectedFile[0] != null ? selectedFile[0].getAbsolutePath() : null

            );
            stage.close();
        });

        cancelButton.setOnAction(e -> {
            stage.close();
        });

        VBox popupLayout = new VBox(10,
            new Label(LanguageManager.getInstance().get("song.title")), titleField,
            new Label(LanguageManager.getInstance().get("song.artist")), artistField,
            new Label(LanguageManager.getInstance().get("song.genre")), genreField,
            chooseCoverButton,
            saveButton,
            cancelButton
        );
        Scene popupScene = new Scene(popupLayout, 300, 250);

        stage.setScene(popupScene);
        stage.show();

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

    private void setButtonActions() {
        playButton.setOnAction(_ -> this.viewController.handlePlay());
    }

}
