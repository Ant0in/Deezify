package MusicApp.Views;

import MusicApp.Utils.LanguageManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import MusicApp.Controllers.PlayerController;
import javafx.stage.StageStyle;
import javafx.scene.paint.Color;

/**
 * PlayerView
 * Class that represents the view of the music player.
 */
public class PlayerView extends View<PlayerView,PlayerController> {
    @FXML
    private Pane controls;
    @FXML
    private HBox playerContainer;
    @FXML
    private BorderPane labelContainer;

    /* To enable drag */
    private double xOffset = 0;
    private double yOffset = 0;

    /**
     * Instantiates a new Player view.
     */
    public PlayerView()  {
    }


    @Override
    public void init(){
        initPanes();
        initBindings();
    }

    private void initPanes(){
        initControlPanel();
        initToolBar();
        initPlayList();
        initQueue();
    }

    private void initControlPanel(){
        controls = viewController.getControlPanelRoot();
        labelContainer.setBottom(controls);
    }

    private void initToolBar(){
        Pane toolBarPane = viewController.getToolBarRoot();
        labelContainer.setTop(toolBarPane);
    }


    private void initPlayList(){
        playerContainer.getChildren().set(0,viewController.getPlayListRoot());
    }

    private void initQueue(){
        playerContainer.getChildren().set(1,viewController.getQueueRoot());
    }


    /**
     * Initialize the bindings between the view and the controller.
     */
    private void initBindings() {
        bindPlayingSongAnchor();
    }


    /**
     * Bind the visibility of the playing song anchor (the controls at the bottom).
     */
    private void bindPlayingSongAnchor(){
        controls.setVisible(true);
    }


    /**
     * Enable double click to grow (fullscreen)
     *
     * @param stage the stage
     */
    public void enableDoubleClickToGrow(Stage stage){
        labelContainer.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                stage.setFullScreen(true);
            }
        });
    }

    /**
     * Setup the window close handler.
     * @param stage The stage to setup the handler for.
     */
    private void setupWindowCloseHandler(Stage stage) {
        stage.setOnCloseRequest(_ -> {
            viewController.close();
            Platform.exit();
        });
    }

    /**
     * Enable drag of the window.
     */
    private void enableDrag(Stage stage) {
        labelContainer.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        labelContainer.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    /**
     * Get the title of the application.
     *
     * @return The title of the application.
     */
    public String getTitle() {
        return LanguageManager.get("app.title");
    }

    /**
     * Show the stage.
     *
     * @param stage The stage to show.
     */
    public void show(Stage stage) {
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(this.scene);
        stage.setTitle(getTitle());
        enableDrag(stage);
        enableDoubleClickToGrow(stage);
        setupWindowCloseHandler(stage);
        stage.show();
    }

    /**
     * Refresh the UI.
     */
    public void refreshUI() {
        Stage stage = (Stage) scene.getWindow();
        stage.setTitle(LanguageManager.get("app.title"));
    }
}
