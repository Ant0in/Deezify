package musicApp.views;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import musicApp.services.LanguageService;

/**
 * PlayerView
 * Class that represents the view of the music player.
 */
public class PlayerView extends View {

    private PlayerViewListener listener;
    private final Stage primaryStage;

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
    public PlayerView(Stage _primaryStage) {
        super();
        primaryStage = _primaryStage;
        xOffset = 0;
        yOffset = 0;
    }

    /**
     * Listener interface for handling user actions from the controller.
     */
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
     * @param newListener the listener
     */
    public void setListener(PlayerViewListener newListener) {
        listener = newListener;
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
     */
    public void enableDoubleClickToGrow() {
        labelContainer.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                primaryStage.setFullScreen(true);
            }
        });
    }

    /**
     * Setup the window close handler.
     *
     */
    private void setupWindowCloseHandler() {
        primaryStage.setOnCloseRequest(_ -> {
            listener.close();
        });
    }

    /**
     * Enable drag of the window.
     */
    private void enableDrag() {
        labelContainer.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        labelContainer.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });
    }

    /**
     * Get the title of the application.
     *
     * @return The title of the application.
     */
    public String getTitle() {
        return LanguageService.getInstance().get("app.title");
    }

    /**
     * Show the stage.
     *
     */
    public void show() {
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.setTitle(getTitle());
        enableDrag();
        enableDoubleClickToGrow();
        setupWindowCloseHandler();
        primaryStage.show();
    }

    /**
     * Refresh the UI.
     */
    public void refreshUI() {
        Stage stage = (Stage) scene.getWindow();
        stage.setTitle(LanguageService.getInstance().get("app.title"));
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
