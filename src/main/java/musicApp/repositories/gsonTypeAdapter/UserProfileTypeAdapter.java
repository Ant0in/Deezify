package musicApp.repositories.gsonTypeAdapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.JsonReader;

import java.nio.file.Path;
import java.nio.file.Paths;

import musicApp.models.Equalizer;
import musicApp.models.UserProfile;

import java.io.IOException;

public class UserProfileTypeAdapter extends TypeAdapter<UserProfile> {
    /**
     * Writes the UserProfile object to JSON format.
     *
     * @param out      The JsonWriter to write to.
     * @param userProfile The UserProfile object to write.
     * @throws IOException If writing fails.
     */
    @Override
    public void write(JsonWriter out, UserProfile userProfile) throws IOException {
        out.beginObject();

        out.name("username").value(userProfile.getUsername());

        // Profile picture path (nullable)
        out.name("userPicturePath");
        Path picturePath = userProfile.getUserPicturePath();
        out.value(picturePath != null ? picturePath.toString() : "");

        out.name("balance").value(userProfile.getBalance());

        // Equalizer bands
        out.name("equalizerBands");
        out.beginArray();
        for (Double band : userProfile.getEqualizerBands()) {
            out.value(band);
        }
        out.endArray();

        // Crossfade duration
        out.name("crossfadeDuration").value(userProfile.getCrossfadeDuration());

        // Music path (nullable)
        out.name("userMusicPath");
        Path musicPath = userProfile.getUserMusicPathToString() != null
                ? userProfile.getUserMusicPath()
                : null;
        out.value(musicPath != null ? musicPath.toString() : "");

        out.endObject();
    }


    /**
     * Reads a UserProfile object from JSON format.
     *
     * @param in The JsonReader to read from.
     * @return The UserProfile object read from JSON.
     * @throws IOException If reading fails.
     */
    @Override
    public UserProfile read(JsonReader in) throws IOException {
        String username = null;
        Path userPicturePath = null;
        Path userMusicPath = null;
        double balance = 0;
        double crossfadeDuration = 0.0;
        Equalizer equalizer = new Equalizer();

        in.beginObject();
        while (in.hasNext()) {
            String fieldName = in.nextName();
            switch (fieldName) {
                case "username" -> username = in.nextString();
                case "userPicturePath" -> {
                    String rawPath = in.nextString();
                    System.out.println("[UserProfileTypeAdapter] rawPath: " + rawPath);
                    userPicturePath = (rawPath != null && !rawPath.isBlank())
                            ? Paths.get(rawPath)
                            : null; 
                }
                case "balance" -> balance = in.nextDouble();
                case "equalizerBands" -> {
                    in.beginArray();
                    int bandIndex = 0;
                    while (in.hasNext()) {
                        equalizer.setBandGain(bandIndex++, in.nextDouble());
                    }
                    in.endArray();
                }
                case "userMusicPath" -> {
                    String rawPath = in.nextString();
                    userMusicPath = (rawPath != null && !rawPath.isBlank())
                            ? Paths.get(rawPath)
                            : Path.of(System.getProperty("user.home"), "Music");
                }
                case "crossfadeDuration" -> {
                    crossfadeDuration = in.nextDouble();
                    break;
                }
                default -> in.skipValue();
            }
        }
        in.endObject();

        // Log minimal for debug
        System.out.println("[UserProfile Loaded] " + username + " | " +
                (userPicturePath != null ? userPicturePath : "no picture") + " | " +
                balance + " | " +
                userMusicPath);

        return new UserProfile(
                username,
                userPicturePath,
                balance,
                userMusicPath,
                equalizer,
                crossfadeDuration
        );
    }

}
