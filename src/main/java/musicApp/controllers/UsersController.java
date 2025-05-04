package musicApp.controllers;

import java.util.ArrayList;
import java.util.List;

import javafx.stage.Stage;
import musicApp.models.User;
import musicApp.models.dtos.UserDTO;
import musicApp.services.UserService;
import musicApp.views.UsersView;

public class UsersController extends ViewController<UsersView> implements UsersView.UsersViewListener {

    private List<User> usersList;
    private EditUserController editController;

    public UsersController(Stage primaryStage, List<User> _usersList) {
        super(new UsersView(primaryStage));
        usersList = _usersList;
        view.setListener(this);
        initView("/fxml/Users.fxml");
    }

    public void onAddProfileClicked(){
        editController = new EditUserController(this);
        editController.show();
    }

    public List<UserDTO> getUsersList() {
        List<UserDTO> usersDTOList = new ArrayList<>();
        for (User user : usersList) {
            usersDTOList.add(user.toDTO());
        }
        return usersDTOList;
    }

    public void show() {
        view.show();
    }

    public void usersUpdate(){
        usersList = new UserService().readUsers();
        view.refreshUI();
    }
}
