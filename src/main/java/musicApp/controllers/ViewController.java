package musicApp.controllers;

import musicApp.views.View;
import javafx.scene.layout.Pane;


/**
 * The type View controller.
 *
 * @param <V> the type parameter
 * @param <C> the type parameter
 */
public abstract class ViewController<V extends View<V, C>, C extends ViewController<V, C>> {
    /**
     * The View.
     */
    protected V view;

    /**
     * Instantiates a new View controller.
     *
     * @param view the view
     */
    public ViewController(V view) {
        this.view = view;
        this.view.setViewController((C) this);
    }

    /**
     * Init view.
     *
     * @param fxmlPath the fxml path
     */
    protected void initView(String fxmlPath) {
        try {
            view.initializeScene(fxmlPath);
            view.init();
        } catch (Exception e) {
            view.displayError(e.getMessage());
        }
    }

    /**
     * Gets root.
     *
     * @return The root Pane of the view
     */
    public Pane getRoot() {
        return view.getRoot();
    }

}
