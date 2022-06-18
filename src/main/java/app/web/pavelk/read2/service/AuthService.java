package app.web.pavelk.read2.service;

import app.web.pavelk.read2.dto.AuthenticationResponse;
import app.web.pavelk.read2.dto.LoginRequest;
import app.web.pavelk.read2.dto.RefreshTokenRequest;
import app.web.pavelk.read2.dto.RegisterRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<String> signUp(RegisterRequest registerRequest);

    ResponseEntity<Void> verifyAccount(String token);

    ResponseEntity<AuthenticationResponse> signIn(LoginRequest loginRequest);

    ResponseEntity<AuthenticationResponse> refreshToken(RefreshTokenRequest refreshTokenRequest);

}
