package musicApp.services;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import java.io.File;
import musicApp.enums.SupportedFileType;

public class FileDialogService {

    /**
     * Opens a directory chooser dialog and returns the selected directory.
     *
     * @param ownerWindow The window over which the dialog should open (can be null).
     * @param title       The title of the dialog.
     * @return The selected directory or null if none selected.
     */
    public static File chooseDirectory(Window ownerWindow, String title) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(title);
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        return directoryChooser.showDialog(ownerWindow);
    }

    /**
     * Opens a file chooser dialog restricted to audio files (.mp3, .wav).
     *
     * @param ownerWindow The window over which the dialog should open (can be null).
     * @param title       The title of the dialog.
     * @return The selected file or null if none selected.
     */
    public static File chooseAudioFile(Window ownerWindow, String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Audio Files", SupportedFileType.getExtensionsForFileFilter())
        );
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        return fileChooser.showOpenDialog(ownerWindow);
    }
}
