package musicApp.exceptions;


public class ID3TagException extends Exception {

    public ID3TagException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

}
