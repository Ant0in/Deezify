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
import musicApp.controllers.MiniPlayerController;

import java.util.List;

/**
 *  View component used to render the audio spectrum visualizer in a separate window (popup)
 */
public class MiniPlayerView extends View<MiniPlayerView, MiniPlayerController>{
    @FXML
    Label songTitleLabel;

    @FXML
    ImageView coverImage;
    @FXML
    Canvas canvas;
    // Flag to track whether the image is clipped or not
    private boolean isClipped = true;

    Boolean isBasicMode = false;

    @Override
    public void init() {
        scene.setOnMouseClicked(_ -> changeVisualizerMode());
    }
    private void changeVisualizerMode() {
        isBasicMode = !isBasicMode;
        toggleClip();
    }

    public void draw(List<Float> values) {
        if (isBasicMode) {
            drawFrame(values);
        } else {
            drawCircularFrame(values);
        }
    }

    public Scene getScene() { return scene; }

    public void updateSongProperties(String title, Image coverImage) {
        songTitleLabel.setText(title);
        setCoverImage(coverImage);
    }

    public void setCoverImage(javafx.scene.image.Image image) {
        coverImage.setImage(image);
    }


    // Method to switch between clipped and non-clipped states
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

    private void drawFrame( List<Float> values) {
        int numBars = viewController.getBandsNumber();
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

    private void drawCircularFrame(List<Float> values) {
        int numBars = viewController.getBandsNumber();
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

            // Calculate the angle for the current bar
            double angle = i * 2 * Math.PI / numBars;

            // Calculate the position of the top of the bar along the circle's perimeter
            double xStart = centerX + Math.cos(angle) * (radius + barHeight); // Position on X axis
            double yStart = centerY + Math.sin(angle) * (radius + barHeight); // Position on Y axis

            // Set the color for each bar (using the color spectrum)
            gc.setFill(Color.hsb(i * 360.0 / numBars, 1.0, 1.0)); // Color spectrum

            // Draw the bars as circles at calculated positions
            gc.fillRect(xStart - 2.5, yStart - 2.5, 5, 5); // Small rectangle (circle equivalent) for the bar
        }
    }


}
