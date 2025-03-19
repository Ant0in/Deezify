package MusicApp.exceptions;


public class BadFileTypeException extends Exception {

    public BadFileTypeException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

}
