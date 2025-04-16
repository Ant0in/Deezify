package musicApp.controllers;

import javafx.scene.layout.Pane;
import musicApp.utils.AlertService;
import musicApp.views.TestView;


public abstract class TestViewController<L, V extends TestView<L>> {
    /**
     * The View.
     */
    protected V view;
    protected AlertService alertService;

    /**
     * Instantiates a new View controller.
     *
     * @param _view the view
     */
    public TestViewController(V _view) {
        view = _view;
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
            view.setListener(getListener());
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

    protected abstract L getListener();


    /**
     * Gets root.
     *
     * @return The root Pane of the view
     */
    public Pane getRoot() {
        return view.getRoot();
    }

}
