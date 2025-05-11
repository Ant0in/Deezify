package musicApp.views;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import musicApp.models.UserProfile;
import musicApp.services.LanguageService;

import java.util.List;
import java.util.Objects;

public class UserProfileSelectionView extends View {
    private UserProfileSelectionViewListener listener;
    private final Stage primaryStage;

    @FXML
    private FlowPane userAccounts;
    @FXML
    private Label titleLabel;
    @FXML
    private Button addProfileButton;
    @FXML
    private ToggleButton toggleDeleteProfilesButton;

    /**
     * * The constructor for the UserProfileSelectionView class.
     * @param _primaryStage
     */
    public UserProfileSelectionView(Stage _primaryStage) {
        super();
        primaryStage = _primaryStage;
    }

    /**
     * This method close the stage.
     */
    public void close() {
        primaryStage.close();
    }
    
    /**
     * The listener interface for receiving user profile selection events.
     * Implement this interface to handle user actions 
     */
    public interface UserProfileSelectionViewListener {
        void onAddProfileClicked();
        List<UserProfile> getUsersList();
        void onUserSelected(UserProfile userProfile);
        void onDeleteUserProfile(UserProfile userProfile);
    }

    /**
     * This method initializes the view.
     * It sets the scene and initializes the buttons and user accounts.
     */
    @Override
    public void init() {
        primaryStage.setScene(scene);
        initButtons();
        initUserAccounts();
        initTranslation();
    }

    /**
     * This method initializes the translation for the UI elements.
     */
    private void initTranslation() {
        addProfileButton.setText(LanguageService.getInstance().get("user_profile.add"));
        toggleDeleteProfilesButton.setText(LanguageService.getInstance().get("user_profile.manage"));
        titleLabel.setText(LanguageService.getInstance().get("user_profile.select"));

    }

    /**
     * This method initializes the buttons and their actions.
     */
    private void initButtons() {
        addProfileButton.setOnAction(_ -> {
            if (listener != null) {
                listener.onAddProfileClicked();
            }
        });
        toggleDeleteProfilesButton.setOnAction(_ -> refreshUI());

    }

    /**
     * This method initializes the user accounts in the UI.
     * It creates a VBox for each user profile and adds it to the FlowPane.
     */
    private VBox initUserVBox(UserProfile user) {
        VBox userBox = new VBox();
        Image image;
        try {
            image = new Image(user.getUserPicturePath().toUri().toString(), true);
            if (image.isError()) throw new Exception("Image failed");
        } catch (Exception e) {
            image = new Image(Objects.requireNonNull(getClass().getResource("/images/default_account.png")).toExternalForm());
        }
        ImageView profileImageView = new ImageView(image);
        profileImageView.setFitWidth(200);
        profileImageView.setFitHeight(200);
        profileImageView.setStyle("-fx-background-radius: 100px;");

        Text usernameText = new Text(user.getUsername());

        userBox.getStyleClass().add("userProfile-vbox");
        profileImageView.getStyleClass().add("userProfile-vbox-img");
        usernameText.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        userBox.getChildren().addAll(profileImageView, usernameText);

        return userBox;
    }

    /**
     * This method initializes the user accounts in the UI.
     * It creates a VBox for each user profile and adds it to the FlowPane.
     */
    private void initUserAccounts() {
        List<UserProfile> usersList = listener.getUsersList();
        if (usersList != null) {
            usersList.forEach(user -> {
                VBox userBox = initUserVBox(user);
                if (toggleDeleteProfilesButton.isSelected()) {
                    userBox.getStyleClass().add("deletable-userProfile-vbox");
                    userBox.setOnMouseClicked(_ -> listener.onDeleteUserProfile(user));
                } else {
                    userBox.setOnMouseClicked(_ -> listener.onUserSelected(user));
                }
                userAccounts.getChildren().add(userBox);
            });
        }
    }

    /**
     * This method sets the listener for the view.
     * @param newListener The new listener to set
     */
    public void setListener(UserProfileSelectionViewListener newListener) {
        listener = newListener;
    }

    /**
     * Show the stage.
     *
     */
    public void show() {
        primaryStage.show();
    }

    /**
     * This method refreshes the UI by clearing the user accounts and re-initializing them.
     */
    public void refreshUI() {
        userAccounts.getChildren().clear();
        initUserAccounts();
    }

    /**
     * This method refreshes the translation for the UI elements.
     */
    @Override
    public void refreshTranslation() {
        LanguageService languageService = LanguageService.getInstance();
        titleLabel.setText(languageService.get("user_profile.select"));
        addProfileButton.setText(languageService.get("user_profile.add"));
        toggleDeleteProfilesButton.setText(languageService.get("user_profile.manage"));
    }
}
