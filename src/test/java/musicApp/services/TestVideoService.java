/**
 * Test suite for the VideoService class.
 */
package musicApp.services;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.jaudiotagger.tag.images.Artwork;
import org.junit.Test;


public class TestVideoService {

    private final VideoService videoService = new VideoService();

    @Test
    public void getArtworkFromFileTest() throws Exception {

        // Test with a video file
        Path videoPath = Paths.get("src", "test", "resources", "soup.mp4");
        File videoFile = new File(videoPath.toString());
        Artwork videoArtwork = videoService.getArtworkFromFile(videoFile);
        assert videoArtwork != null : "Failed to extract artwork from video file";

        // Test with an image file
        Path imagePath = Paths.get("src", "test", "resources", "gus.jpg");
        File imageFile = new File(imagePath.toString());
        Artwork imageArtwork  = videoService.getArtworkFromFile(imageFile);
        assert imageArtwork != null : "Failed to extract artwork from image file";
        
    }
        

}
