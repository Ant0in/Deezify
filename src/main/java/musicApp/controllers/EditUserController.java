package musicApp.controllers;

import musicApp.controllers.playlists.PlaylistNavigatorController;
import musicApp.models.Library;
import musicApp.models.User;
import musicApp.services.AlertService;
import musicApp.services.UserService;
import musicApp.views.EditUserView;
import musicApp.views.playlists.EditPlaylistView;

import java.nio.file.Path;
import java.util.List;

/**
 * The type Playlist edit controller.
 */
public class EditUserController extends ViewController<EditUserView> implements EditUserView.EditUserViewListener {

    private final User user;
    private final boolean isCreation;
    private UsersController usersController;

    /**
     * Create a controller for creating a new user
     */
    public EditUserController(UsersController _usersController) {
        super(new EditUserView());
        init(_usersController);
        user = null;
        isCreation = true;
        view.setListener(this);
        initView("/fxml/EditUser.fxml");
    }

    /**
     * Create a controller for editing an existing user
     *
     * @param _user   The user to edit
     */
    public EditUserController(UsersController _userController, User _user) {
        super(new EditUserView());
        init(_userController);
        user = _user;
        isCreation = false;
        view.setListener(this);
        initView("/fxml/EditUser.fxml", true);
    }

    /**
     * Initialize the controller and part of the constructor.
     */
    private void init(UsersController _usersController) {
        if (user != null) {
            view.populateFields(user.getUsername(),
                                user.getUserPicturePathToString(),
                                user.getUserMusicPathToString());
        }
        usersController = _usersController;
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

        UserService userService = new UserService();
        List<User> allUsers = userService.readUsers();
        

        if (isCreation) {
            User newUser = new User(userName, imagePath, musicPath);
            allUsers.add(newUser);
        } else {
            // Update the existing user
            user.setUsername(userName);
            user.setUserPicturePath(imagePath);
            user.setUserMusicPath(musicPath);

            // Find the user in the list and update it
            for (int i = 0; i < allUsers.size(); i++) {
                if (allUsers.get(i).getUsername().equals(user.getUsername())) {
                    allUsers.set(i, user);
                    break;
                }
            }
        }
        System.out.println("allUsers: " + allUsers);

        userService.writeUser(allUsers);
        usersController.usersUpdate();
        handleClose();
    }


    public void handleClose() {
        view.close();
    }

    public void show() {
        view.show();
    }
}
