package MusicApp.Controllers;

import MusicApp.Views.ToolBarView;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class ToolBarController extends ViewController<ToolBarView,ToolBarController> {
    private PlayerController playerController;

    public ToolBarController(PlayerController playerController){
        super(new ToolBarView());
        this.playerController = playerController;
        initView("/fxml/ToolBar.fxml");
    }


    public void openSettings(){
        playerController.openSettings();
    }
}
