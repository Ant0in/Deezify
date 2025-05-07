package musicApp.services;

import musicApp.models.UserProfile;
import musicApp.repositories.JsonRepository;

import java.util.List;

public class UserProfileService {
    private final JsonRepository jsonRepository;

    public UserProfileService() {
        jsonRepository = new JsonRepository();
    }

    public void writeUser(List<UserProfile> userProfiles) {
        jsonRepository.writeUserProfiles(userProfiles);
    }

    public List<UserProfile> readUsers() {
        return jsonRepository.readUserProfiles();
    }
}
