package musicApp.views;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.List;

/**
 * View component used to render the audio spectrum visualizer in a separate window (popup)
 */
public class MiniPlayerView extends View {

    private MiniPlayerViewListener listener;

    /**
     * The Song title label.
     */
    @FXML
    private Label songTitleLabel;

    /**
     * The Cover image.
     */
    @FXML
    private ImageView coverImage;
    /**
     * The Canvas.
     */
    @FXML
    private Canvas canvas;
    // Flag to track whether the image is clipped or not
    private boolean isClipped;

    /**
     * The Is basic mode.
     */
    Boolean isBasicMode;

    public MiniPlayerView() {
        super();
        isClipped = true;
        isBasicMode = false;
    }

    /**
     * Listener interface for handling user actions from the controller.
     */
    public interface MiniPlayerViewListener {
        int getBandsNumber();
    }

    /**
     * Sets listener.
     *
     * @param _listener the listener
     */
    public void setListener(MiniPlayerViewListener _listener) {
        listener = _listener;
    }

    @Override
    public void init() {
        scene.setOnMouseClicked(_ -> changeVisualizerMode());
    }

    private void changeVisualizerMode() {
        isBasicMode = !isBasicMode;
        toggleClip();
    }

    /**
     * Draw.
     *
     * @param values the values
     */
    public void draw(List<Float> values) {
        if (isBasicMode) {
            drawFrame(values);
        } else {
            drawCircularFrame(values);
        }
    }

    /**
     * Gets scene.
     *
     * @return the scene
     */
    public Scene getScene() { return scene; }

    /**
     * Update song properties.
     *
     * @param title      the title
     * @param coverImage the cover image
     */
    public void updateSongProperties(String title, Image coverImage) {
        songTitleLabel.setText(title);
        setCoverImage(coverImage);
    }

    /**
     * Sets cover image.
     *
     * @param image the image
     */
    public void setCoverImage(Image image) {
        coverImage.setImage(image);
    }


    /**
     * Toggle clip.
     */
    public void toggleClip() {
        if (isClipped) {
            // Remove the clip (non-clipped state)
            coverImage.setClip(null);
        } else {
            // Set the clip to a circle (clipped state)
            Circle clip = new Circle(coverImage.getFitWidth() / 2, coverImage.getFitHeight() / 2, coverImage.getFitWidth() / 2);
            coverImage.setClip(clip);
        }
        // Toggle the flag
        isClipped = !isClipped;
    }

    /**
     * Draw a frame for the audio spectrum visualizer.
     */
    private void drawFrame( List<Float> values) {
        int numBars = listener.getBandsNumber();
        double barWidth = canvas.getWidth() / numBars; // Width of each bar
        double canvasHeight = canvas.getHeight();
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Clear the canvas to start fresh
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Loop through each frequency band and draw bars
        for (int i = 0; i < numBars; i++) {
            // Calculate the height for each bar
            double barHeight = Math.max(values.get(i), 0.005) * canvasHeight;

            // Set the color for each bar (we can customize this later if wanted)
            gc.setFill(Color.hsb(i * 360.0 / numBars, 1.0, 1.0)); // Color spectrum

            // Draw each bar on the canvas
            gc.fillRect(i * barWidth, canvasHeight - barHeight, barWidth, barHeight);
        }
    }

    /**
     * Draw a circular frame for the audio spectrum visualizer.
     */
    private void drawCircularFrame(List<Float> values) {
        int numBars = listener.getBandsNumber();
        double radius = Math.min(canvas.getWidth(), canvas.getHeight()) / 3; // Radius for the circle
        double centerX = canvas.getWidth() / 2;
        double centerY = canvas.getHeight() / 2;
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Clear the canvas to start fresh
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Loop through each frequency band and draw bars in a circular pattern
        for (int i = 0; i < numBars; i++) {
            // Calculate the height for each bar
            double barHeight = Math.max(values.get(i), 0.005) * radius;  // Adjusted to fit the circle radius

            // Calculate the angle for the current bar (spacing them evenly around the circle)
            double angle = i * 2 * Math.PI / numBars;

            // Calculate the position on the circle's circumference
            double xStart = centerX + Math.cos(angle) * radius; // X position of the bar on the circle's perimeter
            double yStart = centerY + Math.sin(angle) * radius; // Y position of the bar on the circle's perimeter

            // Calculate the end position of the bar based on its height
            double xEnd = centerX + Math.cos(angle) * (radius + barHeight); // X end position of the bar (extends outward)
            double yEnd = centerY + Math.sin(angle) * (radius + barHeight); // Y end position of the bar (extends outward)

            // Set the color for each bar (using the color spectrum)
            gc.setStroke(Color.hsb(i * 360.0 / numBars, 1.0, 1.0)); // HSB Color Spectrum

            // Draw the bar as a line from the center to the calculated position on the perimeter
            gc.setLineWidth(3); // Optional: Adjust the width of the lines/bars
            gc.strokeLine(xStart, yStart, xEnd, yEnd); // Draw a line from the center to the outer edge (the bar)
        }
    }




}
