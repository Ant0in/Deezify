package musicApp.exceptions;

public class BadM3URadioException extends BadSongException {
    public BadM3URadioException(String message) {
        super(message);
    }

    public BadM3URadioException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadM3URadioException(Throwable cause) {
        super(cause);
    }
}

