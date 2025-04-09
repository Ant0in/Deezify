package musicApp.views.songs;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import java.util.List;
import javafx.stage.FileChooser;
import java.io.File;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

import musicApp.views.View;
import musicApp.utils.lyrics.KaraokeLine;
import musicApp.controllers.songs.LyricsController;
import musicApp.utils.LanguageManager;
import musicApp.controllers.songs.KaraokeController;

/**
 * The LyricsView class is responsible for displaying and updating
 * the lyrics of a song. It handles the user interactions and delegates
 * actions to the LyricsController.
 */
public class LyricsView extends View<LyricsView, LyricsController> {
    @FXML
    private HBox karaokeHeader;

    @FXML
    private VBox lyricsContainer, karaokeLyricsContainer, karaokePlaceholder;

    @FXML
    private Label lyricsTitle, karaokeNoLyricsLabel;

    @FXML
    private Button simpleLyricsButton, karaokeLyricsButton, karaokeAddLyricsButton, karaokeEditButton;

    @FXML
    private ButtonType yesButton, noButton, cancelButton,saveButton;

    @FXML
    private ScrollPane scrollPane, karaokeScrollPane;

    private String dialogTitleText;
    private String dialogHeaderText;
    private String noLyricsText;
    private String addLyricsText;
    private String karaokeTitleText;
    private String karaokeHeaderText;
    private String karaokeContentText;

    private KaraokeController karaokeController;

    /**
     * Initializes the view. Sets up listeners and UI components.
     */
    @Override
    public void init() {
        initTranslation();
        initButtons();
    }

    /**
     * Initializes the buttons by adding a listener to the currently loaded song property.
     * When the song changes, the lyrics are updated.
     */
    public void initButtons() {
        karaokeAddLyricsButton.setOnAction(e -> {
            karaokeController.importKaraokeLyrics();
        });
        karaokeEditButton.setOnAction(e -> {
            karaokeController.importKaraokeLyrics();
        });

        simpleLyricsButton.setOnAction(e -> {
            scrollPane.setVisible(true);
            scrollPane.setManaged(true);
            karaokeScrollPane.setVisible(false);
            karaokeScrollPane.setManaged(false);
            updateLyrics();
        });

        karaokeLyricsButton.setOnAction(e -> {
            karaokeScrollPane.setVisible(true);
            karaokeScrollPane.setManaged(true);
            scrollPane.setVisible(false);
            scrollPane.setManaged(false);
            updateKaraokeLyrics();
        });

        viewController.getCurrentlyLoadedSongStringProperty().addListener((obs, oldTitle, newTitle) -> {
            initTranslation();
            simpleLyricsButton.fire();
            updateKaraokeLyrics();
        });
    }

    /**
     * Initializes translations for UI elements using the LanguageManager.
     * This method loads translated text for labels, buttons, and dialogs.
     */
    private void initTranslation() {
        LanguageManager lang = LanguageManager.getInstance();
        yesButton = new ButtonType(lang.get("button.yes"), ButtonBar.ButtonData.YES);
        noButton = new ButtonType(lang.get("button.no"), ButtonBar.ButtonData.NO);
        cancelButton = new ButtonType(lang.get("button.cancel"), ButtonBar.ButtonData.CANCEL_CLOSE);
        saveButton = new ButtonType(lang.get("button.save"), ButtonBar.ButtonData.OK_DONE);
        karaokeEditButton.setText(lang.get("button.edit"));
        karaokeLyricsButton.setText(lang.get("button.modeKaraoke"));
        simpleLyricsButton.setText(lang.get("button.simpleLyrics"));
        karaokeNoLyricsLabel.setText(lang.get("karaoke.noLyrics"));
        karaokeAddLyricsButton.setText(lang.get("button.addKaraoke"));
        lyricsTitle.setText(lang.get("lyrics.title"));
        dialogTitleText = lang.get("dialog.editLyrics.title");
        dialogHeaderText = lang.get("dialog.editLyrics.header");
        noLyricsText = lang.get("lyrics.noLyrics");
        addLyricsText = lang.get("button.addLyrics");
        karaokeTitleText = lang.get("karaoke.title");
        karaokeHeaderText = lang.get("karaoke.header");
        karaokeContentText = lang.get("karaoke.content");
    }

    public void setKaraokeController(KaraokeController karaokeController) {
        this.karaokeController = karaokeController;
    }

    /**
     * Displays a dialog to edit the lyrics.
     *
     * @param initialText the initial text to display in the text area
     * @return an Optional containing the edited lyrics if saved, otherwise an empty Optional
     */
    public Optional<String> showEditLyricsDialog(String initialText) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle(dialogTitleText);
        dialog.setHeaderText(dialogHeaderText);
        dialog.getDialogPane().getButtonTypes().addAll(saveButton, cancelButton);

        TextArea textArea = new TextArea(initialText);
        textArea.setWrapText(true);
        textArea.setPrefWidth(400);
        textArea.setPrefHeight(300);
        dialog.getDialogPane().setContent(textArea);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButton) {
                return textArea.getText();
            }
            return null;
        });

        return dialog.showAndWait();
    }

    /**
     * Updates the lyrics displayed in the view.
     * Clears the current lyrics container and loads either a placeholder
     * or the actual lyrics along with an edit header.
     */
    public void updateLyrics() {
        lyricsContainer.getChildren().clear();
        List<String> lyrics = viewController.getCurrentSongLyrics();

        if (lyrics == null || lyrics.isEmpty()) {
            displayEmptyLyricsPlaceholder();
        } else {
            displayLyricsWithHeader(lyrics);
        }
        lyricsContainer.requestLayout();
    }

    /**
     * Displays a placeholder when no lyrics are available.
     * The placeholder includes a message and a button to add lyrics.
     */
    private void displayEmptyLyricsPlaceholder() {
        Label noLyricsLabel = new Label(noLyricsText);
        noLyricsLabel.getStyleClass().add("no-lyrics-label");

        Button addLyricsButton = createEditButton(addLyricsText);
        addLyricsButton.setOnAction(e -> viewController.editLyrics());

        VBox placeholder = new VBox(10, noLyricsLabel, addLyricsButton);
        placeholder.setAlignment(Pos.CENTER);
        lyricsContainer.getChildren().add(placeholder);
    }

    /**
     * Displays the lyrics along with an edit header.
     *
     * @param lyrics the list of lyric lines to display
     */
    private void displayLyricsWithHeader(List<String> lyrics) {
        HBox header = new HBox();
        header.setAlignment(Pos.TOP_RIGHT);
        Button editButton = createEditButton(null);
        editButton.setOnAction(e -> viewController.editLyrics());
        header.getChildren().add(editButton);
        lyricsContainer.getChildren().add(header);

        for (String line : lyrics) {
            Label lyricLine = new Label(line);
            lyricLine.setWrapText(true);
            lyricLine.getStyleClass().add("lyrics-text");
            lyricsContainer.getChildren().add(lyricLine);
        }
    }

    /**
     * Creates a button with an edit icon and optional text.
     *
     * @param text the text to display on the button; if null, only the icon is shown
     * @return the configured Button instance
     */
    private Button createEditButton(String text) {
        Button button = new Button();
        if (text != null) {
            button.setText(text);
        }
        ImageView pencilIcon = new ImageView(new Image(getClass().getResource("/images/edit.png").toExternalForm()));
        pencilIcon.setFitWidth(16);
        pencilIcon.setFitHeight(16);
        button.setGraphic(pencilIcon);
        return button;
    }

    /**
     * Refreshes the UI by reloading the translations.
     */
    public void refreshUI() {
        initTranslation();
        updateLyrics();
        updateKaraokeLyrics();
    }

    /**
     * Updates the karaoke lyrics displayed in the view.
     * <p>
     * This method retrieves the list of karaoke lines from the karaokeController
     * and then updates the karaokeLyricsContainer with labels for each line.
     * It retains the karaokePlaceholder and karaokeHeader nodes if they already exist.
     * If there are no karaoke lines, the placeholder is set visible and managed.
     */
    public void updateKaraokeLyrics() {
        List<KaraokeLine> karaokeLines = karaokeController.getKaraokeLyrics();

        karaokeLyricsContainer.getChildren().removeIf(node -> node != karaokePlaceholder && node != karaokeHeader);
        if (karaokeLines.isEmpty()) {
            karaokePlaceholder.setVisible(true);
            karaokePlaceholder.setManaged(true);

        } else {
            karaokePlaceholder.setVisible(false);
            karaokePlaceholder.setManaged(false);

            for (KaraokeLine line : karaokeLines) {
                Label label = new Label(line.getLyric());
                label.getStyleClass().add("lyrics-text");
                karaokeLyricsContainer.getChildren().add(label);
            }
        }
    }

    /**
     * Shows a file chooser dialog to select an .lrc file.
     * <p>
     * This method opens a FileChooser configured to filter for .lrc files.
     * It returns an Optional<Path> containing the selected file path, or an empty Optional
     * if no file was selected.
     *
     * @return an Optional containing the selected file path if a file was chosen, otherwise an empty Optional.
     */
    public Optional<Path> showLrcFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select .lrc File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("LRC files", "*.lrc"));
        File selectedFile = fileChooser.showOpenDialog(null);
        return Optional.ofNullable(selectedFile).map(File::toPath);
    }

    /**
     * Shows a confirmation dialog asking if the existing .txt lyrics should be overwritten with the text from the LRC file.
     * <p>
     * This method displays an Alert of type CONFIRMATION using pre-defined button types for "Yes", "No", and "Cancel".
     * It returns an Optional<Boolean>:
     * <ul>
     *   <li>Optional.of(true) if the user confirms ("Yes"),</li>
     *   <li>Optional.of(false) if the user selects "No",</li>
     *   <li>An empty Optional if the user cancels or closes the dialog.</li>
     * </ul>
     *
     * @return an Optional<Boolean> indicating the user's choice.
     */
    public Optional<Boolean> showOverwriteTxtConfirmation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(karaokeTitleText);
        alert.setHeaderText(karaokeHeaderText);
        alert.setContentText(karaokeContentText);

        alert.getButtonTypes().setAll(yesButton, noButton, cancelButton);

        var result = alert.showAndWait();
        if (result.isEmpty() || result.get() == cancelButton) {
            return Optional.empty();
        } else if (result.get() == yesButton) {
            return Optional.of(true);
        } else {
            return Optional.of(false);
        }
    }


}
