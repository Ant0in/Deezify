package musicApp.views;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.PopupWindow;
import musicApp.controllers.ViewController;
import musicApp.services.LanguageService;

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
    protected PopupWindow rootWindow;
    protected StringProperty languageProperty;

    public View() {
        initializeLanguageProperty();
    }

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

    public void initializePopupWindow(String fxmlPath) throws IOException {
        URL url = getClass().getResource(fxmlPath);
        FXMLLoader loader = new FXMLLoader(url);
        loader.setController(this);
        rootWindow = loader.load();
    }

    public void initializeLanguageProperty() {
        languageProperty = LanguageService.getInstance().getLanguageProperty();
        languageProperty.addListener((observable, oldValue, newValue) -> {
            refreshTranslation();
        });
    }

    protected void refreshTranslation() {
        // Override this method in subclasses to refresh translations
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