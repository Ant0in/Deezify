package musicApp.controllers;

import java.io.IOException;
import java.util.List;

import javafx.stage.Stage;
import musicApp.models.UserProfile;
import musicApp.services.UserProfileService;
import musicApp.views.UserProfileSelectionView;

public class UserProfileSelectionController extends ViewController<UserProfileSelectionView> implements UserProfileSelectionView.UserProfileSelectionViewListener {

    private List<UserProfile> usersList;
    private final MetaController metaController;
    private final Stage primaryStage;

    public UserProfileSelectionController(MetaController _metaController, Stage _primaryStage, List<UserProfile> _usersList) {
        super(new UserProfileSelectionView(_primaryStage));
        usersList = _usersList;
        primaryStage = _primaryStage;
        metaController = _metaController;
        view.setListener(this);
        initView("/fxml/UserProfileSelection.fxml");
    }

    public void onAddProfileClicked(){
        new EditUserProfileController(metaController);
    }

    public List<UserProfile> getUsersList() {
        return usersList;
    }

    public void show() {
        view.show();
    }

    public void usersUpdate(){
        usersList = new UserProfileService().readUserProfiles();
        view.refreshUI();
    }

    public void onUserSelected(UserProfile selectedUserProfile) {
        view.close();
        metaController.loadPlayerWithUser(selectedUserProfile);
        metaController.switchScene(MetaController.Scenes.MAINWINDOW);

    }

    /**
     * Delete a user profile and update the UI
     *
     * @param userProfile The user profile to delete
     */
    public void onDeleteUserProfile(UserProfile userProfile) {
        UserProfileService userProfileService = new UserProfileService();
        userProfileService.deleteUserProfile(userProfile);
        usersUpdate();
    }

}
