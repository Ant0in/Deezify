
package MusicApp.Controllers;
import java.io.File;
import java.util.HashMap;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.mp3.MP3FileReader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import MusicApp.Exceptions.ID3TagException;





public class MetadataReader {
    
    /**
     * 
     * This method reads the metadata of a file and returns it in a hashmap
     * Fields included in the hashmap are : Album, Artist, Comment, Genre, Title, Track, Year
     * @param fd : mp3 File Object
     * @return HashMap<String, String> containing relevant tags mentioned above
     * @throws ID3TagException
     * 
    */

    public static final HashMap<String, String> getMetadata(File fd) throws ID3TagException {
        
        HashMap<String, String> metadata = new HashMap<>();
        Tag tag = readTag(fd);

        if (tag == null) {
            throw new ID3TagException("No ID3v2 tags found", new Throwable());
        }

        // get some attributes such as Album, Artist, comment, Genre, Title, Track, Year, ...
        // and put them in a new hashmap

        // TODO : add more if needed

        metadata.put("Album", tag.getFirst(FieldKey.ALBUM));
        metadata.put("Artist", tag.getFirst(FieldKey.ARTIST));
        metadata.put("Comment", tag.getFirst(FieldKey.COMMENT));
        metadata.put("Genre", tag.getFirst(FieldKey.GENRE));
        metadata.put("Title", tag.getFirst(FieldKey.TITLE));
        metadata.put("Track", tag.getFirst(FieldKey.TRACK));
        metadata.put("Year", tag.getFirst(FieldKey.YEAR));

        return metadata;

    }

    /**
     * This method reads the tag of a file and returns it
     * @param fd : mp3 File Object
     * @return Tag object
    */

    private static Tag readTag(File fd) {

        AudioFile file = MetadataReader.readFile(fd);
        return file.getTag();

    }

    /**
     * This method reads the file and returns it as an AudioFile object
     * @param fd : mp3 File Object
     * @return AudioFile object
     * @throws RuntimeException (can be IOException, TagException, ReadOnlyFileException, CannotReadException, InvalidAudioFrameException)
    */

    private static AudioFile readFile(File fd) throws RuntimeException {

        MP3FileReader reader = new MP3FileReader();
        AudioFile file = null;

        // These are the exceptions thrown by the read method if something goes wrong
        // IOException, TagException, ReadOnlyFileException, CannotReadException, InvalidAudioFrameException

        try {
            file = reader.read(fd);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return file;

    }

}
