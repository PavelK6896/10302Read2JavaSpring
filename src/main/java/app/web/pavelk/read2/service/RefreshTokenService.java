package app.web.pavelk.read2.service;

import app.web.pavelk.read2.schema.RefreshToken;
import org.springframework.http.ResponseEntity;

public interface RefreshTokenService {
    RefreshToken generateRefreshToken();

    RefreshToken validateRefreshToken(String token);

    ResponseEntity<String> deleteRefreshToken(String token);
}
