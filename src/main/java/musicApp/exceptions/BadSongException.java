package musicApp.exceptions;

public class BadSongException extends Exception {
    public BadSongException(String message) {
        super(message);
    }

    public BadSongException(Throwable cause) {
        super(cause);
    }
}

