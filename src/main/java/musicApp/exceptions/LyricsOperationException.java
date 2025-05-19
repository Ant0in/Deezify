package musicApp.exceptions;

/**
 * Exception thrown when a lyrics operation fails.
 */
public class LyricsOperationException extends Exception {

    public LyricsOperationException(String message) {
        super(message);
    }

    public LyricsOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
