package musicApp.views;

import musicApp.controllers.PlayerController;
import musicApp.models.Song;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.util.Objects;

/**
 * SongCell
 * Class that represents a cell in the song list.
 */
public class SongCell extends ListCell<Song> {
    private final ImageView imageView = new ImageView();
    private final Label titleLabel = new Label();
    private final Label artistLabel = new Label();
    private final Label durationLabel = new Label();
    private final HBox hbox = new HBox(10); // Spacing between image and text
    private final Button playButton = new Button("");

    /**
     * Constructor
     */
    public SongCell(PlayerController playerController) {
        imageView.setFitWidth(25);
        imageView.setFitHeight(25);
        imageView.setPreserveRatio(true);

        artistLabel.setStyle("-fx-text-fill: rgb(255, 255, 255); -fx-opacity: 50%; -fx-start-margin: 10px;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        hbox.getChildren().addAll(playButton, imageView, titleLabel, artistLabel, spacer, durationLabel);
        playButton.setOnMouseClicked(event -> {
            Song song = getItem();
            if (song != null) {
                int index = playerController.getLibrary().toList().indexOf(song);
                playerController.playFromLibrary(index);
            }
        });

        ImageView playIcon = new ImageView(getClass().getResource("/images/play2.png").toExternalForm());
        playIcon.setFitWidth(20);
        playIcon.setFitHeight(20);
        playButton.setGraphic(playIcon);
        playButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
    }

    /**
     * Update the cell with the song information.
     *
     * @param song  The song to display.
     * @param empty Whether the cell is empty.
     */
    @Override
    protected void updateItem(Song song, boolean empty) {
        super.updateItem(song, empty);
        if (empty || song == null) {
            setGraphic(null);
        } else {
            imageView.setImage(song.getCoverImage());
            titleLabel.setText(song.getSongName());
            titleLabel.setStyle("-fx-text-fill: rgb(255, 255, 255);");
            artistLabel.setText(song.getArtistName());
            durationLabel.setText(String.format("%d:%02d", (int) song.getDuration().toMinutes(), (int) song.getDuration().toSeconds() % 60));
            durationLabel.setStyle("-fx-text-fill: rgb(255, 255, 255); -fx-opacity: 50%;");
            setGraphic(hbox);
        }
    }
}