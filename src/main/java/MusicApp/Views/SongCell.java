package MusicApp.Views;

import MusicApp.Models.Song;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;

/**
 * SongCell
 * Class that represents a cell in the song list.
 */
public class SongCell extends ListCell<Song> {
    private final ImageView imageView = new ImageView();
    private final Label titleLabel = new Label();
    private final HBox hbox = new HBox(10); // Spacing between image and text

    /**
     * Constructor
     */
    public SongCell() {
        imageView.setFitWidth(25);  // Set width of image
        imageView.setFitHeight(25); // Set height of image
        hbox.getChildren().addAll(imageView, titleLabel);
    }

    /**
     * Update the cell with the song information.
     * @param song The song to display.
     * @param empty Whether the cell is empty.
     */
    @Override
    protected void updateItem(Song song, boolean empty) {
        super.updateItem(song, empty);

        if (empty || song == null) {
            setGraphic(null);
        } else {
            imageView.setImage(new Image(song.getCoverPath().toString())); // Load image
            titleLabel.setText(song.getSongName()); // Set song title
            setGraphic(hbox); // Set the HBox as the cell content
        }
    }
}