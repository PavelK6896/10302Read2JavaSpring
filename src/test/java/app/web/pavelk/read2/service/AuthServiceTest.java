package app.web.pavelk.read2.service;

import app.web.pavelk.read2.config.properties.AppProperties;
import app.web.pavelk.read2.dto.RegisterRequest;
import app.web.pavelk.read2.exceptions.Read2Exception;
import app.web.pavelk.read2.repository.UserRepository;
import app.web.pavelk.read2.repository.VerificationTokenRepository;
import app.web.pavelk.read2.schema.User;
import app.web.pavelk.read2.security.JwtProvider;
import app.web.pavelk.read2.service.impl.AuthServiceFirstImpl;
import app.web.pavelk.read2.service.impl.MailServiceFirstImpl;
import app.web.pavelk.read2.service.impl.RefreshTokenServiceFirstImpl;
import app.web.pavelk.read2.service.impl.UserDetailsServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

class AuthServiceTest {

    PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    UserRepository userRepository = Mockito.mock(UserRepository.class);
    VerificationTokenRepository verificationTokenRepository = Mockito.mock(VerificationTokenRepository.class);
    MailServiceFirstImpl mailService = Mockito.mock(MailServiceFirstImpl.class);
    JwtProvider jwtProvider = Mockito.mock(JwtProvider.class);
    RefreshTokenServiceFirstImpl refreshTokenService = Mockito.mock(RefreshTokenServiceFirstImpl.class);
    UserDetailsServiceImpl userDetailsService = Mockito.mock(UserDetailsServiceImpl.class);
    AppProperties appProperties = Mockito.mock(AppProperties.class);

    @Test
    void userIsEnabledTrue() {
        AuthService authService = new AuthServiceFirstImpl(passwordEncoder, userRepository, verificationTokenRepository, mailService, jwtProvider, refreshTokenService, userDetailsService, appProperties);
        User userReturn = User.builder()
                .password("passwordReturn")
                .enabled(true)
                .username("usernameReturn")
                .build();
        when(userRepository.findByUsername(Mockito.any())).thenReturn(Optional.of(userReturn));
        RegisterRequest registerRequest = RegisterRequest.builder()
                .email("email")
                .password("password")
                .username("username")
                .build();
        Assertions.assertThrows(Read2Exception.class, () -> {
            authService.signUp(registerRequest);
        });
    }

    @Test
    void userIsEnabledFalse() {
        AuthService authService = new AuthServiceFirstImpl(passwordEncoder, userRepository, verificationTokenRepository, mailService, jwtProvider, refreshTokenService, userDetailsService, appProperties);
        User userReturn = User.builder()
                .password("passwordReturn")
                .enabled(false)
                .username("usernameReturn")
                .build();
        when(userRepository.findByUsername(Mockito.any())).thenReturn(Optional.of(userReturn));
        RegisterRequest registerRequest = RegisterRequest.builder()
                .email("email")
                .password("password")
                .username("username")
                .build();
        ResponseEntity<String> stringResponseEntity = authService.signUp(registerRequest);
        Assertions.assertEquals(OK, stringResponseEntity.getStatusCode());
    }
}
