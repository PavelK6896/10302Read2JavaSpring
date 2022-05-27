package app.web.pavelk.read2.controller;

import app.web.pavelk.read2.dto.AuthenticationResponse;
import app.web.pavelk.read2.dto.LoginRequest;
import app.web.pavelk.read2.dto.RefreshTokenRequest;
import app.web.pavelk.read2.dto.RegisterRequest;
import app.web.pavelk.read2.service.AuthService;
import app.web.pavelk.read2.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @Operation(description = "Sign up.")
    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody @Valid RegisterRequest registerRequest) {
        return authService.signUp(registerRequest);
    }

    @Operation(description = "Verify account.")
    @Parameter(in = ParameterIn.PATH, name = "token", schema = @Schema(defaultValue = "sdfgdshgjghjfg"))
    @GetMapping("account-verification/{token}")
    public ResponseEntity<Void> verifyAccount(@PathVariable String token) {
        return authService.verifyAccount(token);
    }

    @Operation(description = "Sign in.")
    @PostMapping("/sign-in")
    public ResponseEntity<AuthenticationResponse> signIn(@RequestBody LoginRequest loginRequest) {
        return authService.signIn(loginRequest);
    }

    @Operation(description = "Refresh token.")
    @PostMapping("/refresh/token")
    public ResponseEntity<AuthenticationResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest);
    }

    @Operation(description = "Sign out.")
    @PostMapping("/sign-out")
    public ResponseEntity<String> signOut(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
    }

}
