package musicApp.exceptions;

public class EqualizerGainException extends Exception {
    public EqualizerGainException(String message) {
        super(message);
    }

    public EqualizerGainException(String message, Throwable cause) {
        super(message, cause);
    }
}
