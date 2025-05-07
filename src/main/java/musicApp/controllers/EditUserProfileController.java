package musicApp.controllers;

import musicApp.models.UserProfile;
import musicApp.services.UserProfileService;
import musicApp.views.EditUserView;

import java.nio.file.Path;
import java.util.List;

/**
 * The type Playlist edit controller.
 */
public class EditUserProfileController extends ViewController<EditUserView> implements EditUserView.EditUserViewListener {

    private final UserProfile userProfile;
    private final boolean isCreation;
    private UserProfilesController userProfilesController;

    /**
     * Create a controller for creating a new userProfile
     */
    public EditUserProfileController(UserProfilesController _userProfilesController) {
        super(new EditUserView());
        init(_userProfilesController);
        userProfile = null;
        isCreation = true;
        view.setListener(this);
        initView("/fxml/EditUser.fxml");
    }

    /**
     * Create a controller for editing an existing userProfile
     *
     * @param _userProfile   The userProfile to edit
     */
    public EditUserProfileController(UserProfilesController _userController, UserProfile _userProfile) {
        super(new EditUserView());
        init(_userController);
        userProfile = _userProfile;
        isCreation = false;
        view.setListener(this);
        initView("/fxml/EditUser.fxml", true);
    }

    /**
     * Initialize the controller and part of the constructor.
     */
    private void init(UserProfilesController _userProfilesController) {
        if (userProfile != null) {
            view.populateFields(userProfile.getUsername(),
                                userProfile.getUserPicturePathToString(),
                                userProfile.getUserMusicPathToString());
        }
        userProfilesController = _userProfilesController;
    }

    public boolean isCreation() {
        return isCreation;
    }

    /**
     * Handle saving the playlist - either create a new one or update an existing one
     *
     * @param userName      The userProfile's name
     * @param imagePath The playlist image path
     */
    @Override
    public void handleSave(String userName, Path imagePath, Path musicPath) {
        if (userName == null || userName.isBlank()) {
            System.out.println("Name cannot be empty.");
            return;
        }

        if (imagePath == null || musicPath == null) {
            System.out.println("Image path or music path cannot be empty.");
            return;
        }

        UserProfileService userProfileService = new UserProfileService();
        List<UserProfile> allUserProfiles = userProfileService.readUsers();
        

        if (isCreation) {
            UserProfile newUserProfile = new UserProfile(userName, imagePath, musicPath);
            allUserProfiles.add(newUserProfile);
        } else {
            // Update the existing userProfile
            userProfile.setUsername(userName);
            userProfile.setUserPicturePath(imagePath);
            userProfile.setUserMusicPath(musicPath);

            // Find the userProfile in the list and update it
            for (int i = 0; i < allUserProfiles.size(); i++) {
                if (allUserProfiles.get(i).getUsername().equals(userProfile.getUsername())) {
                    allUserProfiles.set(i, userProfile);
                    break;
                }
            }
        }
        System.out.println("allUserProfiles: " + allUserProfiles);

        userProfileService.writeUser(allUserProfiles);
        userProfilesController.usersUpdate();
        handleClose();
    }


    public void handleClose() {
        view.close();
    }

    public void show() {
        view.show();
    }
}
