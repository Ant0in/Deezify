package MusicApp.utils;

import javafx.util.Duration;
import MusicApp.exceptions.BadFileTypeException;
import MusicApp.exceptions.ID3TagException;
import MusicApp.models.Metadata;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.mp3.MP3FileReader;
import org.jaudiotagger.audio.wav.WavFileReader;
import org.jaudiotagger.audio.wav.WavOptions;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.wav.WavTag;

import java.io.File;

enum FileType {
    MP3,
    WAV,
//    FLAC,
//    OGG
    NONE
}

public class MetadataUtils {

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
     * This method writes the passed metadata to the file at the given path
     *
     * @param metadata : Metadata object to write to the given song file
     * @param fd : File object corresponding to the song to modify (wav or mp3)
     *
     */
    public void setMetadata(Metadata metadata, File fd) throws BadFileTypeException, FieldDataInvalidException, CannotWriteException {
        AudioFile audioFile = readFile(fd);

        Tag tag = switch (getFileExtension(fd)) {

            case MP3 -> createMP3Tag(audioFile, metadata);
            case WAV -> createWAVTag(audioFile, metadata);
            default -> throw new BadFileTypeException("Invalid File Extension for file : " + fd.getName(), new Throwable());
        };

        audioFile.setTag(tag);
        audioFile.commit();


    }

    private Tag createMP3Tag(AudioFile audioFile, Metadata metadata) throws CannotWriteException, FieldDataInvalidException {
        Tag tag = audioFile.createDefaultTag();

        // !!  FieldDataInvalidException can technically be thrown but could not be triggered artificially by us.
        tag.addField(FieldKey.ARTIST, metadata.getArtist());
        tag.addField(FieldKey.TITLE, metadata.getTitle());
        tag.addField(FieldKey.GENRE, metadata.getGenre());

        return tag;
    }
    private Tag createWAVTag(AudioFile audioFile, Metadata metadata) throws CannotWriteException, FieldDataInvalidException {

        Tag tag = audioFile.getTag();

        // !!  FieldDataInvalidException can technically be thrown but could not be triggered artificially by us.
        tag.setField(FieldKey.ARTIST, metadata.getArtist());
        tag.setField(FieldKey.TITLE, metadata.getTitle());
        tag.setField(FieldKey.GENRE, metadata.getGenre());

        return tag;
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

        AudioFile file;

        try {
            file = AudioFileIO.read(fd);
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

        AudioFile file;

        // These are the exceptions thrown by the read method if something goes wrong
        // IOException, TagException, ReadOnlyFileException, CannotReadException, InvalidAudioFrameException

        try {
            file = AudioFileIO.read(fd);
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
