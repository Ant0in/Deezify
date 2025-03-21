package musicApp.views;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import musicApp.controllers.LyricsController;

import java.util.ArrayList;
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


public class LyricsView extends View<LyricsView, LyricsController> {
    @FXML
    private VBox lyricsContainer;

    @Override
    public void init() {
        initButtons();
    }

    public void initButtons() {
        viewController.getCurrentlyLoadedSongStringProperty().addListener((obs, oldTitle, newTitle) -> {
            updateLyrics();
        });
    }

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


    public void updateLyrics() {
        lyricsContainer.getChildren().clear();
        List<String> lyrics = viewController.getCurrentSongLyrics();
        
        if (lyrics == null || lyrics.isEmpty()) {

            Label noLyricsLabel = new Label("No lyrics available for this song.\nWould you like to add them?");
            noLyricsLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: gray;");

            Button addLyricsButton = new Button("Add Lyrics");
            ImageView pencilIcon = new ImageView(new Image(getClass().getResource("/images/edit.png").toExternalForm()));
            pencilIcon.setFitWidth(16);
            pencilIcon.setFitHeight(16);
            addLyricsButton.setGraphic(pencilIcon);
            addLyricsButton.setOnAction(e -> viewController.editLyrics());

            VBox placeholder = new VBox(10, noLyricsLabel, addLyricsButton);
            placeholder.setAlignment(Pos.CENTER);
            lyricsContainer.getChildren().add(placeholder);
        } else {
            HBox header = new HBox();
            header.setAlignment(Pos.TOP_RIGHT);
            Button editButton = new Button();
            ImageView pencilIcon = new ImageView(new Image(getClass().getResource("/images/edit.png").toExternalForm()));
            pencilIcon.setFitWidth(16);
            pencilIcon.setFitHeight(16);
            editButton.setGraphic(pencilIcon);
            editButton.setOnAction(e -> viewController.editLyrics());
            header.getChildren().add(editButton);
            lyricsContainer.getChildren().add(header);

            for (String line : lyrics) {
                Label lyricLine = new Label(line);
                lyricLine.setWrapText(true);
                lyricLine.setMaxWidth(Double.MAX_VALUE);
                lyricLine.setStyle("-fx-font-size: 16px; -fx-text-fill: black;");
                lyricsContainer.getChildren().add(lyricLine);
            }
        }        
        lyricsContainer.requestLayout();
    }
}

