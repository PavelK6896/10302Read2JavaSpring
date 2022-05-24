package app.web.pavelk.read2.service;

import app.web.pavelk.read2.dto.*;
import app.web.pavelk.read2.exceptions.ExceptionMessage;
import app.web.pavelk.read2.exceptions.InvalidTokenException;
import app.web.pavelk.read2.exceptions.SubReadException;
import app.web.pavelk.read2.exceptions.UserAlreadyExists;
import app.web.pavelk.read2.repository.UserRepository;
import app.web.pavelk.read2.repository.VerificationTokenRepository;
import app.web.pavelk.read2.schema.User;
import app.web.pavelk.read2.schema.VerificationToken;
import app.web.pavelk.read2.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static app.web.pavelk.read2.exceptions.UserAlreadyExists.SUCH_A_USER_ALREADY_EXISTS;
import static org.springframework.http.HttpStatus.FOUND;
import static org.springframework.http.HttpStatus.OK;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    public static final String USER_REGISTRATION_SUCCESSFUL = "User registration successful.";
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailsService userDetailsServiceImpl;
    @Value("${host:https}")
    private String host;

    @Transactional
    public ResponseEntity<String> signUp(RegisterRequest registerRequest) {
        log.debug("signUp {}", registerRequest);
        User setUser;
        Optional<User> byUsername = userRepository.findByUsername(registerRequest.getUsername());
        if (byUsername.isPresent()) {
            if (byUsername.get().isEnabled()) {
                throw new UserAlreadyExists(SUCH_A_USER_ALREADY_EXISTS);
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
        setUser.setEnabled(false);

        userRepository.save(setUser);
        String token = generateVerificationToken(setUser);

        mailService.sendMail(NotificationEmail.builder()
                .subject("Please Activate your Account")
                .recipient(setUser.getEmail())
                .body("Thank you for signing up to Spring Reddit, " +
                        "please click on the below url to activate your account: " +
                        host + "/api/read2/api/auth/accountVerification/" + token
                ).build());
        return ResponseEntity.status(OK).body(USER_REGISTRATION_SUCCESSFUL);
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public ResponseEntity<Void> verifyAccount(String token) {
        log.info("verifyAccount");
        fetchUserAndEnable(verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid verification Token")));
        return ResponseEntity.status(FOUND).header("Location", host + "/read2").build();
    }

    @Transactional
    public void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new SubReadException(ExceptionMessage.USER_NOT_FOUND.getBodyEn().formatted(username)));
        user.setEnabled(true);
        userRepository.save(user);
    }


    public ResponseEntity<AuthenticationResponse> signIn(LoginRequest loginRequest) {
        log.debug("signIn");
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(loginRequest.getUsername());
        if (userDetails == null || !userDetails.isEnabled()
                || !passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bad credentials");
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        AuthenticationResponse authenticationResponse = jwtProvider.generateToken((UserDetails) authentication.getPrincipal());
        authenticationResponse.setRefreshToken(refreshTokenService.generateRefreshToken().getToken());

        return ResponseEntity.status(OK).body(authenticationResponse);
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {

        org.springframework.security.core.userdetails.User principal
                = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();

        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found " + principal.getUsername()));

    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

    public ResponseEntity<AuthenticationResponse> refreshToken(RefreshTokenRequest refreshTokenRequest) {
        log.info("refreshTokens");
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());

        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(refreshTokenRequest.getUsername());

        AuthenticationResponse authenticationResponse = jwtProvider.generateToken(userDetails);
        authenticationResponse.setRefreshToken(refreshTokenService.generateRefreshToken().getToken());

        return ResponseEntity.status(OK).body(authenticationResponse);
    }
}
