package musicApp.views;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class UsersView extends View {
    private UsersViewListener listener;
    private final Stage primaryStage;

    // @FXML
    // private Text text;

    // @FXML
    // private FlowPane profilesPane;

    @FXML
    private Button addProfileButton;

    public UsersView(Stage _primaryStage) {
        super();
        primaryStage = _primaryStage;
    }

    public void close() {
        primaryStage.close();
    }

    public interface UsersViewListener {
        void onAddProfileClicked();
    }

    @Override
    public void init() {
        primaryStage.setScene(scene);
        // Ici tu peux ajouter les listeners sur tes boutons
        addProfileButton.setOnAction(e -> {
            if (listener != null) {
                listener.onAddProfileClicked();
            }
        });
    }

    public void setListener(UsersViewListener newListener) {
        listener = newListener;
    }

    // public FlowPane getProfilesPane() {
        // return profilesPane;
    // }
    
    /**
     * Show the stage.
     *
     */
    public void show() {
        primaryStage.show();
    }
}
