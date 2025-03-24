package musicApp.views.playlistNavigator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import musicApp.controllers.PlaylistCellController;
import musicApp.models.Library;
import musicApp.utils.LanguageManager;
import musicApp.views.View;

public class PlaylistCellView extends View<PlaylistCellView, PlaylistCellController> {
    @FXML
    private ImageView imageCover;
    @FXML
    private Label playlistNameLabel, playlistSizeLabel;

    @Override
    public void init() {
        initComponents();
    }

    private void initComponents() {
        if (viewController.getLibrary() == null) return;
        playlistNameLabel.setText(viewController.getLibraryName());
        playlistSizeLabel.setText(viewController.getLibrary().size() + " songs");
    }

    public void update(Library library) {
        playlistNameLabel.setText(viewController.getLibraryName());
        playlistSizeLabel.setText(library.size() + " songs");
        imageCover.setImage(library.getCoverImage());
    }
}
