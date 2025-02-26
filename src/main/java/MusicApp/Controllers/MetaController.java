package MusicApp.Controllers;

import javafx.stage.Stage;

import java.io.IOException;

public class MetaController {

    public enum Scenes {
        MAINWINDOW
    }

    private final Stage stage;
    private final PlayerController playerController;

    public MetaController(Stage stage) throws IOException {
        this.stage = stage;
        this.playerController = new PlayerController();
    }

    public final void switchScene(Scenes scene) {
        switch (scene) {
            case Scenes.MAINWINDOW ->this.playerController.show(this.stage);
        }
    }
}
