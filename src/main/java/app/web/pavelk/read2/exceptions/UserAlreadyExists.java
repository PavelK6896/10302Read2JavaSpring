package app.web.pavelk.read2.exceptions;

public class UserAlreadyExists extends RuntimeException {
    public static final String SUCH_A_USER_ALREADY_EXISTS = "Such a user already exists.";

    public UserAlreadyExists(String message) {
        super(message);
    }
}
