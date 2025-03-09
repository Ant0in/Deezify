
package MusicApp.utils;
import java.io.File;
import java.util.Base64;
import java.util.HashMap;

import MusicApp.Exceptions.BadFileTypeException;
import MusicApp.Models.Metadata;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.mp3.MP3FileReader;
import org.jaudiotagger.audio.wav.WavFileReader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import MusicApp.Exceptions.ID3TagException;

enum FileType {
    MP3,
    WAV,
//    FLAC,
//    OGG
    NONE
}

public class MetadataReader {

    /**
     * This method returns an enum based on the extension of a file
     * @param fd : File Object
     * @return FileType enum
    */
    private static FileType getFileExtension(File fd) {
        String fileName = fd.getName();
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);

        if (ext.equals("mp3")) {
            return FileType.MP3;
        } else if (ext.equals("wav")) {
            return FileType.WAV;
        } else {
            return FileType.NONE;
        }
    }

    /**
     * 
     * This method reads the metadata of a file and returns it in a hashmap
     * Fields included in the hashmap are : title, artist, genre, cover, duration
     * @param fd : mp3 File Object
     * @return HashMap<String, String> containing relevant tags mentioned above
     * @throws ID3TagException (if no ID3v2 tags are found)
     * 
    */

    public static Metadata getMetadata(File fd) throws ID3TagException, BadFileTypeException {

        HashMap<String, String> metadata = new HashMap<>();
        AudioFile file = readFile(fd);
        Tag tag = readTag(file);
        if (tag == null) {
            throw new ID3TagException("No ID3v2 tags found", new Throwable());
        }

        // get some attributes such as title, artist, genre, cover, duration, ...
        // and put them in a new hashmap

        // TODO : add more if needed

        metadata.put("title", tag.getFirst(FieldKey.TITLE));
        metadata.put("artist", tag.getFirst(FieldKey.ARTIST));
        metadata.put("genre", tag.getFirst(FieldKey.GENRE));
        metadata.put("duration", String.valueOf(file.getAudioHeader().getTrackLength()));

        try {
            if (tag.getFirstArtwork() != null) {
                byte[] imageData = tag.getFirstArtwork().getBinaryData();
                metadata.put("cover", Base64.getEncoder().encodeToString(imageData));
            }
        } catch (Exception e) {
            metadata.put("cover", null);
        }

        return new Metadata(metadata);
    }

    /**
     * This method reads the tag of an AudioFile and returns it
     * @param fd : AudioFile Object
     * @return Tag object
    */

    private static Tag readTag(AudioFile fd) {
        return fd.getTag();
    }

    /**
     * This method reads the file and returns it as an AudioFile object
     * @param fd : WAV File Object
     * @return AudioFile object
     * @throws RuntimeException (can be IOException, TagException, ReadOnlyFileException, CannotReadException, InvalidAudioFrameException)
     */

    private static AudioFile readWAVFile(File fd) {
        WavFileReader reader = new WavFileReader();
        AudioFile file;

        try {
            file = reader.read(fd);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    /**
     * This method reads the file and returns it as an AudioFile object
     * @param fd : mp3 File Object
     * @return AudioFile object
     * @throws RuntimeException (can be IOException, TagException, ReadOnlyFileException, CannotReadException, InvalidAudioFrameException)
    */

    private static AudioFile readMP3File(File fd) throws RuntimeException {

        MP3FileReader reader = new MP3FileReader();
        AudioFile file;

        // These are the exceptions thrown by the read method if something goes wrong
        // IOException, TagException, ReadOnlyFileException, CannotReadException, InvalidAudioFrameException

        try {
            file = reader.read(fd);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return file;
    }

    /**
     * This method reads the file and returns it as an AudioFile object
     * currently supports MP3 and WAV files only
     * @param fd : File Object
     * @return AudioFile object
    */
    private static AudioFile readFile(File fd) throws BadFileTypeException {
        FileType ext = getFileExtension(fd);

        return switch (ext) {
            case MP3 -> readMP3File(fd);
            case WAV -> readWAVFile(fd);
            default -> throw new BadFileTypeException("Unsupported file type", new Throwable());
        };
    }

}
