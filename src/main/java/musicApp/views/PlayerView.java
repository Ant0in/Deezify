package musicApp.views;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import musicApp.controllers.PlayerController;
import musicApp.utils.LanguageManager;
import javafx.stage.StageStyle;
import javafx.scene.paint.Color;
import javafx.scene.layout.Priority;

/**
 * PlayerView
 * Class that represents the view of the music player.
 */
public class PlayerView extends BaseView {
    private PlayerViewListener listener;
    @FXML
    private Pane controls;
    @FXML
    private HBox playerContainer;
    @FXML
    private BorderPane labelContainer;

    /* To enable drag */
    private double xOffset;
    private double yOffset;

    /**
     * Instantiates a new Player view.
     */
    public PlayerView() {
        xOffset = 0;
        yOffset = 0;
    }

    public interface PlayerViewListener {
        void close();
        Pane getControlPanelRoot();
        Pane getToolBarRoot();
        Pane getMainLibraryRoot();
        Pane getPlaylistNavigatorRoot();
        Pane getQueueRoot();
        Pane getLyricsRoot();
    }

    /**
     * Sets listener.
     *
     * @param listener the listener
     */
    public void setListener(PlayerViewListener listener) {
        this.listener = listener;
    }


    @Override
    public void init() {
        initPanes();
        initBindings();
    }

    private void initPanes() {
        initControlPanel();
        initToolBar();
        initMainLibrary();
        initQueue();
        initPlaylistNavigator();
    }

    private void initControlPanel() {
        controls = listener.getControlPanelRoot();
        labelContainer.setBottom(controls);
    }

    private void initToolBar() {
        Pane toolBarPane = listener.getToolBarRoot();
        labelContainer.setTop(toolBarPane);
    }


    private void initMainLibrary() {
        playerContainer.getChildren().set(0, listener.getMainLibraryRoot());
    }

    private void initPlaylistNavigator() {
        Pane playListsPane = listener.getPlaylistNavigatorRoot();
        labelContainer.setLeft(playListsPane);
    }

    private void initQueue() {
        playerContainer.getChildren().set(1, listener.getQueueRoot());
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
    private void bindPlayingSongAnchor() {
        controls.setVisible(true);
    }


    /**
     * Enable double click to grow (fullscreen)
     *
     * @param stage the stage
     */
    public void enableDoubleClickToGrow(Stage stage) {
        labelContainer.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                stage.setFullScreen(true);
            }
        });
    }

    /**
     * Setup the window close handler.
     *
     * @param stage The stage to setup the handler for.
     */
    private void setupWindowCloseHandler(Stage stage) {
        stage.setOnCloseRequest(_ -> {
            listener.close();
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
        return LanguageManager.getInstance().get("app.title");
    }

    /**
     * Show the stage.
     *
     * @param stage The stage to show.
     */
    public void show(Stage stage) {
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
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
        stage.setTitle(LanguageManager.getInstance().get("app.title"));
    }

    /**
     * Toggle lyrics.
     *
     * @param show the show
     */
    public void toggleLyrics(boolean show) {
        if (show) {
            Pane lyricsPane = listener.getLyricsRoot();
            HBox.setHgrow(lyricsPane, Priority.ALWAYS);
            playerContainer.getChildren().set(0, lyricsPane);
            playerContainer.applyCss();

        } else {
            Pane libraryPane = listener.getMainLibraryRoot();
            HBox.setHgrow(libraryPane, Priority.ALWAYS);
            playerContainer.getChildren().set(0, libraryPane);
        }
    }

}
