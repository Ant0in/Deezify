package musicApp.exceptions;

public class BadSongException extends Exception {
    public BadSongException(String message) {
        super(message);
    }

    public BadSongException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadSongException(Throwable cause) {
        super(cause);
    }
}

