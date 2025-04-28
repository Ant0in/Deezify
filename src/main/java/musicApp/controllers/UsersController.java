package musicApp.controllers;

import java.util.ArrayList;

import javafx.stage.Stage;
import musicApp.models.User;
import musicApp.models.dtos.UserDTO;
import musicApp.views.UsersView;

public class UsersController extends ViewController<UsersView> implements UsersView.UsersViewListener {

    private ArrayList<User> usersList;

    public UsersController(Stage primaryStage) {
        super(new UsersView(primaryStage));
        usersList = new ArrayList<>();
        view.setListener(this);
        initView("/fxml/Users.fxml");
        
    }

    public void onAddProfileClicked(){

    }

    public void show() {
        view.show();
    }

    public ArrayList<UserDTO> getUsersList() {
        ArrayList<UserDTO> usersDTOList = new ArrayList<>();
        for (User user : usersList) {
            usersDTOList.add(user.toDTO());
        }
        return usersDTOList;
    }
    
}
