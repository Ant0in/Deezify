package musicApp.controllers;

import javafx.fxml.Initializable;
import musicApp.views.ToolBarView;

/**
 * The type Tool bar controller.
 */
public class ToolBarController extends BaseViewController<ToolBarView> implements ToolBarView.ToolBarViewListener {
    private final PlayerController playerController;

    /**
     * Instantiates a new Tool bar controller.
     *
     * @param _playerController the player controller
     */
    public ToolBarController(PlayerController _playerController) {
        super(new ToolBarView());
        view.setListener(this);
        playerController = _playerController;
        initView("/fxml/ToolBar.fxml");
    }

    /**
     * Open settings.
     */
    @Override
    public void openSettings(){ playerController.openSettings(); }

    /**
     * Exit app properly.
     */
    @Override
    public void exitApp() { System.exit(0); }

}
