package musicApp.views.songs;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import musicApp.models.KaraokeLine;
import musicApp.services.LanguageService;
import musicApp.views.View;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * The LyricsView class is responsible for displaying and updating
 * the lyrics of a song. It handles the user interactions and delegates
 * actions to the LyricsController.
 */
public class LyricsView extends View {
    
    private LyricsViewListener lyricsListener;
    private KaraokeListener karaokeListener;
    private Timeline syncTimeline;


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
    private ScrollPane lyricsScrollPane, karaokeScrollPane;

    /**
     * Listener interface for handling events in the LyricsView.
     * Implement this interface to define behaviors for editing lyrics,
     * retrieving the current song lyrics, and accessing the currently loaded song property.
     */
    public interface LyricsViewListener {
        void handleEditLyrics();

        List<String> getCurrentSongLyrics();

        void handleLoadedSongChange(Runnable callback);

        void handleShowLyrics();
    }

    public interface KaraokeListener {
        void handleImportKaraokeLyrics();

        void handleShowKaraoke();

        void handleStopKaraoke();

        void handleSyncLyrics();

        List<KaraokeLine> getKaraokeLines();
    }

    public void stopKaraoke() {
        if (syncTimeline != null) {
            syncTimeline.stop();
            syncTimeline = null;
        }
    }

    /**
     * Sets listener.
     *
     * @param newListener the listener
     */
    public void setLyricsListener(LyricsViewListener newListener) {
        lyricsListener = newListener;
    }

    /**
     * Initializes the view. Sets up listeners and UI components.
     */
    @Override
    public void init() {
        initButtons();
        refreshTranslation();
    }

    public void setKaraokeListener(KaraokeListener listener) {
        karaokeListener = listener;
    }

    /**
     * Initializes the buttons by adding a listener to the currently loaded song property.
     * When the song changes, the lyrics are updated.
     */
    public void initButtons() {
        karaokeAddLyricsButton.setOnAction(_ -> karaokeListener.handleImportKaraokeLyrics());
        karaokeEditButton.setOnAction(_ -> karaokeListener.handleImportKaraokeLyrics());

        simpleLyricsButton.setOnAction(_ -> {
            lyricsListener.handleShowLyrics();
        });

        karaokeLyricsButton.setOnAction(_ -> {
            karaokeListener.handleShowKaraoke();
        });

        lyricsListener.handleLoadedSongChange(this::handleLoadedSongChange);
    }

    public void showLyrics() {
        lyricsScrollPane.setVisible(true);
        lyricsScrollPane.setManaged(true);
        karaokeScrollPane.setVisible(false);
        karaokeScrollPane.setManaged(false);
    }

    public void showKaraoke() {
        lyricsScrollPane.setVisible(false);
        lyricsScrollPane.setManaged(false);
        karaokeScrollPane.setVisible(true);
        karaokeScrollPane.setManaged(true);
    }

    private void handleLoadedSongChange(){
        karaokeListener.handleStopKaraoke();
        simpleLyricsButton.fire();
    }

    /**
     * Initializes translations for UI elements using the LanguageService.
     * This method loads translated text for labels, buttons, and dialogs.
     */
    @Override
    protected void refreshTranslation() {
        LanguageService lang = LanguageService.getInstance();
        initButtonsTypes(lang);
        karaokeEditButton.setText(lang.get("button.edit"));
        karaokeLyricsButton.setText(lang.get("button.modeKaraoke"));
        simpleLyricsButton.setText(lang.get("button.simpleLyrics"));
        karaokeNoLyricsLabel.setText(lang.get("karaoke.no_lyrics"));
        karaokeAddLyricsButton.setText(lang.get("button.addKaraoke"));
        lyricsTitle.setText(lang.get("lyrics.title"));
    }

    private void initButtonsTypes(LanguageService lang){
        yesButton = new ButtonType(lang.get("button.yes"), ButtonBar.ButtonData.YES);
        noButton = new ButtonType(lang.get("button.no"), ButtonBar.ButtonData.NO);
        cancelButton = new ButtonType(lang.get("button.cancel"), ButtonBar.ButtonData.CANCEL_CLOSE);
        saveButton = new ButtonType(lang.get("button.save"), ButtonBar.ButtonData.OK_DONE);
    }

    /**
     * Displays a dialog to edit the lyrics.
     */
    public Optional<String> getEditedLyrics(String initialText) {
        LanguageService lang = LanguageService.getInstance();
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle(lang.get("dialog.edit_lyrics.title"));
        dialog.setHeaderText(lang.get("dialog.edit_lyrics.header"));
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
        List<String> lyrics = lyricsListener.getCurrentSongLyrics();

        if (lyrics == null || lyrics.isEmpty()) {
            displayEmptyLyricsPlaceholder();
        } else {
            displayLyricsWithHeader(lyrics);
        }
        lyricsContainer.requestLayout();
    }

    public void updateTimeLine() {
        syncTimeline = new Timeline(new KeyFrame(Duration.millis(200), _ -> karaokeListener.handleSyncLyrics()));
        syncTimeline.setCycleCount(Timeline.INDEFINITE);
        syncTimeline.play();
    }

    /**
     * Displays a placeholder when no lyrics are available.
     * The placeholder includes a message and a button to add lyrics.
     */
    private void displayEmptyLyricsPlaceholder() {
        LanguageService lang = LanguageService.getInstance();
        Label noLyricsLabel = new Label(lang.get("lyrics.no_lyrics"));
        noLyricsLabel.getStyleClass().add("no-lyrics-label");

        Button addLyricsButton = createEditButton(lang.get("button.addLyrics"));
        addLyricsButton.setOnAction(_ -> lyricsListener.handleEditLyrics());

        VBox placeholder = new VBox(10, noLyricsLabel, addLyricsButton);
        placeholder.setAlignment(Pos.CENTER);
        lyricsContainer.getChildren().add(placeholder);
    }

    /**
     * Displays the lyrics along with an edit header.

     */
    private void displayLyricsWithHeader(List<String> lyrics) {
        HBox header = new HBox();
        header.setAlignment(Pos.TOP_RIGHT);
        Button editButton = createEditButton(null);
        editButton.setOnAction(_ -> lyricsListener.handleEditLyrics());
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
     */
    private Button createEditButton(String text) {
        Button button = new Button();
        if (text != null) {
            button.setText(text);
        }
        ImageView pencilIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/images/edit.png")).toExternalForm()));
        pencilIcon.setFitWidth(16);
        pencilIcon.setFitHeight(16);
        button.setGraphic(pencilIcon);
        return button;
    }

    /**
     * Clears the karaoke lyrics container and removes all nodes except the placeholder and header.
     */
    private void clearKaraokeContainer() {
        karaokeLyricsContainer.getChildren().removeIf(
                node -> node != karaokePlaceholder && node != karaokeHeader
        );
    }

    /**
     * Sets the visibility of the karaoke placeholder.
     * If visible, it shows a message indicating that no karaoke lyrics are available.
     */
    private void setPlaceholderVisible(boolean visible) {
        karaokePlaceholder.setVisible(visible);
        karaokePlaceholder.setManaged(visible);
    }

    /**
     * Creates a label for a karaoke line with optional highlighting.
     * The label is styled with CSS classes for karaoke and selected lyrics.
     */
    private Label createKaraokeLabel(KaraokeLine line, boolean highlight) {
        Label label = new Label(line.getLyric());
        label.getStyleClass().add("lyrics-text");
        if (highlight) {
            label.getStyleClass().add("selected-lyrics");
        }
        return label;
    }

    /**
     * Renders the karaoke lines in the karaoke lyrics container.
     * It clears the container, sets the placeholder visibility, and adds labels for each line.
     * If a line is active, it scrolls to that line.
     */
    private void renderKaraokeLines(List<KaraokeLine> lines, KaraokeLine activeLine) {
        clearKaraokeContainer();

        if (lines.isEmpty()) {
            setPlaceholderVisible(true);
            return;
        }
        setPlaceholderVisible(false);
        Label activeLabel = null;

        for (KaraokeLine line : lines) {
            boolean isActive = line.equals(activeLine);
            Label label = createKaraokeLabel(line, isActive);
            if (isActive) {
                activeLabel = label;
            }
            karaokeLyricsContainer.getChildren().add(label);
        }

        if (activeLabel != null) {
            final Label labelToScroll = activeLabel;
            Platform.runLater(() -> {
                double totalHeight = karaokeScrollPane.getContent().getBoundsInLocal().getHeight();
                double labelY = labelToScroll.getBoundsInParent().getMinY();
                double scrollValue = labelY / totalHeight;
                karaokeScrollPane.setVvalue(scrollValue);
            });
        }
    }

    /**
     * Updates the karaoke lyrics displayed in the view.
     * It retrieves the karaoke lines from the karaoke controller and renders them.
     */
    public void updateKaraokeLyrics() {
        List<KaraokeLine> karaokeLines = karaokeListener.getKaraokeLines();
        renderKaraokeLines(karaokeLines, null);
    }
    
    
    /**
     * Updates the karaoke lyrics highlight based on the active line.
     * It retrieves the karaoke lines from the karaoke controller and renders them with highlighting.
     */
    public void updateKaraokeLyricsHighlight(List<KaraokeLine> lines, KaraokeLine activeLine) {
        renderKaraokeLines(lines, activeLine);
    }

    /**
     * Shows a file chooser dialog to select an .lrc file.
     */
    public Optional<Path> getLrcFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select .lrc File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("LRC files", "*.lrc"));
        File selectedFile = fileChooser.showOpenDialog(null);
        return Optional.ofNullable(selectedFile).map(File::toPath);
    }

    /**
     * Shows a confirmation dialog asking if the existing .txt lyrics should be overwritten with the text from the LRC file.
     * This method displays an Alert of type CONFIRMATION using pre-defined button types for "Yes", "No", and "Cancel".
     */
    public Optional<Boolean> showOverwriteTxtConfirmation() {
        LanguageService lang = LanguageService.getInstance();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(lang.get("karaoke.title"));
        alert.setHeaderText(lang.get("karaoke.header"));
        alert.setContentText(lang.get("karaoke.content"));

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

    /**
     * Refreshes the UI by reloading the translations.
     */
    public void refreshUI() {
        updateLyrics();
        updateKaraokeLyrics();
    }
}