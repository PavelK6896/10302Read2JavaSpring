package app.web.pavelk.read2.exceptions;

public class SubReadException extends RuntimeException {
    public SubReadException(String message) {
        super(message);
    }

    public SubReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
