package musicApp.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.stage.Stage;
import musicApp.models.UserProfile;
import musicApp.models.dtos.UserDTO;
import musicApp.services.UserProfileService;
import musicApp.views.UserProfilesView;

public class UserProfilesController extends ViewController<UserProfilesView> implements UserProfilesView.UsersViewListener {

    private List<UserProfile> usersList;
    private EditUserProfileController editController;
    private final MetaController metaController;
    private final Stage primaryStage;

    public UserProfilesController(MetaController _metaController, Stage _primaryStage, List<UserProfile> _usersList) {
        super(new UserProfilesView(_primaryStage));
        usersList = _usersList;
        primaryStage = _primaryStage;
        metaController = _metaController;
        view.setListener(this);
        initView("/fxml/Users.fxml");
    }

    public void onAddProfileClicked(){
        editController = new EditUserProfileController(this);
        editController.show();
    }

    public List<UserDTO> getUsersDTOList() {
        List<UserDTO> usersDTOList = new ArrayList<>();
        for (UserProfile userProfile : usersList) {
            usersDTOList.add(userProfile.toDTO());
        }
        return usersDTOList;
    }

    public List<UserProfile> getUsersList() {
        return usersList;
    }

    public void show() {
        view.show();
    }

    public void usersUpdate(){
        usersList = new UserProfileService().readUsers();
        view.refreshUI();
    }

    public void onUserSelected(UserProfile selectedUserProfile) {
        // TODO UserProfilesController should be able to send a message to the MetaController to link the selected user profile to the Settings model
        //Settings userSettings = new Settings(selectedUserProfile);
        try {
            view.close();
            metaController.loadPlayerWithUser(selectedUserProfile);
            metaController.switchScene(MetaController.Scenes.MAINWINDOW);
        } catch (IOException e) {
            e.printStackTrace(); 
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

}
