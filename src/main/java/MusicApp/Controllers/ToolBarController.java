package MusicApp.Controllers;

import MusicApp.Views.ControlPanelView;
import MusicApp.Views.ToolBarView;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class ToolBarController {
    private PlayerController playerController;
    private ToolBarView toolBarView;

    public ToolBarController(PlayerController playerController){
        this.playerController = playerController;
        initToolBarView();
    }

    private void initToolBarView(){
        this.toolBarView = new ToolBarView();
        this.toolBarView.setController(this);
        try {
            this.toolBarView.initializeScene("/fxml/ToolBar.fxml");
            this.toolBarView.initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Pane getRoot(){
        return toolBarView.getRoot();
    }

    public void openSettings(){
        playerController.openSettings();
    }
}
