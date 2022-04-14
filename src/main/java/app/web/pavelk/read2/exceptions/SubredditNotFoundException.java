package app.web.pavelk.read2.exceptions;

public class SubredditNotFoundException extends RuntimeException {
    public SubredditNotFoundException(String message) {
        super(message);
    }
}
