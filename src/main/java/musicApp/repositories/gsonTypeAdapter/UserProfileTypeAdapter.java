package musicApp.repositories.gsonTypeAdapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import musicApp.enums.Language;
import musicApp.models.Equalizer;
import musicApp.models.UserProfile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UserProfileTypeAdapter extends TypeAdapter<UserProfile> {
    /**
     * Writes the UserProfile object to JSON format.
     *
     * @param out         The JsonWriter to write to.
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

        // Playlist path (nullable)
        out.name("userPlaylistPath");
        Path playlistPath = userProfile.getUserPlaylistPath();
        out.value(playlistPath != null ? playlistPath.toString() : "");

        // User preferred language
        Language userLanguage = userProfile.getLanguage();
        switch (userLanguage) {
            case FRENCH:  out.name("language").value("fr"); break;
            case DUTCH:  out.name("language").value("nl"); break;
            default: out.name("language").value("en"); break;
        }

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
        Path userPlaylistPath = null;
        double balance = 0;
        double crossfadeDuration = 0.0;
        Language language = null;

        Equalizer equalizer = new Equalizer();

        in.beginObject();
        while (in.hasNext()) {
            String fieldName = in.nextName();
            switch (fieldName) {
                case "username" -> username = in.nextString();
                case "userPicturePath" -> {
                    String rawPath = in.nextString();
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
                case "crossfadeDuration" -> crossfadeDuration = in.nextDouble();
                case "userPlaylistPath" -> {
                    String rawPath = in.nextString();
                    userPlaylistPath = (rawPath != null && !rawPath.isBlank())
                            ? Paths.get(rawPath)
                            : null;
                }
                case "language" -> language = Language.fromCode(in.nextString());
                default -> in.skipValue();
            }
        }
        in.endObject();

        return new UserProfile(
                username,
                userPicturePath,
                userMusicPath,
                language,
                userPlaylistPath,
                balance,
                crossfadeDuration,
                equalizer
        );
    }

}
