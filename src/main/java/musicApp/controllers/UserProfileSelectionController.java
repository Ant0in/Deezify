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
    /**
     * * The constructor for the UserProfileSelectionController class.
     * @param _metaController
     * @param _primaryStage
     * @param _usersList
     */
    public UserProfileSelectionController(MetaController _metaController, Stage _primaryStage, List<UserProfile> _usersList) {
        super(new UserProfileSelectionView(_primaryStage));
        usersList = _usersList;
        primaryStage = _primaryStage;
        metaController = _metaController;
        view.setListener(this);
        initView("/fxml/UserProfileSelection.fxml");
    }

    /**
     * * This method is called when the user clicks the "Add Profile" button.
     */
    public void onAddProfileClicked(){
        new EditUserProfileController(metaController);
    }

    /**
     * * This method returns the list of user profiles.
     */
    public List<UserProfile> getUsersList() {
        return usersList;
    }

    /**
     * * This method is called to show the user profile selection view.
     */
    public void show() {
        view.show();
    }

    /**
     * * This method updates the list of user profiles and refreshes the UI.
     */
    public void usersUpdate(){
        usersList = new UserProfileService().readUserProfiles();
        view.refreshUI();
    }

    /**
     * * This method is called when the user selects a user profile.
     * @param selectedUserProfile The selected user profile
     */
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
