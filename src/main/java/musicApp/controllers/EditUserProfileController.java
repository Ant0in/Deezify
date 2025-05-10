package musicApp.controllers;

import musicApp.controllers.settings.EqualizerController;
import musicApp.models.Equalizer;
import musicApp.models.UserProfile;
import musicApp.services.UserProfileService;
import musicApp.views.EditUserProfileView;

import java.nio.file.Path;
import java.util.List;

import javafx.scene.control.Alert;

/**
 * The type Playlist edit controller.
 */
public class EditUserProfileController extends ViewController<EditUserProfileView> implements EditUserProfileView.EditUserProfileViewListener {

    private final UserProfile userProfile;
    private final boolean isCreation;
    private EditUserProfileControllerListener listener;
    private final EqualizerController equalizerController;

    public interface EditUserProfileControllerListener {
        void usersUpdate();
    }

    /**
     * Create a controller for creating a new userProfile
     */
    public EditUserProfileController(EditUserProfileControllerListener _listener) {
        super(new EditUserProfileView());
        userProfile = null;
        isCreation = true;
        equalizerController = new EqualizerController(this, new Equalizer());
        view.setListener(this);
        initView("/fxml/EditUserProfile.fxml");
        init(_listener);        
    }

    /**
     * Create a controller for editing an existing userProfile
     *
     * @param _userProfile   The userProfile to edit
     */
    public EditUserProfileController(EditUserProfileControllerListener _listener, UserProfile _userProfile) {
        super(new EditUserProfileView());
        userProfile = _userProfile;
        isCreation = false;
        equalizerController = new EqualizerController(this, _userProfile.getEqualizer());
        view.setListener(this);
        initView("/fxml/EditUserProfile.fxml");
        init(_listener);     
    }

    /**
     * Initialize the controller and part of the constructor.
     */
    private void init(EditUserProfileControllerListener _listener) {
        if (userProfile != null) {
            view.populateFields(userProfile.getUsername(),
                                userProfile.getUserPicturePathToString(),
                                userProfile.getUserMusicPathToString());
        }
        listener = _listener;
        view.show();
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
    public void handleSave(String userName,double balance, double crossfade, Path imagePath, Path musicPath) {
        if (userName == null || userName.isBlank()) {
            alertService.showAlert("User name cannot be empty", Alert.AlertType.WARNING);
            return;
        }
        if (musicPath == null) {
            alertService.showAlert("Music path cannot be empty", Alert.AlertType.WARNING);
            return;
        }
        UserProfileService userProfileService = new UserProfileService();
        List<UserProfile> allUserProfiles = userProfileService.readUserProfiles();

        if (isCreation) {
            UserProfile newUserProfile = new UserProfile(userName, imagePath, musicPath);
            allUserProfiles.add(newUserProfile);
        } else {
            // Update the existing userProfile
            for (UserProfile userProfile : allUserProfiles) {
                if (userProfile.getUsername().equals(this.userProfile.getUsername())) {
                    userProfile.setUsername(userName);
                    userProfile.setUserPicturePath(imagePath);
                    userProfile.setUserMusicPath(musicPath);
                }
            }
    
        updateEqualizer();
        setBalance(balance);
        setCrossfadeDuration(crossfade);
        }
        userProfileService.writeUserProfiles(allUserProfiles);
        listener.usersUpdate();
        handleClose();
    }


    public void handleClose() {
        view.close();
    }

    public void handleCancel() {
        equalizerController.handleCancel();
        view.close();
    }

    public void show() {
        view.show();
    }
    

    @Override
    public double getCrossfadeDuration() {
        if (userProfile == null) {
            return 0.0;
        }
        return userProfile.getCrossfadeDuration();
    }

    private void setCrossfadeDuration(double crossfadeDuration) {
        userProfile.setCrossfadeDuration(crossfadeDuration);
    }    

    /**
     * Open equalizer.
     */
    public void handleOpenEqualizer() {
        handleClose();
        equalizerController.show();
    }
    

    /**
     * Updates the values of the equalizer with the values of sliders and changes the settings
     */
    private void updateEqualizer() {
        equalizerController.update();
    }

    /**
     * Get the balance of the application.
     *
     * @return The balance of the application.
     */
    public double getBalance() {
        if (userProfile == null) {
            return 0.0;
        }
        return userProfile.getBalance();
    }

    /**
     * Update the balance of the application.
     *
     * @param balance The new balance.
     */
    private void setBalance(double balance) {
        userProfile.setBalance(balance);
    }
}
