package MusicApp.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import MusicApp.controllers.ViewController;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;

/**
 * View class for the application.
 *
 * @param <V> the View class that gets extended
 * @param <C> the Controller class that gets extended
 */
public abstract class View<V extends View<V, C>, C extends ViewController<V, C>> {
    /**
     * The Scene.
     */
    protected Scene scene;
    /**
     * The View controller.
     */
    protected C viewController;
    /**
     * The Root pane.
     */
    protected Pane rootPane;

    /**
     * Sets view controller.
     *
     * @param viewController the view controller
     */
    public void setViewController(C viewController) {
        this.viewController = viewController;
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