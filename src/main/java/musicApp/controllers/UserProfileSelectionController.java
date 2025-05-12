package musicApp.controllers;

import javafx.stage.Stage;
import musicApp.enums.Language;
import musicApp.exceptions.SettingsFilesException;
import musicApp.models.UserProfile;
import musicApp.services.LanguageService;
import musicApp.services.UserProfileService;
import musicApp.views.UserProfileSelectionView;

import java.util.List;

public class UserProfileSelectionController extends ViewController<UserProfileSelectionView> implements UserProfileSelectionView.UserProfileSelectionViewListener {

    private List<UserProfile> usersList;
    private final MetaController metaController;

    /**
     * * The constructor for the UserProfileSelectionController class.
     * @param _metaController MetaController instance to handle the main application logic
     * @param _primaryStage Primary stage of the application
     * @param _usersList List of user profiles to be displayed
     */
    public UserProfileSelectionController(MetaController _metaController, Stage _primaryStage, List<UserProfile> _usersList) {
        super(new UserProfileSelectionView(_primaryStage));
        usersList = _usersList;
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
        try {
            usersList = new UserProfileService().readUserProfiles();
            view.refreshUI();
        }  catch (SettingsFilesException e) {
            alertService.showFatalErrorAlert("Error loading settings", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * * This method is called when the user selects a user profile.
     * @param selectedUserProfile The selected user profile
     */
    public void onUserSelected(UserProfile selectedUserProfile) {
        view.close();
        metaController.loadPlayerWithUser(selectedUserProfile);
        metaController.switchScene(MetaController.Scenes.MAINWINDOW);
        LanguageService.getInstance().setLanguage(Language.fromDisplayName(selectedUserProfile.getLanguage()));
        metaController.refreshUI();
    }

    /**
     * Delete a user profile and update the UI
     *
     * @param userProfile The user profile to delete
     */
    public void onDeleteUserProfile(UserProfile userProfile) {
        try {
            UserProfileService userProfileService = new UserProfileService();
            userProfileService.deleteUserProfile(userProfile);
            usersUpdate();
        } catch (SettingsFilesException e) {
            alertService.showFatalErrorAlert("Error loading settings", e);
            throw new RuntimeException(e);
        }
    }
}
