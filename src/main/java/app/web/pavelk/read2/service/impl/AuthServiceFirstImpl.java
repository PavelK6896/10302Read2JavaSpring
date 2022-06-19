package app.web.pavelk.read2.service.impl;

import app.web.pavelk.read2.config.properties.AppProperties;
import app.web.pavelk.read2.dto.AuthenticationResponse;
import app.web.pavelk.read2.dto.LoginRequest;
import app.web.pavelk.read2.dto.RefreshTokenRequest;
import app.web.pavelk.read2.dto.RegisterRequest;
import app.web.pavelk.read2.exceptions.ExceptionMessage;
import app.web.pavelk.read2.exceptions.InvalidTokenException;
import app.web.pavelk.read2.exceptions.Read2Exception;
import app.web.pavelk.read2.repository.UserRepository;
import app.web.pavelk.read2.repository.VerificationTokenRepository;
import app.web.pavelk.read2.schema.User;
import app.web.pavelk.read2.schema.VerificationToken;
import app.web.pavelk.read2.security.JwtProvider;
import app.web.pavelk.read2.service.AuthService;
import app.web.pavelk.read2.service.MailService;
import app.web.pavelk.read2.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static app.web.pavelk.read2.exceptions.ExceptionMessage.REFRESH_TOKEN_NOT_FOUND;
import static app.web.pavelk.read2.exceptions.ExceptionMessage.USER_EXISTS;
import static app.web.pavelk.read2.util.StaticField.USER_REGISTRATION_SUCCESSFUL;
import static org.springframework.http.HttpStatus.FOUND;
import static org.springframework.http.HttpStatus.OK;


@Slf4j(topic = "auth-service-first")
@Service
@RequiredArgsConstructor
public class AuthServiceFirstImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailsService userDetailsServiceImpl;
    private final AppProperties appProperties;

    @Override
    @Transactional
    public ResponseEntity<String> signUp(RegisterRequest registerRequest) {
        log.debug("signUp {}", registerRequest);
        User setUser;
        Optional<User> byUsername = userRepository.findByUsername(registerRequest.getUsername());
        if (byUsername.isPresent()) {
            if (byUsername.get().isEnabled()) {
                throw new Read2Exception(USER_EXISTS.getMessage());
            } else {
                setUser = byUsername.get();
            }
        } else {
            setUser = new User();
        }

        setUser.setUsername(registerRequest.getUsername());
        setUser.setEmail(registerRequest.getEmail());
        setUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        setUser.setCreated(Instant.now());
        setUser.setEnabled(!appProperties.isNotificationSingUp());
        setUser.setRoles(List.of(User.Role.USER));

        userRepository.save(setUser);
        String token = generateVerificationToken(setUser);
        if (appProperties.isNotificationSingUp()) {
            mailService.sendAuthNotification(setUser.getEmail(), token);
        }
        return ResponseEntity.status(OK).body(USER_REGISTRATION_SUCCESSFUL);
    }

    @Override
    @Transactional
    public ResponseEntity<Void> verifyAccount(String token) {
        log.debug("verifyAccount");
        fetchUserAndEnable(verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException(REFRESH_TOKEN_NOT_FOUND.getMessage())));
        return ResponseEntity.status(FOUND).header("Location", appProperties.getHost() + "/read2").build();
    }

    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new Read2Exception(ExceptionMessage.USER_NOT_FOUND.getMessage().formatted(username)));
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public ResponseEntity<AuthenticationResponse> signIn(LoginRequest loginRequest) {
        log.debug("signIn");
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(loginRequest.getUsername());
        if (userDetails == null || !userDetails.isEnabled()
                || !passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, HttpStatus.FORBIDDEN.getReasonPhrase());
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        AuthenticationResponse authenticationResponse = jwtProvider.generateToken((UserDetails) authentication.getPrincipal());
        authenticationResponse.setRefreshToken(refreshTokenService.generateRefreshToken().getToken());

        return ResponseEntity.status(OK).body(authenticationResponse);
    }

    @Override
    @Transactional
    public ResponseEntity<AuthenticationResponse> refreshToken(RefreshTokenRequest refreshTokenRequest) {
        log.debug("refreshTokens");
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());

        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(refreshTokenRequest.getUsername());

        AuthenticationResponse authenticationResponse = jwtProvider.generateToken(userDetails);
        authenticationResponse.setRefreshToken(refreshTokenService.generateRefreshToken().getToken());

        return ResponseEntity.status(OK).body(authenticationResponse);
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationTokenRepository.save(verificationToken);
        return token;
    }

}
