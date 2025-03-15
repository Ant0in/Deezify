package musicApp.controllers;

import musicApp.views.ToolBarView;

/**
 * The type Tool bar controller.
 */
public class ToolBarController extends ViewController<ToolBarView,ToolBarController> {
    private final PlayerController playerController;

    /**
     * Instantiates a new Tool bar controller.
     *
     * @param playerController the player controller
     */
    public ToolBarController(PlayerController playerController){
        super(new ToolBarView());
        this.playerController = playerController;
        initView("/fxml/ToolBar.fxml");
    }

    /**
     * Open settings.
     */
    public void openSettings(){
        playerController.openSettings();
    }
}
