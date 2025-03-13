package MusicApp.Controllers;

import MusicApp.Views.View;
import javafx.scene.layout.Pane;


public abstract class ViewController<V extends View<V, C>, C extends ViewController<V, C>> {
    protected V view;

    public ViewController(V view) {
        this.view = view;
        this.view.setViewController((C) this);
    }

    protected void initView(String fxmlPath) {
        try {
            view.initializeScene(fxmlPath);
            view.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return The root Pane of the view
     */
    public Pane getRoot() {
        return view.getRoot();
    }

}
