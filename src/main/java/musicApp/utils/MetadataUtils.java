package musicApp.utils;

import javafx.util.Duration;
import musicApp.exceptions.BadFileTypeException;
import musicApp.exceptions.ID3TagException;
import musicApp.models.Metadata;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import java.io.File;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

enum FileType {
    MP3,
    WAV,
    M3U,
//    FLAC,
//    OGG
    NONE
}

public class MetadataUtils {

    /**
     * Parses the user tags String into separate tag strings contained in an ArrayList
     *
     * @param userTagsString : The String to parse ( format : "tag1;tag2;tag3;")
     * @return : ArrayList containing individual tag strings
     */
    static private ArrayList<String> parseUserTags(String userTagsString) {
        // Replaces the string in case it is null
        userTagsString = userTagsString == null ? "" : userTagsString;

        String[] tagsArray = userTagsString.split(";", -1);

        // Convert the String array to an ArrayList
        return new ArrayList<>(Arrays.asList(tagsArray));
    }

    /**
     * Formats a user tag ArrayList into a String
     *
     * @param userTags : The ArrayList to format
     * @return : Formatted String
     */
    static private String formatUserTags(ArrayList<String> userTags) {

        return String.join(";", userTags);
    }

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
        }else if(ext.equals("m3u")) {
            return FileType.M3U;
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
        if (getFileExtension(fd) == FileType.M3U) {
            metadata.setTitle(fd.getName().substring(0, fd.getName().length() - ".m3u".length()));
            metadata.setArtist("N/A");
            metadata.setGenre("Radio");
            metadata.setDuration(Duration.ZERO);
            try {
            InputStream is = getClass().getResourceAsStream("/images/radio.png");
            if (is != null) {
                metadata.setCoverFromBytes(is.readAllBytes());
            } else {
                System.err.println("L'image /images/radio.png est introuvable.");
            }
        } catch (IOException e) {
    e.printStackTrace();
}

            return metadata;
        }
        AudioFile file = readFile(fd);
        Tag tag = readTag(file);
        if (tag == null) {
            throw new ID3TagException("No ID3v2 tags found", new Throwable());
        }

        metadata.setTitle(tag.getFirst(FieldKey.TITLE));
        metadata.setArtist(tag.getFirst(FieldKey.ARTIST));
        metadata.setGenre(tag.getFirst(FieldKey.GENRE));
        metadata.setDuration(Duration.seconds(file.getAudioHeader().getTrackLength()));
        /* The following call to tag.getFirst can throw an unexpected error, we catch it and
         * handle it by setting an empty user tag list
         */
        try {
            metadata.setUserTags(parseUserTags(tag.getFirst(FieldKey.CUSTOM1)));
        } catch (UnsupportedOperationException e) {
            metadata.setUserTags(new ArrayList<String>());
        }


        metadata.setCover(tag.getFirstArtwork());


        return metadata;
    }

    /**
     * This method writes the passed metadata to the file at the given path
     *
     * @param metadata : Metadata object to write to the given song file
     * @param fd       : File object corresponding to the song to modify (wav or mp3)
     */
    public void setMetadata(Metadata metadata, File fd) throws BadFileTypeException, FieldDataInvalidException, CannotWriteException {
        AudioFile audioFile = readFile(fd);

        Tag tag = createTag(audioFile, metadata);

        audioFile.setTag(tag);
        audioFile.commit();

    }

    private Tag createTag(AudioFile audioFile, Metadata metadata) throws FieldDataInvalidException {

        Tag tag = audioFile.getTag();
        // Checks if the tag contains a standard field, if not initializes a default tag
        if (!tag.hasField(FieldKey.TITLE)) {
            tag = audioFile.getTagAndConvertOrCreateDefault();
        }
        // !!  FieldDataInvalidException can technically be thrown but could not be triggered artificially by us.
        tag.setField(FieldKey.ARTIST, metadata.getArtist());
        tag.setField(FieldKey.TITLE, metadata.getTitle());
        tag.setField(FieldKey.GENRE, metadata.getGenre());
        tag.setField(FieldKey.CUSTOM1, formatUserTags(metadata.getUserTags()));
        if (metadata.getCover() != null) {
            tag.deleteArtworkField();
            tag.setField(metadata.getCover());
        }
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
