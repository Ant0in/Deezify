package musicApp.views;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.PopupWindow;
import musicApp.services.LanguageService;

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
    protected StringProperty languageProperty;

    public View() {
        initializeLanguageProperty();
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
     * Initialize popup window.
     *
     * @param fxmlPath
     * @throws IOException
     */
    public void initializePopupWindow(String fxmlPath) throws IOException {
        URL url = getClass().getResource(fxmlPath);
        FXMLLoader loader = new FXMLLoader(url);
        loader.setController(this);
        rootWindow = loader.load();
    }

    /**
     * Initialize language property.
     */
    public void initializeLanguageProperty() {
        languageProperty = LanguageService.getInstance().getLanguageProperty();
        languageProperty.addListener((_, _, _) -> refreshTranslation());
    }

    /**
     * Refresh translation.
     */
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