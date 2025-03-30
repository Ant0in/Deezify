package musicApp.controllers;

import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import musicApp.models.Metadata;
import musicApp.models.Song;
import musicApp.utils.LanguageManager;
import musicApp.utils.MetadataUtils;
import musicApp.views.EditMetadataView;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

public class EditMetadataController extends ViewController<EditMetadataView, EditMetadataController> {
    private final File[] selectedFile = new File[1];
    private final Song song;
    private final Stage editStage = new Stage();
    private final SongCellController songCellController;

    public EditMetadataController(SongCellController cellController) {
        super(new EditMetadataView());
        this.songCellController = cellController;
        this.song = songCellController.getSong();
        initView("/fxml/EditMetadata.fxml");

        if (song != null) {
            view.populateFields(
                    song.getTitle(),
                    song.getArtist(),
                    song.getGenre(),
                    song.getUserTags()
            );
            view.setCoverImage(song.getCoverImage());
        }

        editStage.setTitle(LanguageManager.getInstance().get("edit_metadata.title"));
        editStage.setScene(view.getScene());
        editStage.show();
    }

    /**
     * Handles the user action of choosing a cover image.
     * Opens a file chooser, and if a valid image is selected, loads and displays it in the view.
     */
    public void handleChooseCover() {
        File file = promptUserForCoverFile();
        if (file != null && file.exists()) {
            loadAndApplyCoverImage(file);
        }
    }

    /**
     * Prompts the user with a FileChooser dialog to select an image file.
     *
     * @return the selected File if the user chose one, or {@code null} otherwise
     */
    private File promptUserForCoverFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Cover Image");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        return fileChooser.showOpenDialog(editStage);
    }

    /**
     * Loads an image from the given file and updates the view if successful.
     *
     * @param file the image file to load
     */
    private void loadAndApplyCoverImage(File file) {
        try {
            Image image = new Image(file.toURI().toString());
            if (!image.isError()) {
                view.setCoverImage(image);
                selectedFile[0] = file;
            } else {
                System.err.println("Failed to load image: " + file.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * Handle when the user wants to edit the metadata of the song.
     * Leave the field to `null` if you don't want to change it.
     *
     * @param title  the title
     * @param artist the artist
     * @param genre  the genre
     */
    public void handleSaveMetadata(String title, String artist, String genre, Set<String> userTags) {
        if (song == null) {
            view.displayError("No song to edit");
            return;
        }
        try {
            Metadata newMetadata = song.getMetadata();
            newMetadata.setTitle(title);
            newMetadata.setArtist(artist);
            newMetadata.setGenre(genre);
            newMetadata.setUserTags(new ArrayList<>(userTags));
            if (selectedFile[0] != null) {
                newMetadata.loadCoverFromPath(selectedFile[0].getAbsolutePath());
            }
            MetadataUtils util = new MetadataUtils();

            util.setMetadata(newMetadata, song.getFilePath().toFile());
        } catch (Exception e) {
            e.printStackTrace();
            view.displayError(e.getMessage());
            return;
        }

        song.reloadMetadata();
        songCellController.refreshSong();
        editStage.close();
    }

    /**
     * Handle when the user wants to cancel the edit.
     */
    public void handleCancel() {
        songCellController.refreshSong();
        editStage.close();
    }

}
