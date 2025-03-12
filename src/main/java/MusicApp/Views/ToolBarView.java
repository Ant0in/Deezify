package MusicApp.Views;

import MusicApp.Controllers.ControlPanelController;
import MusicApp.Controllers.ToolBarController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class ToolBarView {
    private Scene scene;
    private ToolBarController toolBarController;
    private Pane toolBarRoot;

    @FXML
    private Button exitButton, btnSettings;
    @FXML
    private Region spacer;

    public ToolBarView(){
    }

    public void setController(ToolBarController toolBarController){
        this.toolBarController = toolBarController;
    }

    /**
     * Initialize the FXML scene.
     * @param fxmlPath The path to the FXML file.
     * @throws IOException If an error occurs while loading the FXML file.
     */
    public void initializeScene(String fxmlPath) throws IOException {
        URL url = PlayerView.class.getResource(fxmlPath);
        FXMLLoader loader = new FXMLLoader(url);
        loader.setController((Object) this);
        toolBarRoot = loader.load();
        this.scene = new Scene(toolBarRoot);
    }

    public Pane getRoot(){
        return toolBarRoot;
    }

    public void initialize(){
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

    private void setButtonActions() {
        exitButton.setOnAction(this::handleExitApp);
        btnSettings.setOnAction(this::handleSettings);
    }




    /**
     * Handle the settings button.
     * @param event The action event.
     */
    private void handleSettings(ActionEvent event) {
        toolBarController.openSettings();
    }

    /**
     * Handle the exit button
     */
    private void handleExitApp(ActionEvent actionEvent) {
        Platform.exit();
    }

}
