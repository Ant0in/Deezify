package musicApp.repositories.gsonTypeAdapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import musicApp.enums.Language;
import musicApp.models.Settings;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * TypeAdapter for the Settings class.
 * This class is used to serialize and deserialize the Settings class to and from JSON.
 */
public class SettingsTypeAdapter extends TypeAdapter<Settings> {
    /**
     * Writes the Settings object to JSON format.
     *
     * @param out      The JsonWriter to write to.
     * @param settings The Settings object to write.
     * @throws IOException If writing fails.
     */
    @Override
    public void write(JsonWriter out, Settings settings) throws IOException {
        out.beginObject();
        out.name("musicFolder");
        out.value(settings.getMusicFolderString());
        out.name("crossfadeDuration");
        out.value(settings.getCrossfadeDuration());
        out.name("language");

        switch (settings.getDefaultLanguage()) {
            case FRENCH: out.value("fr"); break;
            case DUTCH: out.value("nl"); break;
            default: out.value("en");
        }
        out.endObject();
    }

    /**
     * Reads a Settings object from JSON format.
     *
     * @param in The JsonReader to read from.
     * @return The Settings object read from JSON.
     * @throws IOException If reading fails.
     */
    @Override
    public Settings read(JsonReader in) throws IOException {
        Path musicFolder = null;
        Language language = null;

        in.beginObject();
        while (in.hasNext()) {
            String fieldName = in.nextName();
            if (fieldName.equals("musicFolder")) {
                musicFolder = Paths.get(in.nextString());
            } else if (fieldName.equals("language")) {
                language = Language.fromCode(in.nextString());
            } else {
                in.skipValue();
            }
        }
        in.endObject();

        return new Settings(musicFolder, language);
    }
}