package app.web.pavelk.read2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data authentication")
public class AuthenticationResponse {
    @Schema(description = "Toke")
    private String authenticationToken;
    @Schema(description = "Refresh")
    private String refreshToken;
    @Schema(description = "Expires token date")
    private LocalDateTime expiresAt;
    @Schema(description = "User name")
    private String username;
}
