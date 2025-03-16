package musicApp.views;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import musicApp.controllers.LyricsController;

import java.util.ArrayList;
import java.util.List;

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

    public void updateLyrics() {
        lyricsContainer.getChildren().clear();
        List<String> lyrics = viewController.getCurrentSongLyrics();
        for (String line : lyrics) {
            Label lyricLine = new Label(line);
            lyricLine.setWrapText(true);
            lyricLine.setMaxWidth(Double.MAX_VALUE);
            lyricsContainer.getChildren().add(lyricLine);
        }
    }
}
