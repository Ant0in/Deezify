package musicApp.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.stage.Stage;
import musicApp.models.Settings;
import musicApp.models.User;
import musicApp.models.dtos.UserDTO;
import musicApp.services.UserService;
import musicApp.views.UsersView;

public class UsersController extends ViewController<UsersView> implements UsersView.UsersViewListener {

    private List<User> usersList;
    private EditUserController editController;
    private final MetaController metaController;
    private final Stage primaryStage;

    public UsersController(MetaController _metaController,Stage _primaryStage, List<User> _usersList) {
        super(new UsersView(_primaryStage));
        usersList = _usersList;
        primaryStage = _primaryStage;
        metaController = _metaController;
        view.setListener(this);
        initView("/fxml/Users.fxml");
    }

    public void onAddProfileClicked(){
        editController = new EditUserController(this);
        editController.show();
    }

    public List<UserDTO> getUsersDTOList() {
        List<UserDTO> usersDTOList = new ArrayList<>();
        for (User user : usersList) {
            usersDTOList.add(user.toDTO());
        }
        return usersDTOList;
    }

    public List<User> getUsersList() {
        return usersList;
    }

    public void show() {
        view.show();
    }

    public void usersUpdate(){
        usersList = new UserService().readUsers();
        view.refreshUI();
    }

    public void onUserSelected(User selectedUser) {
        Settings userSettings = new Settings(selectedUser); 
        try {
            view.close();
            metaController.loadPlayerWithUser(userSettings.toDTO()); 
            metaController.switchScene(MetaController.Scenes.MAINWINDOW);
        } catch (IOException e) {
            e.printStackTrace(); 
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

}
