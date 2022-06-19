package app.web.pavelk.read2.service;

import app.web.pavelk.read2.dto.AuthenticationResponse;
import app.web.pavelk.read2.dto.LoginRequest;
import app.web.pavelk.read2.dto.RefreshTokenRequest;
import app.web.pavelk.read2.dto.RegisterRequest;
import org.springframework.http.ResponseEntity;

/**
 * Service authorization.
 */
public interface AuthService {
    /**
     * Create new account.
     *
     * @param registerRequest data need of register
     * @return message or exception
     */
    ResponseEntity<String> signUp(RegisterRequest registerRequest);

    /**
     * Approve new account.
     *
     * @param token key for new account
     * @return status 302
     */
    ResponseEntity<Void> verifyAccount(String token);

    /**
     * Login in site.
     *
     * @param loginRequest name and password
     * @return token data
     */
    ResponseEntity<AuthenticationResponse> signIn(LoginRequest loginRequest);

    /**
     * Re login on refresh token.
     *
     * @param refreshTokenRequest refresh token
     * @return token data
     */
    ResponseEntity<AuthenticationResponse> refreshToken(RefreshTokenRequest refreshTokenRequest);

}
