package musicApp.views;

import musicApp.controllers.ToolBarController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;

import java.util.Objects;

/**
 * The ToolBar view.
 */
public class ToolBarView extends View<ToolBarView,ToolBarController> {
    @FXML
    private Button exitButton, btnSettings;
    @FXML
    private Region spacer;

    /**
     * Instantiates a new Tool bar view.
     */
    public ToolBarView(){

    }

    /**
     * Initialize the view.
     */
    public void init(){
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
     * @param event The action event.
     */
    private void handleSettings(ActionEvent event) {
        viewController.openSettings();
    }

    /**
     * Handle the exit button
     */
    private void handleExitApp(ActionEvent actionEvent) {
        Platform.exit();
    }

}
