package musicApp.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class AlertService {
    private final LanguageManager languageManager = LanguageManager.getInstance();


    public void showAlert(String contentText, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(getTitle(alertType));
        alert.setHeaderText(getHeaderText(alertType));
        alert.setContentText(contentText);
        alert.setResizable(true);
        alert.showAndWait();
    }

    public void showExceptionAlert(Exception ex) {
        showExceptionAlert(ex, AlertType.ERROR);
    }

    /**
     * Displays an alert dialog to show information about an exception.
     *
     * @param ex The exception to display. If {@code null}, the alert will display a default message indicating no exception is available.
     * @param alertType The type of alert to show (e.g., ERROR, WARNING, INFO).
     */
    public void showExceptionAlert(Exception ex, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setResizable(true);
        alert.setTitle(getTitle(alertType));
        alert.setHeaderText(getHeaderText(alertType));
        String content = (ex == null) ? "No exception available." :
                (ex.getMessage() != null) ? ex.getMessage() : "An unexpected error occurred.";
        alert.setContentText(content);
        alert.showAndWait();
    }
    /**
     * Retrieves the title for the alert based on the specified alert type.
     *
     * @param alertType The type of alert (e.g., ERROR, WARNING, INFO, CONFIRMATION).
     * @return The title string corresponding to the alert type.
     */
    private String getTitle(AlertType alertType) {
        return switch (alertType) {
            case ERROR -> languageManager.get("alert.title.error");
            case WARNING -> languageManager.get("alert.title.warning");
            case INFORMATION -> languageManager.get("alert.title.information");
            case CONFIRMATION -> languageManager.get("alert.title.confirmation");
            default -> languageManager.get("alert.title.default");
        };
    }
    /**
     * Retrieves the header text for the alert based on the specified alert type.
     *
     * @param alertType The type of alert (e.g., ERROR, WARNING, INFO, CONFIRMATION).
     * @return The header text string corresponding to the alert type.
     */
    private String getHeaderText(AlertType alertType) {
        return switch (alertType) {
            case ERROR -> languageManager.get("alert.header.error");
            case WARNING -> languageManager.get("alert.header.warning");
            case INFORMATION -> languageManager.get("alert.header.information");
            case CONFIRMATION -> languageManager.get("alert.header.confirmation");
            default -> languageManager.get("alert.header.default");
        };
    }

}
