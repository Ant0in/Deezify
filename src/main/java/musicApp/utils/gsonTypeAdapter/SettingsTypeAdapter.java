package musicApp.utils.gsonTypeAdapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import musicApp.models.Equalizer;
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

        out.name("balance");
        out.value(settings.getBalance());

        out.name("musicFolder");
        out.value(settings.getMusicFolder().toString());

        out.name("equalizerBands");
        out.beginArray();
        for (Double band : settings.getEqualizerBands()) {
            out.value(band);
        }
        out.endArray();

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
        double balance = 0.0;
        Path musicFolder = null;
        Equalizer equalizer = new Equalizer();

        in.beginObject();
        while (in.hasNext()) {
            String fieldName = in.nextName();
            switch (fieldName) {
                case "balance":
                    balance = in.nextDouble();
                    break;
                case "musicFolder":
                    musicFolder = Paths.get(in.nextString());
                    break;
                case "equalizerBands":
                    in.beginArray();
                    int bandIndex = 0;
                    while (in.hasNext()) {
                        equalizer.setBandGain(bandIndex++, in.nextDouble());
                    }
                    in.endArray();
                    break;
                default:
                    in.skipValue();
                    break;
            }
        }
        in.endObject();

        return new Settings(balance, musicFolder, equalizer);
    }
}