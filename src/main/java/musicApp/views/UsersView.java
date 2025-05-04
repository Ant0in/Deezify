package musicApp.views;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
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
                VBox userBox = new VBox();
                userBox.setPrefWidth(150);
                userBox.setPrefHeight(220);
                userBox.setSpacing(10);
                userBox.setStyle("""
                    -fx-background-color: white;
                    -fx-background-radius: 10px;
                    -fx-padding: 10px;
                    -fx-alignment: center;
                    -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0.5, 0, 2);
                    -fx-cursor: hand;
                """);

                // Create a new Image object with the user's profile picture path
                Image image;
                try {
                    image = new Image(user.getProfilePicturePath().toUri().toString(), true);
                    System.out.println(user.getProfilePicturePath());
                    if (image.isError()) throw new Exception("Image failed");
                } catch (Exception e) {
                    image = new Image(getClass().getResource("/images/default_account.png").toExternalForm());
                }

                ImageView profileImageView = new ImageView(image);
                profileImageView.setFitWidth(200);
                profileImageView.setFitHeight(200);
                profileImageView.setStyle("-fx-background-radius: 100px;");

                Text usernameText = new Text(user.getUsername());
                usernameText.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

                userBox.getChildren().addAll(profileImageView, usernameText);
                userAccounts.getChildren().add(userBox);
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

    public void refreshUI() {
        userAccounts.getChildren().clear();
        initUserAccounts();
    }
}
