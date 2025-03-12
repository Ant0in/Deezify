package musicApp.utils;

import javafx.util.Duration;
import musicApp.exceptions.BadFileTypeException;
import musicApp.exceptions.ID3TagException;
import musicApp.models.Metadata;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.mp3.MP3FileReader;
import org.jaudiotagger.audio.wav.WavFileReader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import java.io.File;

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
     *
     * @param fd : File Object
     * @return FileType enum
     */
    private FileType getFileExtension(File fd) {
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
     * This method reads the metadata of a file and returns it in a hashmap
     * Fields included in the hashmap are : title, artist, genre, cover, duration
     *
     * @param fd : mp3 File Object
     * @return Metadata object
     * @throws ID3TagException (if no ID3v2 tags are found)
     */
    public Metadata getMetadata(File fd) throws ID3TagException, BadFileTypeException {
        Metadata metadata = new Metadata();
        AudioFile file = readFile(fd);
        Tag tag = readTag(file);
        if (tag == null) {
            throw new ID3TagException("No ID3v2 tags found", new Throwable());
        }

        metadata.setTitle(tag.getFirst(FieldKey.TITLE));
        metadata.setArtist(tag.getFirst(FieldKey.ARTIST));
        metadata.setGenre(tag.getFirst(FieldKey.GENRE));
        metadata.setDuration(Duration.seconds(file.getAudioHeader().getTrackLength()));

        if (tag.getFirstArtwork() != null) {
            metadata.setCover(tag.getFirstArtwork().getBinaryData());
        }

        return metadata;
    }

    /**
     * This method reads the tag of an AudioFile and returns it
     *
     * @param fd : AudioFile Object
     * @return Tag object
     */
    private Tag readTag(AudioFile fd) {
        return fd.getTag();
    }

    /**
     * This method reads the file and returns it as an AudioFile object
     *
     * @param fd : WAV File Object
     * @return AudioFile object
     * @throws RuntimeException (can be IOException, TagException, ReadOnlyFileException, CannotReadException, InvalidAudioFrameException)
     */
    private AudioFile readWAVFile(File fd) {
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
     *
     * @param fd : mp3 File Object
     * @return AudioFile object
     * @throws RuntimeException (can be IOException, TagException, ReadOnlyFileException, CannotReadException, InvalidAudioFrameException)
     */

    private AudioFile readMP3File(File fd) throws RuntimeException {

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
     *
     * @param fd : File Object
     * @return AudioFile object
     */
    private AudioFile readFile(File fd) throws BadFileTypeException {
        FileType ext = getFileExtension(fd);

        return switch (ext) {
            case MP3 -> readMP3File(fd);
            case WAV -> readWAVFile(fd);
            default -> throw new BadFileTypeException("Unsupported file type", new Throwable());
        };
    }

}
