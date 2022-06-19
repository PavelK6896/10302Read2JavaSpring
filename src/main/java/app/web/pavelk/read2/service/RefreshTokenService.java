package app.web.pavelk.read2.service;

import app.web.pavelk.read2.schema.RefreshToken;
import org.springframework.http.ResponseEntity;

/**
 * Service logic refresh token.
 */
public interface RefreshTokenService {
    /**
     * Create new token refresh.
     *
     * @return token entity
     */
    RefreshToken generateRefreshToken();

    /**
     * Validate token on db.
     *
     * @param token request token
     * @return token entity
     */
    RefreshToken validateRefreshToken(String token);

    /**
     * Delete token.
     *
     * @param token request token
     * @return status 200
     */
    ResponseEntity<String> deleteRefreshToken(String token);
}
