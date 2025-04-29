package musicApp.views;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import musicApp.models.dtos.UserDTO;

import java.util.ArrayList;
import java.util.List;

public class UsersView extends View {
    private UsersViewListener listener;
    private final Stage primaryStage;

     @FXML
     private FlowPane userAccounts;

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
        List<UserDTO> getUsersList();
    }

    @Override
    public void init() {
        primaryStage.setScene(scene);
        // Ici tu peux ajouter les listeners sur tes boutons
        initListeners();
        initUserAccounts();
    }

    private void initListeners() {
        addProfileButton.setOnAction(e -> {
            if (listener != null) {
                listener.onAddProfileClicked();
            }
        });
    }

    private void initUserAccounts() {
        List<UserDTO> usersDTOList = listener.getUsersList();
        if (usersDTOList != null) {
            usersDTOList.forEach(user -> {
                // Create a new VBox for each user
                VBox userBox = new VBox();
    //            userBox.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10px; -fx-spacing: 10px;");
                userBox.setPrefWidth(200);
                userBox.setPrefHeight(220);

                // Create a Text node for the username
                Text usernameText = new Text(user.getUsername());
                usernameText.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

                // Create a ImageView for the profile picture
                // if user.getProfilePicturePath() is null, use a default image
                ImageView profileImageView = new ImageView(user.getProfilePicturePath() != null ? user.getProfilePicturePath().toString() : "/images/default_account.png");
                profileImageView.setFitWidth(200);
                profileImageView.setFitHeight(200);

                // Add the Text node to the VBox
                userBox.getChildren().add(usernameText);
                // Add the ImageView to the VBox
                userBox.getChildren().add(profileImageView);

                // Add the VBox to the FlowPane
                userAccounts.getChildren().addFirst(userBox);
        });
        }
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
