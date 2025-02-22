package MusicApp.Models;

import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;

public class SongCell extends ListCell<Song> {
    private final ImageView imageView = new ImageView();
    private final Label titleLabel = new Label();
    private final HBox hbox = new HBox(10); // Spacing between image and text

    public SongCell() {
        imageView.setFitWidth(25);  // Set width of image
        imageView.setFitHeight(25); // Set height of image
        hbox.getChildren().addAll(imageView, titleLabel);
    }

    @Override
    protected void updateItem(Song song, boolean empty) {
        super.updateItem(song, empty);

        if (empty || song == null) {
            setGraphic(null);
        } else {
            imageView.setImage(new Image(song.getImagePath())); // Load image
            titleLabel.setText(song.getSongName()); // Set song title
            setGraphic(hbox); // Set the HBox as the cell content
        }
    }
}