package musicApp.views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import musicApp.services.ViewService;

/**
 * The ToolBar view.
 */
public class ToolBarView extends View {

    private ToolBarViewListener listener;

    @FXML
    private Button exitButton, settingsButton, returnButton;
    @FXML
    private Region spacer;

    /**
     * Listener interface for handling user actions from the controller.
     */
    public interface ToolBarViewListener extends ViewService.ViewServiceListener {
        void handleOpenSettings();

        void handleExitApp();

        void handleReturn();
    }

    /**
     * Sets listener.
     *
     * @param newListener the listener
     */
    public void setListener(ToolBarViewListener newListener) {
        listener = newListener;
    }


    /**
     * Initialize the view.
     */
    public void init() {
        bindButtonsImages();
        setButtonActions();
    }

    /**
     * Bind the images of the buttons.
     */
    private void bindButtonsImages() {
        ViewService viewService = new ViewService();
        viewService.setButtonIcon(exitButton, "/images/cross.png", listener);
        viewService.setButtonIcon(settingsButton, "/images/settings.png", listener);
        viewService.setButtonIcon(returnButton, "/images/return.png", listener);
    }

    /**
     * Set the actions of the buttons.
     */
    private void setButtonActions() {
        exitButton.setOnAction(this::handleExitApp);
        settingsButton.setOnAction(this::handleSettings);
        returnButton.setOnAction(this::handleReturn);
    }

    /**
     * Handle the settings button.
     *
     * @param event The action event.
     */
    private void handleSettings(ActionEvent event) {
        listener.handleOpenSettings();
    }

    /**
     * Handle the exit button
     */
    private void handleExitApp(ActionEvent event) {
        listener.handleExitApp();
    }

    /**
     * Handle the return button.
     * @param event
     */
    private void handleReturn(ActionEvent event) {
        listener.handleReturn();
    }
}
