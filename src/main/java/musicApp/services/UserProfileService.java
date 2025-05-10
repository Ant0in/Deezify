package musicApp.services;

import musicApp.models.UserProfile;
import musicApp.repositories.JsonRepository;

import java.util.List;

public class UserProfileService {
    private final JsonRepository jsonRepository;

    public UserProfileService() {
        jsonRepository = new JsonRepository();
    }

    public void writeUserProfiles(List<UserProfile> userProfiles) {
        jsonRepository.writeUserProfiles(userProfiles);
    }

    public List<UserProfile> readUserProfiles() {
        return jsonRepository.readUserProfiles();
    }

    public void deleteUserProfile(UserProfile userProfile) {
        List<UserProfile> userProfiles = readUserProfiles();
        userProfiles.remove(userProfile);
        writeUserProfiles(userProfiles);
    }
}
