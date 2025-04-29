package musicApp.repositories.gsonTypeAdapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.JsonReader;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import musicApp.models.Equalizer;
import musicApp.models.Settings;
import musicApp.models.User;

import java.io.IOException;

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

        out.name("username");
        out.value(user.getUsername());
        out.name("profilePicturePath");
        if (user.getProfilePicturePath() == null) {
            out.value("");
        } else {
            out.value(user.getProfilePicturePath().toString());
        }
        out.name("balance");
        out.value(user.getBalance());
        out.name("equalizerBands");
        out.beginArray();
        for (Double band : user.getEqualizerBands()) {
            out.value(band);
        }
        out.endArray();


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
        String userPicturePath = null;
        String userMusicPath = null;
        double balance = 0;
        Equalizer equalizer = new Equalizer();

        in.beginObject();
        while (in.hasNext()) {
            String fieldName = in.nextName();
            switch (fieldName) {
                case "username":
                    username = in.nextString();
                    break;
                case "userPicturePath":
                    userPicturePath = in.nextString();
                    break;
                case "balance":
                    balance = in.nextDouble();
                    break;
                case "equalizerBands":
                    in.beginArray();
                    int bandIndex = 0;
                    while (in.hasNext()) {
                        equalizer.setBandGain(bandIndex++, in.nextDouble());
                    }
                    in.endArray();
                    break;
                case "userMusicPath":
                    userMusicPath = in.nextString();
                    break;
                default:
                    in.skipValue();
            }
        }
    }

}
