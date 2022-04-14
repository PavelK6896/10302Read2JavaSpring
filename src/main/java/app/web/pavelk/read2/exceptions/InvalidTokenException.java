package app.web.pavelk.read2.exceptions;


import org.springframework.security.core.AuthenticationException;

public class InvalidTokenException extends AuthenticationException {
    public InvalidTokenException(String msg) {
        super(msg);
    }
}
