package musicApp.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.PopupWindow;

import java.io.IOException;
import java.net.URL;


public abstract class TestView<L> {
    /**
     * The Scene.
     */
    protected Scene scene;

    protected L listener;
    /**
     * The Root pane.
     */
    protected Pane rootPane;
    protected PopupWindow rootWindow;

    public void setListener(L _listener) {
        listener = _listener;
    }

    /**
     * Initialize scene.
     *
     * @param fxmlPath the fxml path
     * @throws IOException the io exception
     */
    public void initializeScene(String fxmlPath) throws IOException {
        URL url = getClass().getResource(fxmlPath);
        FXMLLoader loader = new FXMLLoader(url);
        loader.setController(this);
        rootPane = loader.load();
        scene = new Scene(rootPane);
    }

    public void initializePopupWindow(String fxmlPath) throws IOException {
        URL url = getClass().getResource(fxmlPath);
        FXMLLoader loader = new FXMLLoader(url);
        loader.setController(this);
        rootWindow = loader.load();
    }

    /**
     * Gets root.
     *
     * @return the root
     */
    public Pane getRoot() {
        return rootPane;
    }

    /**
     * Init.
     */
    public abstract void init();

    /**
     * Display error.
     *
     * @param message the message
     */
    public void displayError(String message) {
        System.err.println(message);
    }
}