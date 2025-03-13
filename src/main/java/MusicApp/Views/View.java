package MusicApp.Views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import MusicApp.Controllers.ViewController;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;

public abstract class View<V extends View<V, C>, C extends ViewController<V, C>> {
    protected Scene scene;
    protected C viewController;
    protected Pane rootPane;

    public void setViewController(C viewController) {
        this.viewController = viewController;
    }

    public void initializeScene(String fxmlPath) throws IOException {
        URL url = getClass().getResource(fxmlPath);
        FXMLLoader loader = new FXMLLoader(url);
        loader.setController(this);
        rootPane = loader.load();
        scene = new Scene(rootPane);
    }

    public Pane getRoot() {
        return rootPane;
    }

    public abstract void init();
}