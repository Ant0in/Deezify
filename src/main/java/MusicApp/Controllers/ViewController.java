package MusicApp.Controllers;

import MusicApp.Views.View;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public abstract class ViewController {
    protected View view;

    public ViewController(){
    }

    protected void initView(String fxmlPath){
        this.view = view;
        this.view.setController(this);
        try {
            this.view.initializeScene(fxmlPath);
            this.view.initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return The root Pane of the view
     */
    public Pane getRoot(){
        return view.getRoot();
    }

}
