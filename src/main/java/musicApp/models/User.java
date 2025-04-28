package musicApp.models;

import java.nio.file.Path;

import musicApp.models.dtos.UserDTO;

public class User {
    private String username;
    private Path profilePicturePath;
    
    public User(String _username, Path _profilePicturePath) {
        username = _username;
        profilePicturePath = _profilePicturePath;
    }
    
    public void setUsername(String newUsername) {
        username = newUsername;
    }

    public String getUsername() {
        return username;
    }

    public void setProfilePicturePath(Path newProfilePicturePath) {
        profilePicturePath = newProfilePicturePath;
    }

    public Path getProfilePicturePath() {
        return profilePicturePath;
    }

    public UserDTO toDTO() {
        return new UserDTO(username, profilePicturePath);
    }
}
