package musicApp.services;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertService {
    private final LanguageService languageService = LanguageService.getInstance();


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
            case ERROR -> languageService.get("alert.title.error");
            case WARNING -> languageService.get("alert.title.warning");
            case INFORMATION -> languageService.get("alert.title.information");
            case CONFIRMATION -> languageService.get("alert.title.confirmation");
            default -> languageService.get("alert.title.default");
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
            case ERROR -> languageService.get("alert.header.error");
            case WARNING -> languageService.get("alert.header.warning");
            case INFORMATION -> languageService.get("alert.header.information");
            case CONFIRMATION -> languageService.get("alert.header.confirmation");
            default -> languageService.get("alert.header.default");
        };
    }

    /**
     * Displays a fatal error alert and exits the application after user acknowledgment.
     *
     * @param message The error message to display
     * @param ex The exception that caused the fatal error (can be null)
     */
    public void showFatalErrorAlert(String message, Exception ex) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(languageService.get("alert.title.fatal"));
        alert.setHeaderText(languageService.get("alert.header.fatal"));

        String content = message;
        if (ex != null && ex.getMessage() != null) {
            content += "\n\nError details: " + ex.getMessage();
        }

        alert.setContentText(content);
        alert.setResizable(true);
        alert.showAndWait();

        System.exit(1);
    }

}
