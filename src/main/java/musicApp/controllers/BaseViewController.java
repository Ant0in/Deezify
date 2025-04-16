package musicApp.controllers;

import javafx.scene.layout.Pane;
import javafx.stage.PopupWindow;
import musicApp.utils.AlertService;
import musicApp.views.BaseView;
import musicApp.views.View;


/**
 * The type View controller.
 */
public abstract class BaseViewController<V extends BaseView> {
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
    public BaseViewController(V view) {
        this.view = view;
        alertService = new AlertService();
//        view.setListener(this);
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
