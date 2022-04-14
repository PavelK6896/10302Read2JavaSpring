package app.web.pavelk.read2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String authenticationToken;
    private String refreshToken;
    private LocalDateTime expiresAt;
    private String username;
}
