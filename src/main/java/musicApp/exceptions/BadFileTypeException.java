package MusicApp.Exceptions;


public class BadFileTypeException extends Exception {

    public BadFileTypeException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

}
