package musicApp.services;

import musicApp.models.User;
import musicApp.repositories.JsonRepository;

import java.util.List;

public class UserService {
    private final JsonRepository jsonRepository;

    public UserService() {
        jsonRepository = new JsonRepository();
    }

    public void writeUser(List<User> users) {
        jsonRepository.writeUsers(users);
    }

    public List<User> readUsers() {
        return jsonRepository.readUsers();
    }
}
