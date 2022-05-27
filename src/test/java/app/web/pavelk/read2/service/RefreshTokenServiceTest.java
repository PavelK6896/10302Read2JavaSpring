package app.web.pavelk.read2.service;

import app.web.pavelk.read2.exceptions.InvalidTokenException;
import app.web.pavelk.read2.repository.RefreshTokenRepository;
import app.web.pavelk.read2.schema.RefreshToken;
import app.web.pavelk.read2.service.impl.RefreshTokenServiceFirstImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

class RefreshTokenServiceTest {

    String token = "tokenTest";
    RefreshTokenRepository refreshTokenRepository = Mockito.mock(RefreshTokenRepository.class);
    RefreshToken refreshToken = RefreshToken
            .builder()
            .token(token)
            .id(1L)
            .createdDate(Instant.now())
            .build();
    RefreshTokenService refreshTokenService = new RefreshTokenServiceFirstImpl(refreshTokenRepository);

    @Test
    void rtSave() {
        when(refreshTokenRepository.save(Mockito.any())).thenReturn(refreshToken);
        RefreshToken refreshToken1 = refreshTokenService.generateRefreshToken();
        assertNotNull(refreshToken1);
    }

    @Test
    void rtFindRight() {
        when(refreshTokenRepository.findByToken(Mockito.any())).thenReturn(Optional.of(refreshToken));
        RefreshToken refreshToken1 = refreshTokenService.validateRefreshToken(token);
        assertNotNull(refreshToken1);
    }

    @Test
    void rtFindWrong() {
        when(refreshTokenRepository.findByToken(Mockito.any())).thenReturn(Optional.empty());
        Assertions.assertThrows(InvalidTokenException.class, () -> {
            refreshTokenService.validateRefreshToken(token);
        });
    }

    @Test
    void deleteRight() {
        ResponseEntity<String> stringResponseEntity = refreshTokenService.deleteRefreshToken(token);
        Assertions.assertEquals(OK, stringResponseEntity.getStatusCode());
    }
}
