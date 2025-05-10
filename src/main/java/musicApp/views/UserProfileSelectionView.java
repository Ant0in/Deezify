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
    private ToggleButton toggleManageProfilesButton;

    public UserProfileSelectionView(Stage _primaryStage) {
        super();
        primaryStage = _primaryStage;
    }

    public void close() {
        primaryStage.close();
    }

    public interface UserProfileSelectionViewListener {
        void onAddProfileClicked();
        List<UserProfile> getUsersList();
        void onUserSelected(UserProfile userProfile);
        void onDeleteUserProfile(UserProfile userProfile);
    }

    @Override
    public void init() {
        primaryStage.setScene(scene);
        initButtons();
        initUserAccounts();
        initTranslation();
    }

    private void initTranslation() {
        addProfileButton.setText(LanguageService.getInstance().get("user_profile.add"));
        toggleManageProfilesButton.setText(LanguageService.getInstance().get("user_profile.manage"));
        titleLabel.setText(LanguageService.getInstance().get("user_profile.select"));

    }

    private void initButtons() {
        addProfileButton.setOnAction(_ -> {
            if (listener != null) {
                listener.onAddProfileClicked();
            }
        });
        toggleManageProfilesButton.setOnAction(_ -> refreshUI());

    }

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
        Text usernameText = new Text(user.getUsername());

        userBox.getStyleClass().add("userProfile-vbox");
        profileImageView.getStyleClass().add("userProfile-vbox-img");
        usernameText.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        userBox.getChildren().addAll(profileImageView, usernameText);

        return userBox;
    }

    private void initUserAccounts() {
        List<UserProfile> usersList = listener.getUsersList();
        if (usersList != null) {
            usersList.forEach(user -> {
                VBox userBox = initUserVBox(user);
                if (toggleManageProfilesButton.isSelected()) {
                    userBox.getStyleClass().add("deletable-userProfile-vbox");
                    userBox.setOnMouseClicked(_ -> listener.onDeleteUserProfile(user));
                } else {
                    userBox.setOnMouseClicked(_ -> listener.onUserSelected(user));
                }
                userAccounts.getChildren().add(userBox);
            });
        }
    }


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

    public void refreshUI() {
        userAccounts.getChildren().clear();
        initUserAccounts();
    }

    @Override
    public void refreshTranslation() {
        LanguageService languageService = LanguageService.getInstance();
        titleLabel.setText(languageService.get("user_profile.select"));
        addProfileButton.setText(languageService.get("user_profile.add"));
        toggleManageProfilesButton.setText(languageService.get("user_profile.manage"));
    }
}
