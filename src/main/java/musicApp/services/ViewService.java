package musicApp.services;

import javafx.scene.control.ButtonBase;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class ViewService {

    /**
     * Creates an ImageView from a resource path with predefined dimensions.
     *
     * @param path The path to the image resource.
     * @return The ImageView, or null if loading fails.
     */
    public ImageView createIcon(String path) throws NullPointerException {
        ImageView icon = new ImageView(Objects.requireNonNull(getClass().getResource(path)).toExternalForm());
        icon.setFitWidth(20);
        icon.setFitHeight(20);
        return icon;
    }

    /**
     * Sets the graphic icon for a button.
     *
     * @param button    The button to set the icon for.
     * @param imagePath The path to the image resource.
     */
    public void setButtonIcon(ButtonBase button, String imagePath, ViewServiceListener viewServiceListener) {
        try {
            ViewService viewService = new ViewService();
            ImageView icon = viewService.createIcon(imagePath);
            button.setGraphic(icon);
        } catch (NullPointerException _) {
            viewServiceListener.handleNotFoundImage("Could not load icon : " + imagePath);
        }
    }

    public interface ViewServiceListener {
        void handleNotFoundImage(String message);
    }
}
