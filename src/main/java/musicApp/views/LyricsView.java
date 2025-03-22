package musicApp.views;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import musicApp.controllers.LyricsController;

import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.ButtonBar;
import javafx.geometry.Pos;
import java.util.Optional;

/**
 * The LyricsView class is responsible for displaying and updating
 * the lyrics of a song. It handles the user interactions and delegates
 * actions to the LyricsController.
 */
public class LyricsView extends View<LyricsView, LyricsController> {

    @FXML
    private VBox lyricsContainer;

    /**
     * Initializes the view. Sets up listeners and UI components.
     */
    @Override
    public void init() {
        initButtons();
    }

    /**
     * Initializes the buttons by adding a listener to the currently loaded song property.
     * When the song changes, the lyrics are updated.
     */
    public void initButtons() {
        viewController.getCurrentlyLoadedSongStringProperty().addListener((obs, oldTitle, newTitle) -> {
            updateLyrics();
        });
    }

    /**
     * Displays a dialog to edit the lyrics.
     *
     * @param initialText the initial text to display in the text area
     * @return an Optional containing the edited lyrics if saved, otherwise an empty Optional
     */
    public Optional<String> showEditLyricsDialog(String initialText) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Edit Lyrics");
        dialog.setHeaderText("Edit the lyrics for this song:");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        TextArea textArea = new TextArea(initialText);
        textArea.setWrapText(true);
        textArea.setPrefWidth(400);
        textArea.setPrefHeight(300);
        dialog.getDialogPane().setContent(textArea);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
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
        Label noLyricsLabel = new Label("No lyrics available for this song.\nWould you like to add them?");
        noLyricsLabel.getStyleClass().add("no-lyrics-label");


        Button addLyricsButton = createEditButton("Add Lyrics");
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
}
