package app.web.pavelk.read2.service.impl;


import app.web.pavelk.read2.exceptions.InvalidTokenException;
import app.web.pavelk.read2.repository.RefreshTokenRepository;
import app.web.pavelk.read2.schema.RefreshToken;
import app.web.pavelk.read2.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

import static app.web.pavelk.read2.exceptions.ExceptionMessage.REFRESH_TOKEN_NOT_FOUND;
import static app.web.pavelk.read2.util.StaticField.REFRESH_TOKEN_DELETED;
import static org.springframework.http.HttpStatus.OK;


@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class RefreshTokenServiceFirstImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken generateRefreshToken() {
        log.debug("generateRefreshToken");
        return refreshTokenRepository.save(RefreshToken.builder()
                .createdDate(Instant.now()).token(UUID.randomUUID().toString()).build());
    }

    @Override
    public RefreshToken validateRefreshToken(String token) {
        log.debug("validateRefreshToken");
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException(REFRESH_TOKEN_NOT_FOUND.getMessage()));
    }

    @Override
    public ResponseEntity<String> deleteRefreshToken(String token) {
        log.debug("deleteRefreshToken");
        refreshTokenRepository.deleteByToken(token);
        return ResponseEntity.status(OK).body(REFRESH_TOKEN_DELETED);


    }
}
