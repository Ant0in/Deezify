package musicApp.controllers;

import musicApp.controllers.playlists.PlaylistNavigatorController;
import musicApp.models.Library;
import musicApp.models.User;
import musicApp.views.EditUserView;
import musicApp.views.playlists.EditPlaylistView;

import java.nio.file.Path;

/**
 * The type Playlist edit controller.
 */
public class EditUserController extends ViewController<EditUserView> implements EditUserView.EditUserViewListener {

    private final User user;
    private final boolean isCreation;

    /**
     * Create a controller for creating a new user
     */
    public EditUserController() {
        super(new EditUserView());
        user = null;
        isCreation = true;
        view.setListener(this);
        initView("/fxml/EditUser.fxml");
        init();
    }

    /**
     * Create a controller for editing an existing user
     *
     * @param _user   The user to edit
     */
    public EditUserController(User _user) {
        super(new EditUserView());
        user = _user;
        isCreation = false;
        view.setListener(this);
        initView("/fxml/EditUser.fxml", true);
        init();
    }

    /**
     * Initialize the controller and part of the constructor.
     */
    private void init(){
        if (user != null) {
            view.populateFields(user.getUsername(),
                                user.getProfilePicturePathToString(),
                                user.getUserMusicPathToString());
        }
    }

    public boolean isCreation() {
        return isCreation;
    }

    /**
     * Handle saving the playlist - either create a new one or update an existing one
     *
     * @param userName      The user's name
     * @param imagePath The playlist image path
     */
    public void handleSave(String userName, Path imagePath, Path musicPath) {
        //TODO
        handleClose();
    }

    public void handleClose() {
        view.close();
    }

    public void show() {
        view.show();
    }
}
