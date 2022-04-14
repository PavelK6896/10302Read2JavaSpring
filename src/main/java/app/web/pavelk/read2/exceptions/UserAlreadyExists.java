package app.web.pavelk.read2.exceptions;

public class UserAlreadyExists extends RuntimeException {
    public UserAlreadyExists(String message) {
        super(message);
    }
}
