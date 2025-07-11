package musicApp.controllers;

import javafx.scene.layout.Pane;
import musicApp.services.AlertService;
import musicApp.views.View;


/**
 * The type View controller.
 *
 * @param <V> the type parameter
 */
public abstract class ViewController<V extends View> {
    /**
     * The View.
     */
    protected V view;
    protected AlertService alertService;

    /**
     * Instantiates a new View controller.
     *
     * @param view the view
     */
    public ViewController(V view) {
        this.view = view;
        alertService = new AlertService();
    }

    /**
     * Init view.
     *
     * @param fxmlPath the fxml path
     */
    protected void initView(String fxmlPath) {
        initView(fxmlPath, false);
    }

    /**
     * Init view.
     *
     * @param fxmlPath the fxml path
     */
    protected void initView(String fxmlPath, boolean isPopup) {
        try {
            if (isPopup) {
                view.initializePopupWindow(fxmlPath);
            } else {
                view.initializeScene(fxmlPath);
            }
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
