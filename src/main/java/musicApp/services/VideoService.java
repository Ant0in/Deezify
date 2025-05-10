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

public class VideoService {

    /** Returns the very first video frame as a JavaFX {@link Image}. */
    public Image getFirstFrame(File videoFile) throws Exception {
        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoFile)) {
            grabber.start();

            var frame = grabber.grabImage();  // grabs the first *video* frame

            if (frame == null) return null;

            BufferedImage bimg = new Java2DFrameConverter().convert(frame);
            return SwingFXUtils.toFXImage(bimg, null);
        }
    }



    public Artwork imageToArtwork(Image image) throws Exception {
        File tempFile = tempFileFromImage(image);
        return ArtworkFactory.createArtworkFromFile(tempFile);
    }
    /// Returns an artwork from either an image file or from a video file by using its first frame
    public Artwork getArtworkFromFile(File file) throws Exception {

        if (file.getPath().endsWith(".mp4")) {
            Image firstFrame = getFirstFrame(file);
            if (firstFrame == null) { throw new IOException("Failed to load frame from video"); };
            return imageToArtwork(firstFrame);
        }

        return ArtworkFactory.createArtworkFromFile(file);
    }

    private File tempFileFromImage(Image image) throws IOException {
        File tempFile = File.createTempFile("artwork_", "");
        tempFile.deleteOnExit();
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", tempFile);
        return tempFile;
    }
}
