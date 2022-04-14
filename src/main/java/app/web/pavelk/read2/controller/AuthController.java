package app.web.pavelk.read2.controller;

import app.web.pavelk.read2.dto.AuthenticationResponse;
import app.web.pavelk.read2.dto.LoginRequest;
import app.web.pavelk.read2.dto.RefreshTokenRequest;
import app.web.pavelk.read2.dto.RegisterRequest;
import app.web.pavelk.read2.service.AuthService;
import app.web.pavelk.read2.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@RequestBody @Valid RegisterRequest registerRequest) {
        return authService.signUp(registerRequest);
    }

    @GetMapping("accountVerification/{token}")
    public ResponseEntity<Void> verifyAccount(@PathVariable String token) {
        return authService.verifyAccount(token);
    }


    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest) {
        return authService.signIn(loginRequest);
    }

    @PostMapping("/refresh/token")
    public ResponseEntity<AuthenticationResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
    }

}
