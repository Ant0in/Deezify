package musicApp.services;

import javafx.util.Duration;
import musicApp.enums.SupportedFileType;
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

public class MetadataService {

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
     * @return SupportedFileType, or null
     */
    private SupportedFileType getFileExtension(File fd) throws BadFileTypeException {
        String fileName = fd.getName();
        String ext = fileName.substring(fileName.lastIndexOf("."));
        SupportedFileType fileType = SupportedFileType.fromExtension(ext);
        if (fileType == null) {
            throw new BadFileTypeException("Unsupported file extension", new Throwable());
        }
        return fileType;
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
     * This method reads the metadata of a file and returns it in a hashmap
     * Fields included in the hashmap are : title, artist, genre, cover, duration
     *
     * @param fd : mp3 File Object
     * @return Metadata object
     * @throws ID3TagException (if no ID3v2 tags are found)
     */
    public Metadata getMetadata(File fd) throws ID3TagException, BadFileTypeException {
        Metadata metadata = new Metadata();
        if (getFileExtension(fd) == SupportedFileType.M3U) {
            return getRadioMetadata(fd);
        }
        AudioFile file = readFile(fd);
        Tag tag = readTag(file);
        if (tag == null) {
            throw new ID3TagException("No ID3v2 tags found", new Throwable());
        }

        return loadTagValues(file, metadata, tag);
    }

    /**
     * Loads the tag values from the provided audio file's metadata and populates the given {@link Metadata} object with those values.
     *
     * <p>This method extracts various metadata fields such as the title, artist, genre, duration, and user tags from the provided
     * {@link Tag} and assigns them to the {@link Metadata} object. It also attempts to retrieve the cover artwork. If an error
     * occurs while retrieving user tags, an empty list is assigned instead.</p>
     *
     * @param file     The {@link AudioFile} associated with the tag, used to extract the duration.
     * @param metadata The {@link Metadata} object that will be populated with the extracted tag values.
     * @param tag      The {@link Tag} containing the metadata values to be loaded.
     * @return The populated {@link Metadata} object with the extracted tag values.
     * @throws IllegalArgumentException if the file or metadata is {@code null}.
     */
    private Metadata loadTagValues(AudioFile file, Metadata metadata, Tag tag) {
        metadata.setTitle(tag.getFirst(FieldKey.TITLE));
        metadata.setArtist(tag.getFirst(FieldKey.ARTIST));
        metadata.setAlbum(tag.getFirst(FieldKey.ALBUM));
        metadata.setGenre(tag.getFirst(FieldKey.GENRE));
        metadata.setDuration(Duration.seconds(file.getAudioHeader().getTrackLength()));
        /* The following call to tag.getFirst can throw an unexpected error, we catch it and
         * handle it by setting an empty user tag list
         */
        try {
            metadata.setUserTags(parseUserTags(tag.getFirst(FieldKey.CUSTOM1)));
        } catch (UnsupportedOperationException e) {
            metadata.setUserTags(new ArrayList<>());
        }
        metadata.setCover(tag.getFirstArtwork());
        return metadata;
    }

    /**
     * This method assigns default metadata to a radio file
     *
     * @param fd : the File on which to read the metadata
     * @return : A metadata object loaded with the info from the m3u file
     */
    private Metadata getRadioMetadata(File fd) {
        Metadata metadata = new Metadata();
        metadata.setTitle(fd.getName().replace(".m3u", ""));
        metadata.setArtist("N/A");
        metadata.setAlbum("N/A");
        metadata.setGenre("Radio");
        metadata.setDuration(Duration.ZERO);

        try (InputStream is = getClass().getResourceAsStream("/images/radio.png")) {
            if (is != null) {
                metadata.setCoverFromBytes(is.readAllBytes());
            } else {
                System.err.println("The image /images/radio.png is unfoundable.");
            }
        } catch (IOException e) {
            System.err.println("Error while reading the image from radio cover : " + e.getMessage());
        }

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

    /**
     * Creates or updates the tag for the provided audio file based on the given metadata.
     *
     * <p>This method checks if the audio file's tag contains standard fields (e.g., title). If the tag is missing a required field,
     * it creates or retrieves a default tag. The tag is then populated with data from the provided {@link Metadata} object,
     * including artist, title, genre, custom user tags, and cover artwork.</p>
     *
     * <p>Note: The {@link FieldDataInvalidException} could potentially be thrown when setting the fields, but it cannot be
     * artificially triggered during the normal operation of this method.</p>
     *
     * @param audioFile The {@link AudioFile} to which the tag will be created or updated.
     * @param metadata  The {@link Metadata} object containing the values to be written to the tag.
     * @return The updated {@link Tag} containing the metadata fields.
     * @throws FieldDataInvalidException If the tag data is invalid or inconsistent with expected field types.
     */
    private Tag createTag(AudioFile audioFile, Metadata metadata) throws FieldDataInvalidException {
        Tag tag = audioFile.getTagOrCreateAndSetDefault();
        // Checks if the tag contains a standard field, if not initializes a default tag
        if (!tag.hasField(FieldKey.TITLE)) {
            tag = audioFile.getTagAndConvertOrCreateDefault();
        }
        // !!  FieldDataInvalidException can technically be thrown but could not be triggered artificially by us.
        tag.setField(FieldKey.ARTIST, metadata.getArtist());
        tag.setField(FieldKey.ALBUM, metadata.getAlbum());
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
     * This method reads the file and returns it as an AudioFile object
     * currently supports MP3 and WAV files only
     *
     * @param fd : File Object
     * @return AudioFile object
     */
    private AudioFile readFile(File fd) throws BadFileTypeException {
        SupportedFileType ext = getFileExtension(fd);
        AudioFile file;

        file = switch (ext) {
            case MP3, WAV -> {
                try {
                    yield AudioFileIO.read(fd);
                } catch (Exception e) {
                    throw new RuntimeException("Error while reading the image from file : " + fd.getName(), e);
                }
            }
            default -> throw new BadFileTypeException("Unsupported file type", new Throwable());
        };

        return file;
    }

}
