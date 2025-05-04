package musicApp.repositories.gsonTypeAdapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.JsonReader;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import musicApp.models.Equalizer;
import musicApp.models.Settings;
import musicApp.models.User;

import java.io.IOException;
import java.util.Objects;

public class UserTypeAdapter extends TypeAdapter<User> {
    /**
     * Writes the User object to JSON format.
     *
     * @param out      The JsonWriter to write to.
     * @param user The User object to write.
     * @throws IOException If writing fails.
     */
    @Override
    public void write(JsonWriter out, User user) throws IOException {
        out.beginObject();

        out.name("username").value(user.getUsername());

        // Profile picture path (nullable)
        out.name("userPicturePath");
        Path picturePath = user.getUserPicturePath();
        out.value(picturePath != null ? picturePath.toString() : "");

        out.name("balance").value(user.getBalance());

        // Equalizer bands
        out.name("equalizerBands");
        out.beginArray();
        for (Double band : user.getEqualizerBands()) {
            out.value(band);
        }
        out.endArray();

        // Music path (nullable)
        out.name("userMusicPath");
        Path musicPath = user.getUserMusicPathToString() != null
                ? user.getUserMusicPath()
                : null;
        out.value(musicPath != null ? musicPath.toString() : "");

        out.endObject();
    }


    /**
     * Reads a User object from JSON format.
     *
     * @param in The JsonReader to read from.
     * @return The User object read from JSON.
     * @throws IOException If reading fails.
     */
    @Override
    public User read(JsonReader in) throws IOException {
        String username = null;
        Path userPicturePath = null;
        Path userMusicPath = null;
        double balance = 0;
        Equalizer equalizer = new Equalizer();

        in.beginObject();
        while (in.hasNext()) {
            String fieldName = in.nextName();
            switch (fieldName) {
                case "username" -> username = in.nextString();
                case "userPicturePath" -> {
                    String rawPath = in.nextString();
                    System.out.println("[UserTypeAdapter] rawPath: " + rawPath);
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
                default -> in.skipValue();
            }
        }
        in.endObject();

        // Log minimal for debug
        System.out.println("[User Loaded] " + username + " | " +
                (userPicturePath != null ? userPicturePath : "no picture") + " | " +
                balance + " | " +
                userMusicPath);

        return new User(
                username,
                userPicturePath,
                balance,
                userMusicPath,
                equalizer
        );
    }

}
