package musicApp.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.PopupWindow;

import java.io.IOException;
import java.net.URL;

/**
 * View class for the application.
 */
public abstract class View {
    /**
     * The Scene.
     */
    protected Scene scene;

    /**
     * The Root pane.
     */
    protected Pane rootPane;
    protected PopupWindow rootWindow;


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