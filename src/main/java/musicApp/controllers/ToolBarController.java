package musicApp.controllers;

import javafx.scene.control.Alert;
import musicApp.views.ToolBarView;

/**
 * The type Tool bar controller.
 */
public class ToolBarController extends ViewController<ToolBarView> implements ToolBarView.ToolBarViewListener {
    private final PlayerController playerController;

    /**
     * Instantiates a new Tool bar controller.
     *
     * @param playerController the player controller
     */
    public ToolBarController(PlayerController playerController) {
        super(new ToolBarView());
        view.setListener(this);
        this.playerController = playerController;
        initView("/fxml/ToolBar.fxml");
    }

    /**
     * Open settings.
     */
    public void handleOpenSettings() {
        playerController.openSettings();
    }

    /**
     * Exit app properly.
     */
    public void handleExitApp() {
        playerController.close();
    }

    public void handleReturn() {
        playerController.returnToUsersWindow();
    }


    public void handleNotFoundImage(String errorMessage) {
        alertService.showAlert("ToolBarController : " + errorMessage, Alert.AlertType.ERROR);
    }
}
