package musicApp.models.dtos;

import java.nio.file.Path;

public class UserDTO {
    private final String username;
    private final Path profilePicturePath;

    public UserDTO(String _username, Path _profilePicturePath) {
        username = _username;
        profilePicturePath = _profilePicturePath;
    }

    public String getUsername() {
        return username;
    }

    public Path getProfilePicturePath() {
        return profilePicturePath;
    }
    
}
