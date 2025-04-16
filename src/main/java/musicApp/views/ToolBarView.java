package musicApp.views;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import musicApp.controllers.BaseViewController;

import java.util.Objects;

/**
 * The ToolBar view.
 */
public class ToolBarView extends BaseView {

    private ToolBarViewListener listener;

    @FXML
    private Button exitButton, btnSettings;
    @FXML
    private Region spacer;

    /**
     * Instantiates a new Tool bar view.
     */
    public ToolBarView() {

    }

    public interface ToolBarViewListener {
        void openSettings();
        void exitApp();
    }

    /**
     * Sets listener.
     *
     * @param listener the listener
     */
    public void setListener(ToolBarViewListener listener) {
        this.listener = listener;
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
        ImageView exitIcon = new ImageView(Objects.requireNonNull(getClass().getResource("/images/cross.png")).toExternalForm());
        exitIcon.setFitWidth(20);
        exitIcon.setFitHeight(20);
        exitButton.setGraphic(exitIcon);

        ImageView settingsIcon = new ImageView(Objects.requireNonNull(getClass().getResource("/images/settings.png")).toExternalForm());
        settingsIcon.setFitWidth(20);
        settingsIcon.setFitHeight(20);
        btnSettings.setGraphic(settingsIcon);
    }

    /**
     * Set the actions of the buttons.
     */
    private void setButtonActions() {
        exitButton.setOnAction(this::handleExitApp);
        btnSettings.setOnAction(this::handleSettings);
    }

    /**
     * Handle the settings button.
     *
     * @param event The action event.
     */
    private void handleSettings(ActionEvent event) {
        listener.openSettings();
    }

    /**
     * Handle the exit button
     */
    private void handleExitApp(ActionEvent actionEvent) {
        Platform.exit();
        listener.exitApp();
    }
}
