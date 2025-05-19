package musicApp.services;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.jaudiotagger.tag.images.Artwork;
import org.jaudiotagger.tag.images.ArtworkFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * A utility service for extracting images and artwork from video and image files.
 */
public class VideoService {

    /**
     * Extracts the very first frame from a mp4 video file and returns it as a JavaFX {@link Image}.
     *
     * @param videoFile the video file to process
     * @return the first frame of the video as a JavaFX {@link Image}, or {@code null} if no frame was found
     * @throws Exception if an error occurs during video processing
     */
    public Image getFirstFrame(File videoFile) throws Exception {
        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoFile)) {
            grabber.start();

            var frame = grabber.grabImage(); // grabs the first video frame

            if (frame == null) return null;

            BufferedImage bimg = new Java2DFrameConverter().convert(frame);
            return SwingFXUtils.toFXImage(bimg, null);
        }
    }

    /**
     * Converts a JavaFX {@link Image} to a Jaudiotagger {@link Artwork} instance using a temporary file.
     *
     * @param image the image to convert
     * @return the generated {@link Artwork} object
     * @throws Exception if the image cannot be converted or written to file
     */
    public Artwork imageToArtwork(Image image) throws Exception {
        File tempFile = tempFileFromImage(image);
        return ArtworkFactory.createArtworkFromFile(tempFile);
    }

    /**
     * Creates an {@link Artwork} from a file.
     * <p>
     * If the file is a video (e.g., .mp4), the first frame is extracted and used as artwork.
     * Otherwise, the file is assumed to be an image and passed directly to {@link ArtworkFactory}.
     *
     * @param file the image or video file
     * @return an {@link Artwork} representation of the visual content
     * @throws Exception if the artwork cannot be created or video processing fails
     */
    public Artwork getArtworkFromFile(File file) throws Exception {
        if (file.getPath().endsWith(".mp4")) {
            Image firstFrame = getFirstFrame(file);
            if (firstFrame == null) {
                throw new IOException("Failed to load frame from video");
            }
            return imageToArtwork(firstFrame);
        }

        return ArtworkFactory.createArtworkFromFile(file);
    }

    /**
     * Writes raw video bytes to a temporary MP4 file on disk.
     *
     * @param rawVideo the raw video data as a byte array
     * @return a {@link File} reference to the newly created temporary video file
     * @throws Exception if writing the file fails
     */
    public File tempFileFromBytes(byte[] rawVideo) throws Exception {
        File tempFile = File.createTempFile("coverVideo_", ".mp4");
        tempFile.deleteOnExit();

        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(rawVideo);
        }

        return tempFile;
    }

    /**
     * Writes a JavaFX {@link Image} to a temporary PNG file on disk.
     *
     * @param image the JavaFX image to save
     * @return a {@link File} reference to the temporary file
     * @throws IOException if the image cannot be written
     */
    private File tempFileFromImage(Image image) throws IOException {
        File tempFile = File.createTempFile("artwork_", ".png");
        tempFile.deleteOnExit();
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", tempFile);
        return tempFile;
    }
}
