package musicApp.controllers.songs;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import musicApp.controllers.ViewController;
import musicApp.models.Metadata;
import musicApp.models.Song;
import musicApp.services.MetadataService;
import musicApp.services.VideoService;
import musicApp.views.songs.EditMetadataView;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

/**
 * The type Edit metadata controller.
 */
public class EditMetadataController extends ViewController<EditMetadataView> implements EditMetadataView.EditMetadataViewListener {
    private File selectedCoverFile;
    private final Song song;
    private final SongCellController songCellController;


    /**
     * Instantiates a new Edit metadata controller.
     *
     * @param cellController the cell controller
     */
    public EditMetadataController(SongCellController cellController) {
        super(new EditMetadataView());
        view.setListener(this);
        songCellController = cellController;
        song = songCellController.getSong();
        initView("/fxml/EditMetadata.fxml");

        if (song != null) {
            view.populateFields(
                    song.getTitle(),
                    song.getArtist(),
                    song.getAlbum(),
                    song.getGenre(),
                    song.getUserTags()
            );
            view.setCoverImage(song.getCoverImage());
        }
    }

    /**
     * Handles the user action of choosing a cover image.
     * Opens a file chooser, and if a valid image is selected, loads and displays it in the view.
     */
    public void handleCoverChanged(File file) {
        if (file != null && file.exists()) {
            long maxSizeBytes = 100L * 1024 * 1024; // 100 MB in bytes
            if (file.length() > maxSizeBytes) {
                alertService.showAlert("Selected file is too large. Please choose a file under 100 MB.", Alert.AlertType.ERROR);
                return;
            }

            loadAndApplyCoverImage(file);
        }
    }

    /**
     * Loads an image from the given file and updates the view if successful.
     *
     * @param file the image file to load
     */
    private void loadAndApplyCoverImage(File file) {

        try {

            Image image = null;

            // Check if File is a video file
            if (file.toPath().toString().endsWith(".mp4")) {
                VideoService videoService = new VideoService();
                image = videoService.getFirstFrame(file);
                if (image == null) { return; }
            } else {
                image = new Image(file.toURI().toString());
            }

            if ( !image.isError()) {
                view.setCoverImage(image);
                selectedCoverFile = file;
            } else {
                System.err.println("Failed to load file: " + file.getName());
            }
        } catch (Exception e) {
            alertService.showExceptionAlert(e);
        }
    }


    /**
     * Handle when the user wants to edit the metadata of the song.
     * Leave the field to `null` if you don't want to change it.
     *
     * @param title    the title
     * @param artist   the artist
     * @param album    the album
     * @param genre    the genre
     * @param userTags the user tags
     */
    public void handleSaveMetadata(String title, String artist, String album, String genre, Set<String> userTags) {
        if (song == null) {
            view.displayError("No song to edit");
            return;
        }
        try {
            Metadata newMetadata = song.getMetadata();
            newMetadata.setTitle(title);
            newMetadata.setArtist(artist);
            newMetadata.setAlbum(album);
            newMetadata.setGenre(genre);
            newMetadata.setUserTags(new ArrayList<>(userTags));
            if (selectedCoverFile != null) {
                newMetadata.loadCoverFromPath(selectedCoverFile);
            }
            MetadataService util = new MetadataService();

            util.setMetadata(newMetadata, song.getFilePath().toFile());
        } catch (Exception e) {
            alertService.showExceptionAlert(e);
            return;
        }

        song.reloadMetadata();
        songCellController.refreshSong();
        view.close();
    }

    /**
     * Handle when the user wants to cancel the edit.
     */
    public void handleCancel() {
        songCellController.refreshSong();
        view.close();
    }

    /**
     * Get artist auto completion optional.
     *
     * @param input the input
     * @return the optional
     */
    public Optional<String> getArtistAutoCompletion(String input){
        return songCellController.getArtistAutoCompletion(input);
    }

    /**
     * Get album auto completion optional.
     *
     * @param input the input
     * @return the optional
     */
    public Optional<String> getAlbumAutoCompletion(String input){
        return songCellController.getAlbumAutoCompletion(input);
    }

    /**
     * Get tag auto completion optional.
     *
     * @param input the input
     * @return the optional
     */
    public Optional<String> getTagAutoCompletion(String input){
        return songCellController.getTagAutoCompletion(input);
    }

}
